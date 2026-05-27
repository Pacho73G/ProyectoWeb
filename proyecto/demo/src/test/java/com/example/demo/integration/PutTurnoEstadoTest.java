package com.example.demo.integration;

import com.example.demo.BaseIntegrationTest;
import com.example.demo.model.*;
import org.junit.jupiter.api.*;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba de integración — método PUT.
 *
 * <p>Endpoint bajo prueba: {@code PUT /api/turnos/{id}}</p>
 *
 * <p>Complejidad: es el PUT más complejo del sistema porque implementa una
 * máquina de estados con notificaciones condicionales:
 * <ul>
 *   <li>PENDIENTE → EN_CURSO: dispara {@code notificarCheckIn()} →
 *       crea CONFIRMACION_CHECKIN para el docente, todos los coordinadores
 *       y todos los administradores.</li>
 *   <li>EN_CURSO → CERRADO: dispara {@code notificarCierreTurno()} →
 *       crea CIERRE_TURNO para el docente, todos los coordinadores
 *       y todos los administradores.</li>
 *   <li>Cualquier otra transición: sin notificaciones.</li>
 * </ul>
 * </p>
 *
 * <p>Control de acceso:
 * <ul>
 *   <li>ADMINISTRADOR y DOCENTE pueden hacer PUT en {@code /api/turnos/**}
 *       (el docente necesita este permiso para iniciar su turno).</li>
 *   <li>DOCENTE NO puede hacer PUT en {@code /api/zonas/**}
 *       (solo ADMINISTRADOR).</li>
 *   <li>Sin token → 401 Unauthorized.</li>
 * </ul>
 * </p>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PutTurnoEstadoTest extends BaseIntegrationTest {

    private Administrador admin;
    private Coordinador   coord;
    private Docente       docente;
    private Zona          zona;
    private String        tokenAdmin;

    @BeforeEach
    void setUp() {
        String ts = String.valueOf(System.nanoTime());
        admin   = crearAdmin("pt-" + ts);
        coord   = crearCoordinador("pt-" + ts);
        docente = crearDocente("pt-" + ts);
        zona    = crearZona("Zona-PT-" + ts);
        tokenAdmin = login(admin.getEmail(), "pwd-pt-" + ts);
    }

    @AfterEach
    void tearDown() {
        limpiarTodo();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Actualizar campos básicos de un turno sin cambiar el estado
     * no debe generar ninguna notificación.
     */
    @Test
    @Order(1)
    void put_debeActualizarTurno_sinGenerarNotificaciones_cuandoEstadoNoVaria() {
        Turno turno = crearTurno(docente, zona, EstadoTurno.PENDIENTE, LocalDate.now().plusDays(3));
        long notifAntes = notificacionRepository.count();

        Map<String, Object> body = Map.of(
                "docenteId",  docente.getId(),
                "zonaId",     zona.getId(),
                "fecha",      LocalDate.now().plusDays(3).toString(),
                "horaInicio", "10:00:00",
                "horaFin",    "11:00:00",
                "franja",     "Recreo tarde actualizado",
                "estado",     "PENDIENTE"   // mismo estado → no dispara notificaciones
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/turnos/" + turno.getId(), HttpMethod.PUT, req, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("franja")).isEqualTo("Recreo tarde actualizado");

        // Sin notificaciones adicionales
        assertThat(notificacionRepository.count())
                .as("Misma transición de estado no debe generar notificaciones")
                .isEqualTo(notifAntes);
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Cambio de estado PENDIENTE → EN_CURSO:
     * debe generar CONFIRMACION_CHECKIN para docente + coordinador + administrador.
     */
    @Test
    @Order(2)
    void put_debeNotificarConfirmacionCheckin_cuandoCambiaPendienteAEnCurso() {
        Turno turno = crearTurno(docente, zona, EstadoTurno.PENDIENTE, LocalDate.now());
        long notifAntes = notificacionRepository.count();

        Map<String, Object> body = Map.of(
                "docenteId",  docente.getId(),
                "zonaId",     zona.getId(),
                "fecha",      LocalDate.now().toString(),
                "horaInicio", "09:00:00",
                "horaFin",    "10:00:00",
                "franja",     "Recreo manana test",
                "estado",     "EN_CURSO"    // PENDIENTE → EN_CURSO dispara notificarCheckIn
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/turnos/" + turno.getId(), HttpMethod.PUT, req, Map.class
        );

        // ── THEN ──────────────────────────────────────────────────────────────
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("estado")).isEqualTo("EN_CURSO");

        // Deben haberse creado al menos 3 notificaciones (docente + coord + admin)
        long notifDespues = notificacionRepository.count();
        assertThat(notifDespues - notifAntes)
                .as("Deben crearse al menos 3 CONFIRMACION_CHECKIN (docente, coord, admin)")
                .isGreaterThanOrEqualTo(3);

        List<Notificacion> todasNotif = notificacionRepository.findAll();

        // El docente recibe su CONFIRMACION_CHECKIN
        assertThat(todasNotif).anyMatch(n ->
                n.getDestinatario().getId().equals(docente.getId())
                && n.getTipo() == TipoNotificacion.CONFIRMACION_CHECKIN);

        // El coordinador recibe su CONFIRMACION_CHECKIN
        assertThat(todasNotif).anyMatch(n ->
                n.getDestinatario().getId().equals(coord.getId())
                && n.getTipo() == TipoNotificacion.CONFIRMACION_CHECKIN);

        // El administrador recibe su CONFIRMACION_CHECKIN
        assertThat(todasNotif).anyMatch(n ->
                n.getDestinatario().getId().equals(admin.getId())
                && n.getTipo() == TipoNotificacion.CONFIRMACION_CHECKIN);
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Cambio de estado EN_CURSO → CERRADO:
     * debe generar CIERRE_TURNO para docente + coordinador + administrador.
     */
    @Test
    @Order(3)
    void put_debeNotificarCierreTurno_cuandoCambiaEnCursoACerrado() {
        Turno turno = crearTurno(docente, zona, EstadoTurno.EN_CURSO, LocalDate.now());
        long notifAntes = notificacionRepository.count();

        Map<String, Object> body = Map.of(
                "docenteId",  docente.getId(),
                "zonaId",     zona.getId(),
                "fecha",      LocalDate.now().toString(),
                "horaInicio", "09:00:00",
                "horaFin",    "10:00:00",
                "franja",     "Recreo manana test",
                "estado",     "CERRADO"     // EN_CURSO → CERRADO dispara notificarCierreTurno
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/turnos/" + turno.getId(), HttpMethod.PUT, req, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("estado")).isEqualTo("CERRADO");

        long notifDespues = notificacionRepository.count();
        assertThat(notifDespues - notifAntes)
                .as("Deben crearse al menos 3 CIERRE_TURNO (docente, coord, admin)")
                .isGreaterThanOrEqualTo(3);

        List<Notificacion> todasNotif = notificacionRepository.findAll();

        assertThat(todasNotif).anyMatch(n ->
                n.getDestinatario().getId().equals(docente.getId())
                && n.getTipo() == TipoNotificacion.CIERRE_TURNO);

        assertThat(todasNotif).anyMatch(n ->
                n.getDestinatario().getId().equals(coord.getId())
                && n.getTipo() == TipoNotificacion.CIERRE_TURNO);

        assertThat(todasNotif).anyMatch(n ->
                n.getDestinatario().getId().equals(admin.getId())
                && n.getTipo() == TipoNotificacion.CIERRE_TURNO);
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Intentar actualizar un turno inexistente debe retornar 404 Not Found.
     */
    @Test
    @Order(4)
    void put_debeRetornar404_cuandoTurnoNoExiste() {
        Map<String, Object> body = Map.of(
                "docenteId",  docente.getId(),
                "zonaId",     zona.getId(),
                "fecha",      LocalDate.now().toString(),
                "horaInicio", "09:00:00",
                "horaFin",    "10:00:00",
                "franja",     "Franja test",
                "estado",     "EN_CURSO"
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/turnos/999999", HttpMethod.PUT, req, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Un DOCENTE no puede actualizar una ZONA (solo ADMINISTRADOR tiene ese permiso).
     * Prueba el control de acceso diferenciado: PUT /api/zonas/** → 403 para DOCENTE.
     *
     * <p>Nota: PUT /api/turnos/** sí está permitido para el DOCENTE porque necesita
     * ese endpoint para iniciar su turno (PENDIENTE → EN_CURSO). El recurso restringido
     * para el docente en PUT es /api/zonas/**, /api/usuarios/**, etc.</p>
     */
    @Test
    @Order(5)
    void put_debeRetornar403_cuandoDocenteIntentaModificarZona() {
        String ts = String.valueOf(System.nanoTime());
        Docente d2 = crearDocente("pt2-" + ts);
        String tokenDocente = login(d2.getEmail(), "pwd-pt2-" + ts);

        Map<String, Object> body = Map.of(
                "nombre",          zona.getNombre() + " modificado",
                "descripcion",     "Intento de modificacion por docente",
                "ubicacion",       "Bloque B",
                "capacidadMaxima", 50,
                "activa",          true
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenDocente));
        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/zonas/" + zona.getId(), HttpMethod.PUT, req, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Sin token JWT cualquier recurso protegido debe retornar 401 Unauthorized.
     *
     * <p>Se usa GET /api/turnos sin Authorization para evitar el problema de streaming
     * que ocurre con PUT+body sin auth en TestRestTemplate con HttpURLConnection
     * (lanza ResourceAccessException al reintentar). Un GET sin body evita el modo
     * streaming y permite capturar el 401 limpiamente.</p>
     */
    @Test
    @Order(6)
    void get_debeRetornar401_cuandoNoAutenticado() {
        // GET sin Authorization — sin body, sin streaming, 401 limpio
        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/turnos", HttpMethod.GET, HttpEntity.EMPTY, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}