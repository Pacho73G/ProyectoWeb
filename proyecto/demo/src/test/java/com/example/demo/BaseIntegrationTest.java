package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * Clase base compartida por todas las pruebas de integracion y de sistema.
 *
 * Levanta el contexto completo de Spring Boot contra H2 en memoria (perfil "test").
 * No usa mocks: todos los beans (controllers, services, repositories) son reales.
 *
 * Convencion de limpieza:
 *   - Cada subclase llama limpiarTodo() en su @AfterEach para aislar los datos entre tests.
 *   - La prueba de sistema CicloCompletoTurnoST gestiona su propio ciclo de vida
 *     con @BeforeEach y @AfterAll debido a que sus pasos son secuenciales.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    // ── Repositorios para setup y teardown directo en BD ─────────────────────

    @Autowired protected ReconocimientoRepository      reconocimientoRepository;
    @Autowired protected MetricaDocenteRepository      metricaDocenteRepository;
    @Autowired protected MapaCalorRepository           mapaCalorRepository;
    @Autowired protected NotificacionRepository        notificacionRepository;
    @Autowired protected CheckpointRecorridoRepository checkpointRecorridoRepository;
    @Autowired protected RecorridoRepository           recorridoRepository;
    @Autowired protected RegistroLimpiezaRepository    registroLimpiezaRepository;
    @Autowired protected ReasignacionRepository        reasignacionRepository;
    @Autowired protected IncidenteRepository           incidenteRepository;
    @Autowired protected CheckInRepository             checkInRepository;
    @Autowired protected TurnoRepository               turnoRepository;
    @Autowired protected ZonaRepository                zonaRepository;
    @Autowired protected ConfiguracionSistemaRepository configuracionSistemaRepository;
    @Autowired protected DocenteRepository             docenteRepository;
    @Autowired protected CoordinadorRepository         coordinadorRepository;
    @Autowired protected AdministradorRepository       administradorRepository;
    @Autowired protected UsuarioRepository             usuarioRepository;

    // ── Limpieza de datos en orden inverso a las FK ───────────────────────────

    /**
     * Elimina todos los registros de la BD en orden inverso a sus dependencias.
     * Debe llamarse en @AfterEach para garantizar aislamiento entre tests.
     */
    protected void limpiarTodo() {
        reconocimientoRepository.deleteAllInBatch();
        metricaDocenteRepository.deleteAllInBatch();
        mapaCalorRepository.deleteAllInBatch();
        notificacionRepository.deleteAllInBatch();
        checkpointRecorridoRepository.deleteAllInBatch();
        recorridoRepository.deleteAllInBatch();
        registroLimpiezaRepository.deleteAllInBatch();
        reasignacionRepository.deleteAllInBatch();
        incidenteRepository.deleteAllInBatch();
        checkInRepository.deleteAllInBatch();
        turnoRepository.deleteAllInBatch();
        zonaRepository.deleteAllInBatch();
        configuracionSistemaRepository.deleteAllInBatch();
        // Jerarquia JOINED: primero tablas hijas, luego la tabla base "usuarios"
        docenteRepository.deleteAllInBatch();
        coordinadorRepository.deleteAllInBatch();
        administradorRepository.deleteAllInBatch();
        usuarioRepository.deleteAllInBatch();
    }

    // ── Helpers de autenticacion JWT ─────────────────────────────────────────

    /**
     * Hace POST /api/auth/login y devuelve el token JWT.
     * La contrasena puede estar en texto plano porque LegacyCompatiblePasswordEncoder
     * acepta hashes sin prefijo BCrypt directamente como contrasenas validas.
     */
    @SuppressWarnings("unchecked")
    protected String login(String email, String password) {
        Map<String, String> body = Map.of("email", email, "password", password);
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/auth/login", body, Map.class);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException(
                "Login fallido para " + email + " — HTTP " + response.getStatusCode());
        }
        return (String) response.getBody().get("token");
    }

    /** Devuelve HttpHeaders con Authorization: Bearer {token} y Content-Type: application/json. */
    protected HttpHeaders bearerHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // ── Fabricas de entidades de prueba (insercion directa en BD) ────────────
    //
    // Se usan repositorios directamente (no la API) para el setup porque:
    //   1. Es mas rapido (sin HTTP ni JWT).
    //   2. La contrasena queda en texto plano; LegacyCompatiblePasswordEncoder la valida en login.
    //   3. El sufijo garantiza unicidad del email entre tests que se ejecuten en paralelo.

    protected Administrador crearAdmin(String sufijo) {
        Administrador a = new Administrador();
        a.setNombre("Admin " + sufijo);
        a.setEmail("admin." + sufijo + "@test.edu");
        a.setPasswordHash("pwd-" + sufijo);
        a.setRol(RolUsuario.ADMINISTRADOR);
        a.setCargo("Director Test");
        return administradorRepository.save(a);
    }

    protected Coordinador crearCoordinador(String sufijo) {
        Coordinador c = new Coordinador();
        c.setNombre("Coord " + sufijo);
        c.setEmail("coord." + sufijo + "@test.edu");
        c.setPasswordHash("pwd-" + sufijo);
        c.setRol(RolUsuario.COORDINADOR);
        c.setNivel("Secundaria");
        return coordinadorRepository.save(c);
    }

    protected Docente crearDocente(String sufijo) {
        Docente d = new Docente();
        d.setNombre("Docente " + sufijo);
        d.setEmail("docente." + sufijo + "@test.edu");
        d.setPasswordHash("pwd-" + sufijo);
        d.setRol(RolUsuario.DOCENTE);
        d.setMaterias("Matematicas");
        return docenteRepository.save(d);
    }

    protected Zona crearZona(String nombre) {
        Zona z = new Zona();
        z.setNombre(nombre);
        z.setDescripcion("Zona de prueba");
        z.setUbicacion("Bloque A");
        z.setCapacidadMaxima(100);
        z.setActiva(true);
        return zonaRepository.save(z);
    }

    protected Turno crearTurno(Docente docente, Zona zona, EstadoTurno estado, LocalDate fecha) {
        Turno t = new Turno();
        t.setDocente(docente);
        t.setZona(zona);
        t.setFecha(fecha);
        t.setHoraInicio(LocalTime.of(9, 0));
        t.setHoraFin(LocalTime.of(10, 0));
        t.setFranja("Recreo manana test");
        t.setEstado(estado);
        return turnoRepository.save(t);
    }
}
