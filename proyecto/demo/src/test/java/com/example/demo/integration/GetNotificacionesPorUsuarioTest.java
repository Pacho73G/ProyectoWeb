package com.example.demo.integration;

import com.example.demo.BaseIntegrationTest;
import com.example.demo.model.*;
import org.junit.jupiter.api.*;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba de integración — método GET.
 *
 * <p>Endpoint bajo prueba: {@code GET /api/notificaciones?userId={id}}</p>
 *
 * <p>Complejidad: al invocarse, el servicio ejecuta internamente
 * {@code registrarAlertasAusenciaPendientes()}, que:
 * <ol>
 *   <li>Escanea todos los turnos PENDIENTE cuya fecha+hora ya venció.</li>
 *   <li>Cambia su estado a SIN_COBERTURA.</li>
 *   <li>Genera notificaciones ALERTA_AUSENCIA para todos los COORDINADOR y ADMINISTRADOR.</li>
 * </ol>
 * Adicionalmente se prueba que el filtro por usuario funciona correctamente.</p>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GetNotificacionesPorUsuarioTest extends BaseIntegrationTest {

    private Administrador admin;
    private Coordinador   coord;
    private Docente       docente;
    private String        tokenAdmin;

    @BeforeEach
    void setUp() {
        // Creamos los actores del test con sufijo único para no violar la restricción UNIQUE de email
        String ts = String.valueOf(System.nanoTime());
        admin   = crearAdmin("gn-" + ts);
        coord   = crearCoordinador("gn-" + ts);
        docente = crearDocente("gn-" + ts);
        tokenAdmin = login(admin.getEmail(), "pwd-gn-" + ts);
    }

    @AfterEach
    void tearDown() {
        limpiarTodo();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Cuando el usuario existe pero no tiene ninguna notificación
     * (y tampoco hay turnos vencidos), la respuesta debe ser 200 con lista vacía.
     */
    @Test
    @Order(1)
    void get_debeRetornar200ConListaVacia_cuandoUsuarioSinNotificacionesNiTurnosVencidos() {
        HttpEntity<Void> req = new HttpEntity<>(bearerHeaders(tokenAdmin));

        ResponseEntity<List> response = restTemplate.exchange(
                "/api/notificaciones?userId=" + admin.getId(),
                HttpMethod.GET, req, List.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEmpty();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Escenario complejo: existe un turno PENDIENTE con fecha anterior a hoy.
     * Al consultar notificaciones, el sistema debe:
     *   - Marcar automáticamente ese turno como SIN_COBERTURA.
     *   - Generar y persistir notificaciones ALERTA_AUSENCIA para COORD y ADMIN.
     *   - Devolver esas notificaciones en la respuesta del coordinador consultado.
     */
    @Test
    @Order(2)
    void get_debeActivarAlertaAusenciaYCambiarEstadoTurno_cuandoTurnoPasadoPendiente() {
        // Preparar zona y turno vencido (fecha ayer, estado PENDIENTE)
        String ts = String.valueOf(System.nanoTime());
        Zona zona = crearZona("Zona-GET-" + ts);
        Turno turnoPasado = crearTurno(docente, zona, EstadoTurno.PENDIENTE, LocalDate.now().minusDays(1));

        // ── WHEN: se consultan las notificaciones del coordinador ─────────────
        HttpEntity<Void> req = new HttpEntity<>(bearerHeaders(tokenAdmin));
        ResponseEntity<List> response = restTemplate.exchange(
                "/api/notificaciones?userId=" + coord.getId(),
                HttpMethod.GET, req, List.class
        );

        // ── THEN ──────────────────────────────────────────────────────────────

        // 1. La respuesta HTTP es 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        // 2. El turno ahora debe estar SIN_COBERTURA en la base
        Turno turnoActualizado = turnoRepository.findById(turnoPasado.getId()).orElseThrow();
        assertThat(turnoActualizado.getEstado())
                .as("El turno vencido debe cambiar a SIN_COBERTURA")
                .isEqualTo(EstadoTurno.SIN_COBERTURA);

        // 3. Deben existir notificaciones ALERTA_AUSENCIA persistidas para el coordinador
        boolean tieneAlertaCoord = notificacionRepository.findAll()
                .stream()
                .anyMatch(n ->
                        n.getDestinatario() != null
                        && n.getDestinatario().getId().equals(coord.getId())
                        && n.getTipo() == TipoNotificacion.ALERTA_AUSENCIA);
        assertThat(tieneAlertaCoord)
                .as("El coordinador debe tener al menos una ALERTA_AUSENCIA")
                .isTrue();

        // 4. El cuerpo de la respuesta contiene al menos una notificación
        assertThat(response.getBody()).isNotEmpty();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * El endpoint sin userId requerido devuelve el listado global de notificaciones.
     * No debe mezclar notificaciones de distintos usuarios cuando se filtra por userId.
     */
    @Test
    @Order(3)
    void get_debeFiltrarPorUsuario_cuandoSePasaUserId() {
        // Crear una notificación directamente para el admin (no para el coordinador)
        String ts = String.valueOf(System.nanoTime());
        Zona zona = crearZona("Zona-FIL-" + ts);
        Turno turno = crearTurno(docente, zona, EstadoTurno.PENDIENTE, LocalDate.now().plusDays(2));

        Notificacion notifAdmin = new Notificacion();
        notifAdmin.setDestinatario(admin);
        notifAdmin.setTurno(turno);
        notifAdmin.setTipo(TipoNotificacion.ASIGNACION_TURNO);
        notifAdmin.setTitulo("Test");
        notifAdmin.setMensaje("Solo para admin");
        notifAdmin.setEnviadaEn(java.time.LocalDateTime.now());
        notifAdmin.setLeida(false);
        notifAdmin.setMinutosAnticipacion(0);
        notificacionRepository.save(notifAdmin);

        // Consultar notificaciones del coordinador (no tiene ninguna propia)
        HttpEntity<Void> req = new HttpEntity<>(bearerHeaders(tokenAdmin));
        ResponseEntity<List> responseCoord = restTemplate.exchange(
                "/api/notificaciones?userId=" + coord.getId(),
                HttpMethod.GET, req, List.class
        );

        assertThat(responseCoord.getStatusCode()).isEqualTo(HttpStatus.OK);
        // El coordinador no tiene notificaciones propias
        assertThat(responseCoord.getBody()).isNotNull().isEmpty();

        // Consultar las del admin: debe ver la suya
        ResponseEntity<List> responseAdmin = restTemplate.exchange(
                "/api/notificaciones?userId=" + admin.getId(),
                HttpMethod.GET, req, List.class
        );
        assertThat(responseAdmin.getBody()).isNotNull().isNotEmpty();
    }
}
