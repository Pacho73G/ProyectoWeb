/* Archivo documentado: Fachada interna usada por la carga inicial. Simplifica el acceso a varios servicios concretos cuando se construyen datos semilla. */
package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.model.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
/**
 * Fachada usada por vistas compartidas y batch para no acoplarse a varios servicios concretos.
 */
public class SistemaService {

    private final CatalogQueryService catalogQueryService;
    private final UsuarioManagementService usuarioManagementService;
    private final OperacionManagementService operacionManagementService;
    private final AnaliticaManagementService analiticaManagementService;

    public java.util.List<Usuario> usuarios() { return catalogQueryService.usuarios(); }
    public Usuario usuario(Long id) { return catalogQueryService.usuario(id); }
    public Usuario guardar(Usuario entity) { return usuarioManagementService.guardar(entity); }
    public void eliminarUsuario(Long id) { usuarioManagementService.eliminarUsuario(id); }
    public java.util.List<Docente> docentes() { return catalogQueryService.docentes(); }
    public Docente docente(Long id) { return catalogQueryService.docente(id); }
    public Docente guardar(Docente entity) { return usuarioManagementService.guardar(entity); }
    public void eliminarDocente(Long id) { usuarioManagementService.eliminarDocente(id); }
    public java.util.List<Coordinador> coordinadores() { return catalogQueryService.coordinadores(); }
    public Coordinador coordinador(Long id) { return catalogQueryService.coordinador(id); }
    public Coordinador guardar(Coordinador entity) { return usuarioManagementService.guardar(entity); }
    public void eliminarCoordinador(Long id) { usuarioManagementService.eliminarCoordinador(id); }
    public java.util.List<Administrador> administradores() { return catalogQueryService.administradores(); }
    public Administrador administrador(Long id) { return catalogQueryService.administrador(id); }
    public Administrador guardar(Administrador entity) { return usuarioManagementService.guardar(entity); }
    public void eliminarAdministrador(Long id) { usuarioManagementService.eliminarAdministrador(id); }
    public java.util.List<ConfiguracionSistema> configuraciones() { return catalogQueryService.configuraciones(); }
    public ConfiguracionSistema configuracion(Long id) { return catalogQueryService.configuracion(id); }
    public ConfiguracionSistema guardar(ConfiguracionSistema entity) { return usuarioManagementService.guardar(entity); }
    public void eliminarConfiguracion(Long id) { usuarioManagementService.eliminarConfiguracion(id); }
    public java.util.List<Zona> zonas() { return catalogQueryService.zonas(); }
    public Zona zona(Long id) { return catalogQueryService.zona(id); }
    public Zona guardar(Zona entity) { return operacionManagementService.guardar(entity); }
    public void eliminarZona(Long id) { operacionManagementService.eliminarZona(id); }
    public java.util.List<Turno> turnos() { return catalogQueryService.turnos(); }
    public Turno turno(Long id) { return catalogQueryService.turno(id); }
    public Turno guardar(Turno entity) { return operacionManagementService.guardar(entity); }
    public void eliminarTurno(Long id) { operacionManagementService.eliminarTurno(id); }
    public java.util.List<CheckIn> checkIns() { return catalogQueryService.checkIns(); }
    public CheckIn checkIn(Long id) { return catalogQueryService.checkIn(id); }
    public CheckIn guardar(CheckIn entity) { return operacionManagementService.guardar(entity); }
    public void eliminarCheckIn(Long id) { operacionManagementService.eliminarCheckIn(id); }
    public java.util.List<Incidente> incidentes() { return catalogQueryService.incidentes(); }
    public Incidente incidente(Long id) { return catalogQueryService.incidente(id); }
    public Incidente guardar(Incidente entity) { return operacionManagementService.guardar(entity); }
    public void eliminarIncidente(Long id) { operacionManagementService.eliminarIncidente(id); }
    public java.util.List<Reasignacion> reasignaciones() { return catalogQueryService.reasignaciones(); }
    public Reasignacion reasignacion(Long id) { return catalogQueryService.reasignacion(id); }
    public Reasignacion guardar(Reasignacion entity) { return operacionManagementService.guardar(entity); }
    public void eliminarReasignacion(Long id) { operacionManagementService.eliminarReasignacion(id); }
    public java.util.List<RegistroLimpieza> limpiezas() { return catalogQueryService.limpiezas(); }
    public RegistroLimpieza limpieza(Long id) { return catalogQueryService.limpieza(id); }
    public RegistroLimpieza limpiezaPorTurno(Long turnoId) { return catalogQueryService.limpiezaPorTurno(turnoId); }
    public RegistroLimpieza guardar(RegistroLimpieza entity) { return operacionManagementService.guardar(entity); }
    public void eliminarLimpieza(Long id) { operacionManagementService.eliminarLimpieza(id); }
    public java.util.List<Notificacion> notificaciones() { return catalogQueryService.notificaciones(); }
    public Notificacion notificacion(Long id) { return catalogQueryService.notificacion(id); }
    public Notificacion guardar(Notificacion entity) { return operacionManagementService.guardar(entity); }
    public void eliminarNotificacion(Long id) { operacionManagementService.eliminarNotificacion(id); }
    public java.util.List<Recorrido> recorridos() { return catalogQueryService.recorridos(); }
    public Recorrido recorrido(Long id) { return catalogQueryService.recorrido(id); }
    public Recorrido guardar(Recorrido entity) { return analiticaManagementService.guardar(entity); }
    public void eliminarRecorrido(Long id) { analiticaManagementService.eliminarRecorrido(id); }
    public java.util.List<CheckpointRecorrido> checkpoints() { return catalogQueryService.checkpoints(); }
    public CheckpointRecorrido checkpoint(Long id) { return catalogQueryService.checkpoint(id); }
    public CheckpointRecorrido guardar(CheckpointRecorrido entity) { return analiticaManagementService.guardar(entity); }
    public void eliminarCheckpoint(Long id) { analiticaManagementService.eliminarCheckpoint(id); }
    public java.util.List<MapaCalor> mapasCalor() { return catalogQueryService.mapasCalor(); }
    public MapaCalor mapaCalor(Long id) { return catalogQueryService.mapaCalor(id); }
    public MapaCalor guardar(MapaCalor entity) { return analiticaManagementService.guardar(entity); }
    public void eliminarMapaCalor(Long id) { analiticaManagementService.eliminarMapaCalor(id); }
    public java.util.List<MetricaDocente> metricas() { return catalogQueryService.metricas(); }
    public MetricaDocente metrica(Long id) { return catalogQueryService.metrica(id); }
    public MetricaDocente guardar(MetricaDocente entity) { return analiticaManagementService.guardar(entity); }
    public void eliminarMetrica(Long id) { analiticaManagementService.eliminarMetrica(id); }
    public java.util.List<Reconocimiento> reconocimientos() { return catalogQueryService.reconocimientos(); }
    public Reconocimiento reconocimiento(Long id) { return catalogQueryService.reconocimiento(id); }
    public Reconocimiento guardar(Reconocimiento entity) { return analiticaManagementService.guardar(entity); }
    public void eliminarReconocimiento(Long id) { analiticaManagementService.eliminarReconocimiento(id); }

    public long totalDocentes() { return catalogQueryService.totalDocentes(); }
    public long totalTurnos() { return catalogQueryService.totalTurnos(); }
    public long totalIncidentes() { return catalogQueryService.totalIncidentes(); }
    public long totalReasignaciones() { return catalogQueryService.totalReasignaciones(); }
    public long totalRecorridos() { return catalogQueryService.totalRecorridos(); }
    public long totalReconocimientos() { return catalogQueryService.totalReconocimientos(); }
}
