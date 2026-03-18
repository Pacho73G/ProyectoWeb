package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.RelacionInvalidaException;
import com.example.demo.model.Administrador;
import com.example.demo.model.CheckIn;
import com.example.demo.model.CheckpointRecorrido;
import com.example.demo.model.ConfiguracionSistema;
import com.example.demo.model.Coordinador;
import com.example.demo.model.Docente;
import com.example.demo.model.Incidente;
import com.example.demo.model.MapaCalor;
import com.example.demo.model.MetricaDocente;
import com.example.demo.model.Notificacion;
import com.example.demo.model.Reasignacion;
import com.example.demo.model.Reconocimiento;
import com.example.demo.model.Recorrido;
import com.example.demo.model.RegistroLimpieza;
import com.example.demo.model.Turno;
import com.example.demo.model.Usuario;
import com.example.demo.model.Zona;
import com.example.demo.repository.AdministradorRepository;
import com.example.demo.repository.CheckInRepository;
import com.example.demo.repository.CheckpointRecorridoRepository;
import com.example.demo.repository.ConfiguracionSistemaRepository;
import com.example.demo.repository.CoordinadorRepository;
import com.example.demo.repository.DocenteRepository;
import com.example.demo.repository.IncidenteRepository;
import com.example.demo.repository.MapaCalorRepository;
import com.example.demo.repository.MetricaDocenteRepository;
import com.example.demo.repository.NotificacionRepository;
import com.example.demo.repository.ReasignacionRepository;
import com.example.demo.repository.ReconocimientoRepository;
import com.example.demo.repository.RecorridoRepository;
import com.example.demo.repository.RegistroLimpiezaRepository;
import com.example.demo.repository.TurnoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.ZonaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
/**
 * Servicio de consulta centralizado para listas, estadísticas y búsquedas por id.
 */
public class CatalogQueryService {

    private final UsuarioRepository usuarioRepository;
    private final DocenteRepository docenteRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final AdministradorRepository administradorRepository;
    private final ConfiguracionSistemaRepository configuracionSistemaRepository;
    private final ZonaRepository zonaRepository;
    private final TurnoRepository turnoRepository;
    private final CheckInRepository checkInRepository;
    private final IncidenteRepository incidenteRepository;
    private final ReasignacionRepository reasignacionRepository;
    private final RegistroLimpiezaRepository registroLimpiezaRepository;
    private final NotificacionRepository notificacionRepository;
    private final RecorridoRepository recorridoRepository;
    private final CheckpointRecorridoRepository checkpointRecorridoRepository;
    private final MapaCalorRepository mapaCalorRepository;
    private final MetricaDocenteRepository metricaDocenteRepository;
    private final ReconocimientoRepository reconocimientoRepository;

    public List<Usuario> usuarios() { return usuarioRepository.findAll(); }
    public Usuario usuario(Long id) { return usuarioRepository.findById(id).orElseThrow(() -> notFound("El usuario solicitado no existe.")); }
    public List<Docente> docentes() { return docenteRepository.findAll(); }
    public Docente docente(Long id) { return docenteRepository.findById(id).orElseThrow(() -> invalidRelation("El docente seleccionado no es válido.")); }
    public List<Coordinador> coordinadores() { return coordinadorRepository.findAll(); }
    public Coordinador coordinador(Long id) { return coordinadorRepository.findById(id).orElseThrow(() -> notFound("El coordinador solicitado no existe.")); }
    public List<Administrador> administradores() { return administradorRepository.findAll(); }
    public Administrador administrador(Long id) { return administradorRepository.findById(id).orElseThrow(() -> invalidRelation("El administrador seleccionado no es válido.")); }
    public List<ConfiguracionSistema> configuraciones() { return configuracionSistemaRepository.findAll(); }
    public ConfiguracionSistema configuracion(Long id) { return configuracionSistemaRepository.findById(id).orElseThrow(() -> notFound("La configuración solicitada no existe.")); }
    public List<Zona> zonas() { return zonaRepository.findAll(); }
    public Zona zona(Long id) { return zonaRepository.findById(id).orElseThrow(() -> invalidRelation("La zona seleccionada no es válida.")); }
    public List<Turno> turnos() { return turnoRepository.findAll(); }
    public Turno turno(Long id) { return turnoRepository.findById(id).orElseThrow(() -> invalidRelation("El turno seleccionado no es válido.")); }
    public List<CheckIn> checkIns() { return checkInRepository.findAll(); }
    public CheckIn checkIn(Long id) { return checkInRepository.findById(id).orElseThrow(() -> notFound("El check-in solicitado no existe.")); }
    public List<Incidente> incidentes() { return incidenteRepository.findAll(); }
    public Incidente incidente(Long id) { return incidenteRepository.findById(id).orElseThrow(() -> notFound("El incidente solicitado no existe.")); }
    public List<Reasignacion> reasignaciones() { return reasignacionRepository.findAll(); }
    public Reasignacion reasignacion(Long id) { return reasignacionRepository.findById(id).orElseThrow(() -> notFound("La reasignación solicitada no existe.")); }
    public List<RegistroLimpieza> limpiezas() { return registroLimpiezaRepository.findAll(); }
    public RegistroLimpieza limpieza(Long id) { return registroLimpiezaRepository.findById(id).orElseThrow(() -> notFound("El registro de limpieza solicitado no existe.")); }
    public RegistroLimpieza limpiezaPorTurno(Long turnoId) { return registroLimpiezaRepository.findByTurnoId(turnoId).orElse(null); }
    public List<Notificacion> notificaciones() { return notificacionRepository.findAll(); }
    public Notificacion notificacion(Long id) { return notificacionRepository.findById(id).orElseThrow(() -> notFound("La notificación solicitada no existe.")); }
    public List<Recorrido> recorridos() { return recorridoRepository.findAll(); }
    public Recorrido recorrido(Long id) { return recorridoRepository.findById(id).orElseThrow(() -> notFound("El recorrido solicitado no existe.")); }
    public List<CheckpointRecorrido> checkpoints() { return checkpointRecorridoRepository.findAll(); }
    public CheckpointRecorrido checkpoint(Long id) { return checkpointRecorridoRepository.findById(id).orElseThrow(() -> notFound("El checkpoint solicitado no existe.")); }
    public List<MapaCalor> mapasCalor() { return mapaCalorRepository.findAll(); }
    public MapaCalor mapaCalor(Long id) { return mapaCalorRepository.findById(id).orElseThrow(() -> notFound("El mapa de calor solicitado no existe.")); }
    public List<MetricaDocente> metricas() { return metricaDocenteRepository.findAll(); }
    public MetricaDocente metrica(Long id) { return metricaDocenteRepository.findById(id).orElseThrow(() -> invalidRelation("La métrica seleccionada no es válida.")); }
    public List<Reconocimiento> reconocimientos() { return reconocimientoRepository.findAll(); }
    public Reconocimiento reconocimiento(Long id) { return reconocimientoRepository.findById(id).orElseThrow(() -> notFound("El reconocimiento solicitado no existe.")); }

    public long totalDocentes() { return docenteRepository.count(); }
    public long totalTurnos() { return turnoRepository.count(); }
    public long totalIncidentes() { return incidenteRepository.count(); }
    public long totalReasignaciones() { return reasignacionRepository.count(); }
    public long totalRecorridos() { return recorridoRepository.count(); }
    public long totalReconocimientos() { return reconocimientoRepository.count(); }

    private RecursoNoEncontradoException notFound(String message) {
        return new RecursoNoEncontradoException(message);
    }

    private RelacionInvalidaException invalidRelation(String message) {
        return new RelacionInvalidaException(message);
    }
}
