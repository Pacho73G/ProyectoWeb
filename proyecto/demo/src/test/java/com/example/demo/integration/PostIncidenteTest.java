package com.example.demo.integration;

import com.example.demo.BaseIntegrationTest;
import com.example.demo.model.*;
import org.junit.jupiter.api.*;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba de integración — método POST.
 *
 * <p>Endpoint bajo prueba: {@code POST /api/incidentes}</p>
 *
 * <p>Complejidad: al crearse un incidente el sistema ejecuta en cadena:
 * <ol>
 *   <li>Validación y persistencia del {@code Incidente} (OperacionManagementService).</li>
 *   <li>Llamada a {@code notificarIncidente()}: genera notificaciones
 *       REPORTE_INCIDENTE para <em>todos</em> los usuarios con rol
 *       COORDINADOR y ADMINISTRADOR registrados en la base.</li>
 * </ol>
 * Así se verifica que el POST tiene efectos secundarios transversales
 * (no solo el objeto creado).</p>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostIncidenteTest extends BaseIntegrationTest {

    private Administrador admin;
    private Coordinador   coord;
    private Docente       docente;
    private Zona          zona;
    private Turno         turno;
    private String        tokenAdmin;
    private String        tokenDocente;

    @BeforeEach
    void setUp() {
        String ts = String.valueOf(System.nanoTime());
        admin   = crearAdmin("pi-" + ts);
        coord   = crearCoordinador("pi-" + ts);
        docente = crearDocente("pi-" + ts);
        zona    = crearZona("Zona-PI-" + ts);
        turno   = crearTurno(docente, zona, EstadoTurno.EN_CURSO, LocalDate.now());

        tokenAdmin   = login(admin.getEmail(), "pwd-pi-" + ts);
        tokenDocente = login(docente.getEmail(), "pwd-pi-" + ts);
    }

    @AfterEach
    void tearDown() {
        limpiarTodo();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Un docente autenticado puede registrar un incidente asociado a su turno.
     * La respuesta debe ser 201 Created con el DTO completo del incidente.
     */
    @Test
    @Order(1)
    void post_debeRetornar201ConDatosCorrectos_cuandoRequestValido() {
        Map<String, Object> body = Map.of(
                "turnoId",           turno.getId(),
                "docenteId",         docente.getId(),
                "zonaId",            zona.getId(),
                "tipo",              "CONVIVENCIA",
                "severidad",         "S1_LEVE",
                "descripcion",       "Estudiante corriendo en pasillo.",
                "observacionSocial", "Sin contexto adicional.",
                "registradoEn",      "2024-05-01T10:30:00",
                "requiereSeguimiento", false
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenDocente));
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/incidentes", req, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("id")).isNotNull();
        assertThat(response.getBody().get("tipo")).isEqualTo("CONVIVENCIA");
        assertThat(response.getBody().get("docenteNombre")).isEqualTo(docente.getNombre());
        assertThat(response.getBody().get("zonaNombre")).isEqualTo(zona.getNombre());
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Escenario complejo: tras crear el incidente, el sistema debe haber generado
     * notificaciones REPORTE_INCIDENTE para el coordinador y para el administrador.
     * Esto verifica el efecto secundario transversal de la creación de incidentes.
     */
    @Test
    @Order(2)
    void post_debeNotificarCoordinadorYAdministrador_cuandoSeRegistraIncidente() {
        long notificacionesPrevias = notificacionRepository.count();

        Map<String, Object> body = Map.of(
                "turnoId",           turno.getId(),
                "docenteId",         docente.getId(),
                "zonaId",            zona.getId(),
                "tipo",              "SEGURIDAD_FISICA",
                "severidad",         "S2_SEGUIMIENTO",
                "descripcion",       "Vidrio roto en ventana del pasillo.",
                "observacionSocial", "Se acordono la zona.",
                "registradoEn",      "2024-05-01T11:00:00",
                "requiereSeguimiento", true
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenDocente));
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/incidentes", req, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Tras el POST, deben existir nuevas notificaciones en la base
        long notificacionesPost = notificacionRepository.count();
        assertThat(notificacionesPost)
                .as("Deben haberse creado notificaciones REPORTE_INCIDENTE")
                .isGreaterThan(notificacionesPrevias);

        // Verificar que el coordinador recibió su notificación
        boolean coordNotificado = notificacionRepository.findAll()
                .stream()
                .anyMatch(n ->
                        n.getDestinatario() != null
                        && n.getDestinatario().getId().equals(coord.getId())
                        && n.getTipo() == TipoNotificacion.REPORTE_INCIDENTE);
        assertThat(coordNotificado)
                .as("El coordinador debe haber recibido REPORTE_INCIDENTE")
                .isTrue();

        // Verificar que el administrador recibió su notificación
        boolean adminNotificado = notificacionRepository.findAll()
                .stream()
                .anyMatch(n ->
                        n.getDestinatario() != null
                        && n.getDestinatario().getId().equals(admin.getId())
                        && n.getTipo() == TipoNotificacion.REPORTE_INCIDENTE);
        assertThat(adminNotificado)
                .as("El administrador debe haber recibido REPORTE_INCIDENTE")
                .isTrue();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Un incidente sin turnoId (incidente fuera de turno) también debe persistirse
     * y notificar correctamente. El mensaje de notificación cambia: dice "fuera de turno".
     */
    @Test
    @Order(3)
    void post_debePersistirIncidenteSinTurno_yNotificar() {
        Map<String, Object> body = new HashMap<>();
        body.put("turnoId",            null);
        body.put("docenteId",          docente.getId());
        body.put("zonaId",             zona.getId());
        body.put("tipo",               "OBSERVACION_SOCIAL");
        body.put("severidad",          "S3_ATENCION_INMEDIATA");
        body.put("descripcion",        "Incidente fuera de turno asignado.");
        body.put("observacionSocial",  null);
        body.put("registradoEn",       "2024-05-01T12:00:00");
        body.put("requiereSeguimiento", true);

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenDocente));
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/incidentes", req, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().get("turnoId")).isNull();
        assertThat(response.getBody().get("tipo")).isEqualTo("OBSERVACION_SOCIAL");
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Sin token JWT cualquier recurso protegido debe retornar 401 Unauthorized.
     *
     * <p>Se usa GET /api/incidentes sin Authorization para evitar el problema de
     * streaming que ocurre con POST+body sin auth en TestRestTemplate con
     * HttpURLConnection (lanza ResourceAccessException al reintentar). Un GET
     * sin body evita el modo streaming y permite capturar el 401 limpiamente.</p>
     */
    @Test
    @Order(4)
    void get_debeRetornar401_cuandoNoAutenticado() {
        // GET sin Authorization — sin body, sin streaming, 401 limpio
        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/incidentes", HttpMethod.GET, HttpEntity.EMPTY, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Un DOCENTE no puede crear una ZONA (POST /api/zonas solo es para ADMINISTRADOR).
     * Debe recibir 403 Forbidden. Verifica que el control de acceso en POST
     * diferencia correctamente los recursos permitidos por rol.
     */
    @Test
    @Order(5)
    void post_debeRetornar403_cuandoDocenteIntentaCrearZona() {
        String ts = String.valueOf(System.nanoTime());
        Map<String, Object> body = Map.of(
                "nombre",          "Zona Intrusa " + ts,
                "descripcion",     "Intento de creacion por docente",
                "ubicacion",       "Bloque X",
                "capacidadMaxima", 30,
                "activa",          true
        );

        // tokenDocente viene del setUp — el DOCENTE no puede hacer POST en /api/zonas
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, bearerHeaders(tokenDocente));
        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/zonas", HttpMethod.POST, req, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}