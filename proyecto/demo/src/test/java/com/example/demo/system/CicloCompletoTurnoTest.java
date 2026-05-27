package com.example.demo.system;

import com.example.demo.BaseIntegrationTest;
import com.example.demo.model.*;
import org.junit.jupiter.api.*;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ╔══════════════════════════════════════════════════════════════════════════╗
 * ║              PRUEBA DE SISTEMA — Ciclo Completo de Supervisión          ║
 * ╚══════════════════════════════════════════════════════════════════════════╝
 *
 * <p>Caso de uso: <b>"Supervisión completa de un turno escolar"</b></p>
 *
 * <p>Es el flujo más largo y complejo del sistema porque encadena todas las
 * entidades y efectos secundarios en orden cronológico realista:</p>
 *
 * <ol>
 *   <li>Login del administrador (autenticación JWT).</li>
 *   <li>Creación de docente y zona vía API REST.</li>
 *   <li>Creación de turno PENDIENTE → notificación ASIGNACION_TURNO al docente.</li>
 *   <li>Registro de recorrido en el turno → notificaciones REGISTRO_RECORRIDO
 *       a coordinadores y administradores.</li>
 *   <li>Registro de checkpoint en el recorrido.</li>
 *   <li>Registro de check-in del docente en el turno.</li>
 *   <li>Registro de incidente → notificaciones REPORTE_INCIDENTE a coord+admin.</li>
 *   <li>Cambio de estado PENDIENTE→EN_CURSO → notificaciones CONFIRMACION_CHECKIN
 *       (docente + coord + admin).</li>
 *   <li>Solicitud de reasignación → notificaciones PROPUESTA_REEMPLAZO a coord+admin.</li>
 *   <li>Respuesta a la reasignación (estado→ACEPTADA) → notificación al docente
 *       solicitante.</li>
 *   <li>Cambio de estado EN_CURSO→CERRADO → notificaciones CIERRE_TURNO.</li>
 *   <li>Verificación final: el administrador consulta su bandeja y recibe todas
 *       las notificaciones esperadas del flujo.</li>
 * </ol>
 *
 * <p>Todo el flujo se ejecuta exclusivamente a través de la API HTTP real,
 * sin mocks ni accesos directos al repositorio durante las llamadas de negocio.</p>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CicloCompletoTurnoTest extends BaseIntegrationTest {

    // ── Estado compartido entre los pasos del test (se inicializa en @BeforeAll) ──

    /** ID del administrador creado directamente en BD para bootstrap. */
    private static Long adminId;
    /** Email del administrador (para login). */
    private static String adminEmail;
    /** Contraseña en texto plano del administrador. */
    private static String adminPassword;
    /** JWT del administrador, válido durante toda la prueba. */
    private static String tokenAdmin;

    /** IDs obtenidos de las respuestas de la API a lo largo del test. */
    private static Long docenteId;
    private static Long coordId;
    private static Long zonaId;
    private static Long turnoId;
    private static Long recorridoId;
    private static Long checkpointId;
    private static Long checkinId;
    private static Long incidenteId;
    private static Long reasignacionId;
    private static Long docenteReemplazoId;

    // ─────────────────────────────────────────────────────────────────────────
    // Setup: crear actores base directamente en BD (no a través de la API
    // porque aún no tenemos token de autenticación)
    // ─────────────────────────────────────────────────────────────────────────

    @BeforeEach
    void bootstrapSiNoInicializado() {
        // Solo inicializar una vez para todo el flujo
        if (adminId != null) return;

        String ts = String.valueOf(System.nanoTime());
        adminEmail    = "admin.sistema." + ts + "@test.edu";
        adminPassword = "pwd-sistema-" + ts;

        Administrador admin = crearAdmin("sistema-" + ts);
        // Sobreescribir email/password con los valores calculados
        admin.setEmail(adminEmail);
        admin.setPasswordHash(adminPassword);
        administradorRepository.save(admin);
        adminId = admin.getId();

        // Coordinador para que reciba notificaciones durante el flujo
        Coordinador coord = crearCoordinador("sis-coord-" + ts);
        coordId = coord.getId();

        // Login para obtener el JWT que se usará en todos los pasos siguientes
        tokenAdmin = login(adminEmail, adminPassword);
    }

    @AfterAll
    static void resetearEstadoEstatico() {
        // Resetear estado estático para permitir reusos futuros en el mismo JVM
        adminId = coordId = docenteId = zonaId = turnoId = null;
        recorridoId = checkpointId = checkinId = incidenteId = null;
        reasignacionId = docenteReemplazoId = null;
        adminEmail = adminPassword = tokenAdmin = null;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Paso 0 — Verificar que el login fue exitoso
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("Paso 1: Login del administrador devuelve JWT válido")
    void paso01_loginAdministradorObtenerJwt() {
        // Verificar que el token fue generado en @BeforeEach
        assertThat(tokenAdmin)
                .as("El JWT del administrador no debe ser nulo ni vacío")
                .isNotNull()
                .isNotBlank();

        // Confirmar con GET /api/auth/me que el token es válido
        HttpEntity<Void> req = new HttpEntity<>(bearerHeaders(tokenAdmin));
        ResponseEntity<Map> me = restTemplate.exchange("/api/auth/me", HttpMethod.GET, req, Map.class);

        assertThat(me.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(me.getBody().get("email")).isEqualTo(adminEmail);
        assertThat(me.getBody().get("rol")).isEqualTo("ADMINISTRADOR");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Paso 2 — Crear docente vía API
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(2)
    @DisplayName("Paso 2: Crear docente vía POST /api/docentes")
    void paso02_crearDocente() {
        String ts = String.valueOf(System.nanoTime());
        Map<String, Object> body = new HashMap<>();
        body.put("nombre",             "Prof. Carlos Ramírez");
        body.put("email",              "carlos.ramirez." + ts + "@colegio.edu");
        body.put("passwordHash",       "pwd-docente-" + ts);
        body.put("activo",             true);
        body.put("materias",           "Matemáticas, Física");
        body.put("cargaActual",        0);
        body.put("puntajeGamificacion", 0);

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/docentes", req, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().get("id")).isNotNull();
        docenteId = ((Number) response.getBody().get("id")).longValue();

        // Crear también un docente de reemplazo para la reasignación posterior
        body.put("nombre", "Prof. María Reemplazo");
        body.put("email", "maria.reemplazo." + ts + "@colegio.edu");
        ResponseEntity<Map> resp2 = restTemplate.postForEntity("/api/docentes", req, Map.class);
        assertThat(resp2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        docenteReemplazoId = ((Number) resp2.getBody().get("id")).longValue();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Paso 3 — Crear zona vía API
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(3)
    @DisplayName("Paso 3: Crear zona vía POST /api/zonas")
    void paso03_crearZona() {
        String ts = String.valueOf(System.nanoTime());
        Map<String, Object> body = Map.of(
                "nombre",          "Patio Principal ST-" + ts,
                "descripcion",     "Zona de vigilancia en recreo",
                "ubicacion",       "Bloque A",
                "capacidadMaxima", 300,
                "activa",          true
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/zonas", req, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        zonaId = ((Number) response.getBody().get("id")).longValue();
        assertThat(response.getBody().get("nombre").toString()).startsWith("Patio Principal ST-");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Paso 4 — Crear turno PENDIENTE → notificación ASIGNACION_TURNO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(4)
    @DisplayName("Paso 4: Crear turno PENDIENTE → notificación ASIGNACION_TURNO al docente")
    void paso04_crearTurnoPendiente_generaNotificacionAsignacion() {
        long notifAntes = notificacionRepository.count();

        Map<String, Object> body = Map.of(
                "docenteId",  docenteId,
                "zonaId",     zonaId,
                "fecha",      LocalDate.now().plusDays(1).toString(),
                "horaInicio", "09:30:00",
                "horaFin",    "09:50:00",
                "franja",     "Recreo mañana primaria",
                "estado",     "PENDIENTE"
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/turnos", req, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        turnoId = ((Number) response.getBody().get("id")).longValue();
        assertThat(response.getBody().get("estado")).isEqualTo("PENDIENTE");

        // POST /api/turnos llama notificarAsignacionTurno() → 1 notificación al docente
        assertThat(notificacionRepository.count())
                .as("Debe crearse exactamente 1 notificación ASIGNACION_TURNO")
                .isEqualTo(notifAntes + 1);

        boolean docenteNotificado = notificacionRepository.findAll().stream()
                .anyMatch(n -> n.getDestinatario() != null
                        && n.getDestinatario().getId().equals(docenteId)
                        && n.getTipo() == TipoNotificacion.ASIGNACION_TURNO);
        assertThat(docenteNotificado).isTrue();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Paso 5 — Crear recorrido → notificaciones REGISTRO_RECORRIDO a coord+admin
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(5)
    @DisplayName("Paso 5: Crear recorrido → notificaciones REGISTRO_RECORRIDO a coordinadores y administradores")
    void paso05_crearRecorrido_generaNotificacionesRecorrido() {
        long notifAntes = notificacionRepository.count();

        Map<String, Object> body = Map.of(
                "docenteId",       docenteId,
                "turnoId",         turnoId,
                "iniciadoEn",      "2024-05-10T09:30:00",
                "estado",          "EN_PROGRESO",
                "duracionMinutos", 20
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/recorridos", req, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        recorridoId = ((Number) response.getBody().get("id")).longValue();

        // notificarRecorrido() → notifica a todos los COORDINADOR y ADMINISTRADOR
        long notifNuevas = notificacionRepository.count() - notifAntes;
        assertThat(notifNuevas)
                .as("Deben crearse notificaciones REGISTRO_RECORRIDO para coord y admin")
                .isGreaterThanOrEqualTo(2);

        // El coordinador debe estar entre los notificados
        assertThat(notificacionRepository.findAll()).anyMatch(n ->
                n.getDestinatario() != null
                && n.getDestinatario().getId().equals(coordId)
                && n.getTipo() == TipoNotificacion.REGISTRO_RECORRIDO);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Paso 6 — Registrar checkpoint en el recorrido
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(6)
    @DisplayName("Paso 6: Registrar checkpoint en el recorrido")
    void paso06_crearCheckpoint() {
        Map<String, Object> body = Map.of(
                "zonaId",      zonaId,
                "recorridoId", recorridoId,
                "codigoQR",    "QR-PATIO-001",
                "descripcion", "Entrada principal patio",
                "orden",       1,
                "escaneadoEn", "2024-05-10T09:33:00"
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/checkpoints", req, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        checkpointId = ((Number) response.getBody().get("id")).longValue();
        assertThat(response.getBody().get("codigoQR")).isEqualTo("QR-PATIO-001");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Paso 7 — Registrar check-in del docente
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(7)
    @DisplayName("Paso 7: Registrar check-in del docente en el turno")
    void paso07_crearCheckIn() {
        Map<String, Object> body = Map.of(
                "turnoId",   turnoId,
                "docenteId", docenteId,
                "zonaId",    zonaId,
                "timestamp", "2024-05-10T09:31:00",
                "metodo",    "QR",
                "evidencia", "qr://patio-principal-001",
                "valido",    true
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/checkins", req, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        checkinId = ((Number) response.getBody().get("id")).longValue();
        assertThat(response.getBody().get("metodo")).isEqualTo("QR");
        assertThat((Boolean) response.getBody().get("valido")).isTrue();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Paso 8 — Registrar incidente → notificaciones REPORTE_INCIDENTE
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(8)
    @DisplayName("Paso 8: Registrar incidente → notificaciones REPORTE_INCIDENTE a coordinadores y administradores")
    void paso08_registrarIncidente_generaNotificacionesReporte() {
        long notifAntes = notificacionRepository.count();

        Map<String, Object> body = Map.of(
                "turnoId",           turnoId,
                "docenteId",         docenteId,
                "zonaId",            zonaId,
                "tipo",              "CONVIVENCIA",
                "severidad",         "S2_SEGUIMIENTO",
                "descripcion",       "Discusión entre estudiantes durante el recreo.",
                "observacionSocial", "Se recomendó seguimiento por orientación.",
                "registradoEn",      "2024-05-10T09:40:00",
                "requiereSeguimiento", true
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/incidentes", req, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        incidenteId = ((Number) response.getBody().get("id")).longValue();
        assertThat((Boolean) response.getBody().get("requiereSeguimiento")).isTrue();

        // notificarIncidente() → notifica a coord+admin
        assertThat(notificacionRepository.count() - notifAntes)
                .as("Deben crearse notificaciones REPORTE_INCIDENTE")
                .isGreaterThanOrEqualTo(2);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Paso 9 — Cambiar turno PENDIENTE → EN_CURSO → notificaciones CONFIRMACION_CHECKIN
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(9)
    @DisplayName("Paso 9: Turno PENDIENTE→EN_CURSO → notificaciones CONFIRMACION_CHECKIN (docente+coord+admin)")
    void paso09_cambiarTurnoAEnCurso_generaNotificacionesCheckin() {
        long notifAntes = notificacionRepository.count();

        Map<String, Object> body = Map.of(
                "docenteId",  docenteId,
                "zonaId",     zonaId,
                "fecha",      LocalDate.now().plusDays(1).toString(),
                "horaInicio", "09:30:00",
                "horaFin",    "09:50:00",
                "franja",     "Recreo mañana primaria",
                "estado",     "EN_CURSO"
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/turnos/" + turnoId, HttpMethod.PUT, req, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("estado")).isEqualTo("EN_CURSO");

        // notificarCheckIn() crea CONFIRMACION_CHECKIN para docente + todos coord + todos admin
        long notifNuevas = notificacionRepository.count() - notifAntes;
        assertThat(notifNuevas)
                .as("Deben crearse al menos 3 CONFIRMACION_CHECKIN (docente + coord + admin)")
                .isGreaterThanOrEqualTo(3);

        assertThat(notificacionRepository.findAll()).anyMatch(n ->
                n.getDestinatario().getId().equals(docenteId)
                && n.getTipo() == TipoNotificacion.CONFIRMACION_CHECKIN);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Paso 10 — Solicitar reasignación → notificaciones PROPUESTA_REEMPLAZO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(10)
    @DisplayName("Paso 10: Solicitar reasignación → notificaciones PROPUESTA_REEMPLAZO a coordinadores y administradores")
    void paso10_crearReasignacion_generaNotificacionesPropuesta() {
        long notifAntes = notificacionRepository.count();

        Map<String, Object> body = Map.of(
                "turnoId",              turnoId,
                "docenteSolicitanteId", docenteId,
                "docenteReemplazoId",   docenteReemplazoId,
                "motivo",               "Reunión académica urgente con rectoría.",
                "estado",               "PENDIENTE",
                "propuestaEn",          "2024-05-10T09:45:00",
                "segundosVentana",      300
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/reasignaciones", req, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        reasignacionId = ((Number) response.getBody().get("id")).longValue();
        assertThat(response.getBody().get("estado")).isEqualTo("PROPUESTA");

        // notificarReasignacion() → notifica a coord+admin
        assertThat(notificacionRepository.count() - notifAntes)
                .as("Deben crearse notificaciones PROPUESTA_REEMPLAZO")
                .isGreaterThanOrEqualTo(2);

        assertThat(notificacionRepository.findAll()).anyMatch(n ->
                n.getDestinatario().getId().equals(coordId)
                && n.getTipo() == TipoNotificacion.PROPUESTA_REEMPLAZO);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Paso 11 — Responder reasignación con ACEPTADA → notificación al solicitante
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(11)
    @DisplayName("Paso 11: Reasignación PENDIENTE→ACEPTADA → notificación PROPUESTA_REEMPLAZO al docente solicitante")
    void paso11_aceptarReasignacion_notificaDocenteSolicitante() {
        long notifAntes = notificacionRepository.count();

        Map<String, Object> body = Map.of(
                "turnoId",              turnoId,
                "docenteSolicitanteId", docenteId,
                "docenteReemplazoId",   docenteReemplazoId,
                "motivo",               "Reunión académica urgente con rectoría.",
                "estado",               "ACEPTADA",    // PENDIENTE → ACEPTADA dispara notificarRespuestaReasignacion
                "propuestaEn",          "2024-05-10T09:45:00",
                "respondidaEn",         "2024-05-10T09:50:00",
                "segundosVentana",      300
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/reasignaciones/" + reasignacionId, HttpMethod.PUT, req, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("estado")).isEqualTo("ACEPTADA");

        // notificarRespuestaReasignacion() → notifica al docente solicitante
        long notifNuevas = notificacionRepository.count() - notifAntes;
        assertThat(notifNuevas)
                .as("Debe crearse la notificación de respuesta al docente solicitante")
                .isGreaterThanOrEqualTo(1);

        assertThat(notificacionRepository.findAll()).anyMatch(n ->
                n.getDestinatario().getId().equals(docenteId)
                && n.getTipo() == TipoNotificacion.PROPUESTA_REEMPLAZO);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Paso 12 — Cerrar turno EN_CURSO → CERRADO → notificaciones CIERRE_TURNO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(12)
    @DisplayName("Paso 12: Turno EN_CURSO→CERRADO → notificaciones CIERRE_TURNO (docente+coord+admin)")
    void paso12_cerrarTurno_generaNotificacionesCierre() {
        long notifAntes = notificacionRepository.count();

        Map<String, Object> body = Map.of(
                "docenteId",  docenteId,
                "zonaId",     zonaId,
                "fecha",      LocalDate.now().plusDays(1).toString(),
                "horaInicio", "09:30:00",
                "horaFin",    "09:50:00",
                "franja",     "Recreo mañana primaria",
                "estado",     "CERRADO",
                "cerradoEn",  "2024-05-10T09:50:00"
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/turnos/" + turnoId, HttpMethod.PUT, req, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("estado")).isEqualTo("CERRADO");

        assertThat(notificacionRepository.count() - notifAntes)
                .as("Deben crearse al menos 3 CIERRE_TURNO (docente + coord + admin)")
                .isGreaterThanOrEqualTo(3);

        List<Notificacion> todas = notificacionRepository.findAll();
        assertThat(todas).anyMatch(n ->
                n.getDestinatario().getId().equals(docenteId)
                && n.getTipo() == TipoNotificacion.CIERRE_TURNO);
        assertThat(todas).anyMatch(n ->
                n.getDestinatario().getId().equals(coordId)
                && n.getTipo() == TipoNotificacion.CIERRE_TURNO);
        assertThat(todas).anyMatch(n ->
                n.getDestinatario().getId().equals(adminId)
                && n.getTipo() == TipoNotificacion.CIERRE_TURNO);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Paso 13 — Verificación final: el admin consulta su bandeja completa
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(13)
    @DisplayName("Paso 13: Verificación final — el administrador tiene todas las notificaciones esperadas del flujo")
    void paso13_verificacionFinal_adminTieneTodasLasNotificaciones() {
        HttpEntity<Void> req = new HttpEntity<>(bearerHeaders(tokenAdmin));
        ResponseEntity<List> response = restTemplate.exchange(
                "/api/notificaciones?userId=" + adminId,
                HttpMethod.GET, req, List.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<?> notificaciones = response.getBody();
        assertThat(notificaciones).isNotNull().isNotEmpty();

        // El admin debe tener al menos:
        //   REPORTE_INCIDENTE (paso 8)
        //   CONFIRMACION_CHECKIN (paso 9)
        //   PROPUESTA_REEMPLAZO (paso 10)
        //   CIERRE_TURNO (paso 12)
        //   REGISTRO_RECORRIDO (paso 5)
        Set<String> tiposEsperados = Set.of(
                "REPORTE_INCIDENTE", "CONFIRMACION_CHECKIN",
                "PROPUESTA_REEMPLAZO", "CIERRE_TURNO", "REGISTRO_RECORRIDO"
        );

        List<Notificacion> notificacionesAdmin = notificacionRepository.findAll()
                .stream()
                .filter(n -> n.getDestinatario() != null
                        && n.getDestinatario().getId().equals(adminId))
                .toList();

        Set<String> tiposRecibidos = new HashSet<>();
        for (Notificacion n : notificacionesAdmin) {
            tiposRecibidos.add(n.getTipo().name());
        }

        assertThat(tiposRecibidos)
                .as("El administrador debe haber recibido todos los tipos de notificación del flujo")
                .containsAll(tiposEsperados);

        // Verificar que el conteo de entidades en BD refleja el flujo completo
        assertThat(turnoRepository.existsById(turnoId)).isTrue();
        assertThat(recorridoRepository.existsById(recorridoId)).isTrue();
        assertThat(checkpointRecorridoRepository.existsById(checkpointId)).isTrue();
        assertThat(checkInRepository.existsById(checkinId)).isTrue();
        assertThat(incidenteRepository.existsById(incidenteId)).isTrue();
        assertThat(reasignacionRepository.existsById(reasignacionId)).isTrue();
    }
}
