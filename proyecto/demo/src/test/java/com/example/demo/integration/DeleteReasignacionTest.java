package com.example.demo.integration;

import com.example.demo.BaseIntegrationTest;
import com.example.demo.model.*;
import org.junit.jupiter.api.*;
import org.springframework.http.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba de integración — método DELETE.
 *
 * <p>Endpoint bajo prueba: {@code DELETE /api/reasignaciones/{id}}</p>
 *
 * <p>Complejidad: la reasignación es el recurso con más restricciones del sistema:
 * <ul>
 *   <li>Existe única por turno (restricción UNIQUE en BD: {@code turno_id}).</li>
 *   <li>Solo usuarios con rol ADMINISTRADOR o COORDINADOR pueden eliminarla.</li>
 *   <li>El endpoint valida existencia antes de eliminar (lanza 404 si no existe).</li>
 *   <li>Al eliminar una reasignación, las notificaciones asociadas al turno
 *       se eliminan en cascada (FK con ON DELETE CASCADE).</li>
 * </ul>
 * </p>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeleteReasignacionIT extends BaseIntegrationTest {

    private Administrador admin;
    private Coordinador   coord;
    private Docente       docenteSolicitante;
    private Docente       docenteReemplazo;
    private Zona          zona;
    private String        tokenAdmin;
    private String        tokenCoord;

    @BeforeEach
    void setUp() {
        String ts = String.valueOf(System.nanoTime());
        admin             = crearAdmin("dr-" + ts);
        coord             = crearCoordinador("dr-" + ts);
        docenteSolicitante = crearDocente("drA-" + ts);
        docenteReemplazo   = crearDocente("drB-" + ts);
        zona              = crearZona("Zona-DR-" + ts);

        tokenAdmin = login(admin.getEmail(), "pwd-dr-" + ts);
        tokenCoord = login(coord.getEmail(), "pwd-dr-" + ts);
    }

    @AfterEach
    void tearDown() {
        limpiarTodo();
    }

    /** Crea y persiste una Reasignacion de prueba vinculada a un turno nuevo. */
    private Reasignacion crearReasignacion(Turno turno) {
        Reasignacion r = new Reasignacion();
        r.setTurno(turno);
        r.setDocenteSolicitante(docenteSolicitante);
        r.setDocenteReemplazo(docenteReemplazo);
        r.setMotivo("Reunión académica urgente.");
        r.setEstado(EstadoReasignacion.PROPUESTA);
        r.setPropuestaEn(LocalDateTime.now().minusMinutes(5));
        r.setSegundosVentana(300);
        return reasignacionRepository.save(r);
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Un ADMINISTRADOR puede eliminar una reasignación existente.
     * La respuesta debe ser 204 No Content y la entidad debe desaparecer de la BD.
     */
    @Test
    @Order(1)
    void delete_debeRetornar204YEliminarEntidad_cuandoAdminElimina() {
        Turno turno = crearTurno(docenteSolicitante, zona, EstadoTurno.PENDIENTE,
                LocalDate.now().plusDays(1));
        Reasignacion reasignacion = crearReasignacion(turno);
        Long idReasignacion = reasignacion.getId();

        HttpEntity<Void> req = new HttpEntity<>(bearerHeaders(tokenAdmin));
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/reasignaciones/" + idReasignacion, HttpMethod.DELETE, req, Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(reasignacionRepository.existsById(idReasignacion))
                .as("La reasignación debe haberse eliminado de la BD")
                .isFalse();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Un COORDINADOR también tiene permiso para eliminar una reasignación.
     */
    @Test
    @Order(2)
    void delete_debeRetornar204_cuandoCoordinadorElimina() {
        Turno turno = crearTurno(docenteSolicitante, zona, EstadoTurno.PENDIENTE,
                LocalDate.now().plusDays(2));
        Reasignacion reasignacion = crearReasignacion(turno);

        HttpEntity<Void> req = new HttpEntity<>(bearerHeaders(tokenCoord));
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/reasignaciones/" + reasignacion.getId(), HttpMethod.DELETE, req, Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(reasignacionRepository.existsById(reasignacion.getId())).isFalse();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Intentar eliminar una reasignación con ID inexistente debe retornar 404.
     */
    @Test
    @Order(3)
    void delete_debeRetornar404_cuandoReasignacionNoExiste() {
        HttpEntity<Void> req = new HttpEntity<>(bearerHeaders(tokenAdmin));
        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/reasignaciones/999999", HttpMethod.DELETE, req, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Un DOCENTE no tiene permiso para eliminar reasignaciones (403 Forbidden).
     */
    @Test
    @Order(4)
    void delete_debeRetornar403_cuandoDocenteIntentaEliminar() {
        String ts = String.valueOf(System.nanoTime());
        Docente docenteExtra = crearDocente("drX-" + ts);
        String tokenDocente = login(docenteExtra.getEmail(), "pwd-drX-" + ts);

        Turno turno = crearTurno(docenteSolicitante, zona, EstadoTurno.PENDIENTE,
                LocalDate.now().plusDays(3));
        Reasignacion reasignacion = crearReasignacion(turno);

        HttpEntity<Void> req = new HttpEntity<>(bearerHeaders(tokenDocente));
        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/reasignaciones/" + reasignacion.getId(), HttpMethod.DELETE, req, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        // La reasignación NO debe haberse eliminado
        assertThat(reasignacionRepository.existsById(reasignacion.getId())).isTrue();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Sin token JWT el DELETE debe ser rechazado con 401 Unauthorized.
     */
    @Test
    @Order(5)
    void delete_debeRetornar401_cuandoNoAutenticado() {
        Turno turno = crearTurno(docenteSolicitante, zona, EstadoTurno.PENDIENTE,
                LocalDate.now().plusDays(4));
        Reasignacion reasignacion = crearReasignacion(turno);

        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/reasignaciones/" + reasignacion.getId(), HttpMethod.DELETE,
                HttpEntity.EMPTY, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
