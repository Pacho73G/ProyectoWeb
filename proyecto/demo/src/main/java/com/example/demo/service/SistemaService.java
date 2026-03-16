package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
public class SistemaService {

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
    public Usuario usuario(Long id) { return usuarioRepository.findById(id).orElseThrow(); }
    public Usuario guardar(Usuario entity) { return usuarioRepository.save(entity); }
    public void eliminarUsuario(Long id) { usuarioRepository.deleteById(id); }
    public List<Docente> docentes() { return docenteRepository.findAll(); }
    public Docente docente(Long id) { return docenteRepository.findById(id).orElseThrow(); }
    public Docente guardar(Docente entity) { return docenteRepository.save(entity); }
    public void eliminarDocente(Long id) { docenteRepository.deleteById(id); }
    public List<Coordinador> coordinadores() { return coordinadorRepository.findAll(); }
    public Coordinador coordinador(Long id) { return coordinadorRepository.findById(id).orElseThrow(); }
    public Coordinador guardar(Coordinador entity) { return coordinadorRepository.save(entity); }
    public void eliminarCoordinador(Long id) { coordinadorRepository.deleteById(id); }
    public List<Administrador> administradores() { return administradorRepository.findAll(); }
    public Administrador administrador(Long id) { return administradorRepository.findById(id).orElseThrow(); }
    public Administrador guardar(Administrador entity) { return administradorRepository.save(entity); }
    public void eliminarAdministrador(Long id) { administradorRepository.deleteById(id); }
    public List<ConfiguracionSistema> configuraciones() { return configuracionSistemaRepository.findAll(); }
    public ConfiguracionSistema configuracion(Long id) { return configuracionSistemaRepository.findById(id).orElseThrow(); }
    public ConfiguracionSistema guardar(ConfiguracionSistema entity) { return configuracionSistemaRepository.save(entity); }
    public void eliminarConfiguracion(Long id) { configuracionSistemaRepository.deleteById(id); }
    public List<Zona> zonas() { return zonaRepository.findAll(); }
    public Zona zona(Long id) { return zonaRepository.findById(id).orElseThrow(); }
    public Zona guardar(Zona entity) { return zonaRepository.save(entity); }
    public void eliminarZona(Long id) { zonaRepository.deleteById(id); }
    public List<Turno> turnos() { return turnoRepository.findAll(); }
    public Turno turno(Long id) { return turnoRepository.findById(id).orElseThrow(); }
    public Turno guardar(Turno entity) { return turnoRepository.save(entity); }
    public void eliminarTurno(Long id) { turnoRepository.deleteById(id); }
    public List<CheckIn> checkIns() { return checkInRepository.findAll(); }
    public CheckIn checkIn(Long id) { return checkInRepository.findById(id).orElseThrow(); }
    public CheckIn guardar(CheckIn entity) { return checkInRepository.save(entity); }
    public void eliminarCheckIn(Long id) { checkInRepository.deleteById(id); }
    public List<Incidente> incidentes() { return incidenteRepository.findAll(); }
    public Incidente incidente(Long id) { return incidenteRepository.findById(id).orElseThrow(); }
    public Incidente guardar(Incidente entity) { return incidenteRepository.save(entity); }
    public void eliminarIncidente(Long id) { incidenteRepository.deleteById(id); }
    public List<Reasignacion> reasignaciones() { return reasignacionRepository.findAll(); }
    public Reasignacion reasignacion(Long id) { return reasignacionRepository.findById(id).orElseThrow(); }
    public Reasignacion guardar(Reasignacion entity) { return reasignacionRepository.save(entity); }
    public void eliminarReasignacion(Long id) { reasignacionRepository.deleteById(id); }
    public List<RegistroLimpieza> limpiezas() { return registroLimpiezaRepository.findAll(); }
    public RegistroLimpieza limpieza(Long id) { return registroLimpiezaRepository.findById(id).orElseThrow(); }
    public RegistroLimpieza limpiezaPorTurno(Long turnoId) { return registroLimpiezaRepository.findByTurnoId(turnoId).orElse(null); }
    public RegistroLimpieza guardar(RegistroLimpieza entity) { return registroLimpiezaRepository.save(entity); }
    public void eliminarLimpieza(Long id) { registroLimpiezaRepository.deleteById(id); }
    public List<Notificacion> notificaciones() { return notificacionRepository.findAll(); }
    public Notificacion notificacion(Long id) { return notificacionRepository.findById(id).orElseThrow(); }
    public Notificacion guardar(Notificacion entity) { return notificacionRepository.save(entity); }
    public void eliminarNotificacion(Long id) { notificacionRepository.deleteById(id); }
    public List<Recorrido> recorridos() { return recorridoRepository.findAll(); }
    public Recorrido recorrido(Long id) { return recorridoRepository.findById(id).orElseThrow(); }
    public Recorrido guardar(Recorrido entity) { return recorridoRepository.save(entity); }
    public void eliminarRecorrido(Long id) { recorridoRepository.deleteById(id); }
    public List<CheckpointRecorrido> checkpoints() { return checkpointRecorridoRepository.findAll(); }
    public CheckpointRecorrido checkpoint(Long id) { return checkpointRecorridoRepository.findById(id).orElseThrow(); }
    public CheckpointRecorrido guardar(CheckpointRecorrido entity) { return checkpointRecorridoRepository.save(entity); }
    public void eliminarCheckpoint(Long id) { checkpointRecorridoRepository.deleteById(id); }
    public List<MapaCalor> mapasCalor() { return mapaCalorRepository.findAll(); }
    public MapaCalor mapaCalor(Long id) { return mapaCalorRepository.findById(id).orElseThrow(); }
    public MapaCalor guardar(MapaCalor entity) { return mapaCalorRepository.save(entity); }
    public void eliminarMapaCalor(Long id) { mapaCalorRepository.deleteById(id); }
    public List<MetricaDocente> metricas() { return metricaDocenteRepository.findAll(); }
    public MetricaDocente metrica(Long id) { return metricaDocenteRepository.findById(id).orElseThrow(); }
    public MetricaDocente guardar(MetricaDocente entity) { return metricaDocenteRepository.save(entity); }
    public void eliminarMetrica(Long id) { metricaDocenteRepository.deleteById(id); }
    public List<Reconocimiento> reconocimientos() { return reconocimientoRepository.findAll(); }
    public Reconocimiento reconocimiento(Long id) { return reconocimientoRepository.findById(id).orElseThrow(); }
    public Reconocimiento guardar(Reconocimiento entity) { return reconocimientoRepository.save(entity); }
    public void eliminarReconocimiento(Long id) { reconocimientoRepository.deleteById(id); }

    public long totalDocentes() { return docenteRepository.count(); }
    public long totalTurnos() { return turnoRepository.count(); }
    public long totalIncidentes() { return incidenteRepository.count(); }
    public long totalReasignaciones() { return reasignacionRepository.count(); }
    public long totalRecorridos() { return recorridoRepository.count(); }
    public long totalReconocimientos() { return reconocimientoRepository.count(); }
}
