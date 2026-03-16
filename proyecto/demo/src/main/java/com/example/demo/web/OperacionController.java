package com.example.demo.web;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.CheckIn;
import com.example.demo.model.Incidente;
import com.example.demo.model.Notificacion;
import com.example.demo.model.Reasignacion;
import com.example.demo.model.RegistroLimpieza;
import com.example.demo.model.Turno;
import com.example.demo.model.Zona;
import com.example.demo.service.SistemaService;
import com.example.demo.web.form.CheckInForm;
import com.example.demo.web.form.IncidenteForm;
import com.example.demo.web.form.NotificacionForm;
import com.example.demo.web.form.ReasignacionForm;
import com.example.demo.web.form.RegistroLimpiezaForm;
import com.example.demo.web.form.TurnoForm;
import com.example.demo.web.form.ZonaForm;

@Controller
@RequestMapping
public class OperacionController extends BaseController {

    public OperacionController(SistemaService sistemaService) {
        super(sistemaService);
    }

    @GetMapping("/zonas")
    public String zonas(Model model) { addShared(model); return "operacion/zonas"; }
    @GetMapping("/turnos")
    public String turnos(Model model) { addShared(model); return "operacion/turnos"; }
    @GetMapping("/checkins")
    public String checkins(Model model) { addShared(model); return "operacion/checkins"; }
    @GetMapping("/incidentes")
    public String incidentes(Model model) { addShared(model); return "operacion/incidentes"; }
    @GetMapping("/reasignaciones")
    public String reasignaciones(Model model) { addShared(model); return "operacion/reasignaciones"; }
    @GetMapping("/limpiezas")
    public String limpiezas(Model model) { addShared(model); return "operacion/limpiezas"; }
    @GetMapping("/notificaciones")
    public String notificaciones(Model model) { addShared(model); return "operacion/notificaciones"; }

    @GetMapping("/zonas/nuevo")
    public String nuevaZona(Model model) { addShared(model); model.addAttribute("form", new ZonaForm()); model.addAttribute("titulo", "Nueva zona"); model.addAttribute("accion", "/zonas"); return "operacion/zona-form"; }
    @GetMapping("/turnos/nuevo")
    public String nuevoTurno(Model model) { addShared(model); TurnoForm f=new TurnoForm(); f.setFecha(java.time.LocalDate.now()); model.addAttribute("form", f); model.addAttribute("titulo", "Nuevo turno"); model.addAttribute("accion", "/turnos"); return "operacion/turno-form"; }
    @GetMapping("/checkins/nuevo")
    public String nuevoCheckIn(Model model) { addShared(model); CheckInForm f=new CheckInForm(); f.setTimestamp(LocalDateTime.now()); model.addAttribute("form", f); model.addAttribute("titulo", "Nuevo check-in"); model.addAttribute("accion", "/checkins"); return "operacion/checkin-form"; }
    @GetMapping("/incidentes/nuevo")
    public String nuevoIncidente(Model model) { addShared(model); IncidenteForm f=new IncidenteForm(); f.setRegistradoEn(LocalDateTime.now()); model.addAttribute("form", f); model.addAttribute("titulo", "Nuevo incidente"); model.addAttribute("accion", "/incidentes"); return "operacion/incidente-form"; }
    @GetMapping("/reasignaciones/nuevo")
    public String nuevaReasignacion(Model model) { addShared(model); ReasignacionForm f=new ReasignacionForm(); f.setPropuestaEn(LocalDateTime.now()); model.addAttribute("form", f); model.addAttribute("titulo", "Nueva reasignación"); model.addAttribute("accion", "/reasignaciones"); return "operacion/reasignacion-form"; }
    @GetMapping("/limpiezas/nuevo")
    public String nuevaLimpieza(Model model) { addShared(model); RegistroLimpiezaForm f=new RegistroLimpiezaForm(); f.setRegistradoEn(LocalDateTime.now()); model.addAttribute("form", f); model.addAttribute("titulo", "Nuevo registro de limpieza"); model.addAttribute("accion", "/limpiezas"); return "operacion/limpieza-form"; }
    @GetMapping("/notificaciones/nuevo")
    public String nuevaNotificacion(Model model) { addShared(model); NotificacionForm f=new NotificacionForm(); f.setEnviadaEn(LocalDateTime.now()); model.addAttribute("form", f); model.addAttribute("titulo", "Nueva notificación"); model.addAttribute("accion", "/notificaciones"); return "operacion/notificacion-form"; }

    @GetMapping("/zonas/{id}/editar")
    public String editarZona(@PathVariable Long id, Model model) { Zona e=sistemaService.zona(id); ZonaForm f=new ZonaForm(); f.setId(e.getId()); f.setNombre(e.getNombre()); f.setDescripcion(e.getDescripcion()); f.setUbicacion(e.getUbicacion()); f.setCapacidadMaxima(e.getCapacidadMaxima()); f.setActiva(e.getActiva()); addShared(model); model.addAttribute("form", f); model.addAttribute("titulo", "Editar zona"); model.addAttribute("accion", "/zonas/"+id); return "operacion/zona-form"; }
    @GetMapping("/turnos/{id}/editar")
    public String editarTurno(@PathVariable Long id, Model model) { Turno e=sistemaService.turno(id); TurnoForm f=new TurnoForm(); f.setId(e.getId()); f.setDocenteId(e.getDocente().getId()); f.setZonaId(e.getZona().getId()); f.setFecha(e.getFecha()); f.setHoraInicio(e.getHoraInicio()); f.setHoraFin(e.getHoraFin()); f.setFranja(e.getFranja()); f.setEstado(e.getEstado()); f.setAbiertoEn(e.getAbiertoEn()); f.setCerradoEn(e.getCerradoEn()); addShared(model); model.addAttribute("form", f); model.addAttribute("titulo", "Editar turno"); model.addAttribute("accion", "/turnos/"+id); return "operacion/turno-form"; }
    @GetMapping("/checkins/{id}/editar")
    public String editarCheckIn(@PathVariable Long id, Model model) { CheckIn e=sistemaService.checkIn(id); CheckInForm f=new CheckInForm(); f.setId(e.getId()); f.setTurnoId(e.getTurno().getId()); f.setDocenteId(e.getDocente().getId()); f.setZonaId(e.getZona().getId()); f.setTimestamp(e.getTimestamp()); f.setMetodo(e.getMetodo()); f.setEvidencia(e.getEvidencia()); f.setValido(e.getValido()); addShared(model); model.addAttribute("form", f); model.addAttribute("titulo", "Editar check-in"); model.addAttribute("accion", "/checkins/"+id); return "operacion/checkin-form"; }
    @GetMapping("/incidentes/{id}/editar")
    public String editarIncidente(@PathVariable Long id, Model model) { Incidente e=sistemaService.incidente(id); IncidenteForm f=new IncidenteForm(); f.setId(e.getId()); f.setTurnoId(e.getTurno().getId()); f.setDocenteId(e.getDocente().getId()); f.setZonaId(e.getZona().getId()); f.setTipo(e.getTipo()); f.setSeveridad(e.getSeveridad()); f.setDescripcion(e.getDescripcion()); f.setObservacionSocial(e.getObservacionSocial()); f.setRegistradoEn(e.getRegistradoEn()); f.setRequiereSeguimiento(e.getRequiereSeguimiento()); addShared(model); model.addAttribute("form", f); model.addAttribute("titulo", "Editar incidente"); model.addAttribute("accion", "/incidentes/"+id); return "operacion/incidente-form"; }
    @GetMapping("/reasignaciones/{id}/editar")
    public String editarReasignacion(@PathVariable Long id, Model model) { Reasignacion e=sistemaService.reasignacion(id); ReasignacionForm f=new ReasignacionForm(); f.setId(e.getId()); f.setTurnoId(e.getTurno().getId()); f.setDocenteSolicitanteId(e.getDocenteSolicitante().getId()); f.setDocenteReemplazoId(e.getDocenteReemplazo()!=null?e.getDocenteReemplazo().getId():null); f.setMotivo(e.getMotivo()); f.setEstado(e.getEstado()); f.setPropuestaEn(e.getPropuestaEn()); f.setRespondidaEn(e.getRespondidaEn()); f.setSegundosVentana(e.getSegundosVentana()); addShared(model); model.addAttribute("form", f); model.addAttribute("titulo", "Editar reasignación"); model.addAttribute("accion", "/reasignaciones/"+id); return "operacion/reasignacion-form"; }
    @GetMapping("/limpiezas/{id}/editar")
    public String editarLimpieza(@PathVariable Long id, Model model) { RegistroLimpieza e=sistemaService.limpieza(id); RegistroLimpiezaForm f=new RegistroLimpiezaForm(); f.setId(e.getId()); f.setTurnoId(e.getTurno().getId()); f.setEscala(e.getEscala()); f.setObservaciones(e.getObservaciones()); f.setRegistradoEn(e.getRegistradoEn()); addShared(model); model.addAttribute("form", f); model.addAttribute("titulo", "Editar limpieza"); model.addAttribute("accion", "/limpiezas/"+id); return "operacion/limpieza-form"; }
    @GetMapping("/notificaciones/{id}/editar")
    public String editarNotificacion(@PathVariable Long id, Model model) { Notificacion e=sistemaService.notificacion(id); NotificacionForm f=new NotificacionForm(); f.setId(e.getId()); f.setTurnoId(e.getTurno().getId()); f.setTipo(e.getTipo()); f.setMensaje(e.getMensaje()); f.setEnviadaEn(e.getEnviadaEn()); f.setLeida(e.getLeida()); f.setMinutosAnticipacion(e.getMinutosAnticipacion()); addShared(model); model.addAttribute("form", f); model.addAttribute("titulo", "Editar notificación"); model.addAttribute("accion", "/notificaciones/"+id); return "operacion/notificacion-form"; }

    @PostMapping("/zonas") public String crearZona(ZonaForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,new Zona())); ra.addFlashAttribute("successMessage","Zona creada."); return "redirect:/zonas"; }
    @PostMapping("/zonas/{id}") public String actualizarZona(@PathVariable Long id, ZonaForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,sistemaService.zona(id))); ra.addFlashAttribute("successMessage","Zona actualizada."); return "redirect:/zonas"; }
    @PostMapping("/zonas/{id}/eliminar") public String eliminarZona(@PathVariable Long id, RedirectAttributes ra){ sistemaService.eliminarZona(id); ra.addFlashAttribute("successMessage","Zona eliminada."); return "redirect:/zonas"; }
    @PostMapping("/turnos") public String crearTurno(TurnoForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,new Turno())); ra.addFlashAttribute("successMessage","Turno creado."); return "redirect:/turnos"; }
    @PostMapping("/turnos/{id}") public String actualizarTurno(@PathVariable Long id, TurnoForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,sistemaService.turno(id))); ra.addFlashAttribute("successMessage","Turno actualizado."); return "redirect:/turnos"; }
    @PostMapping("/turnos/{id}/eliminar") public String eliminarTurno(@PathVariable Long id, RedirectAttributes ra){ sistemaService.eliminarTurno(id); ra.addFlashAttribute("successMessage","Turno eliminado."); return "redirect:/turnos"; }
    @PostMapping("/checkins") public String crearCheckIn(CheckInForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,new CheckIn())); ra.addFlashAttribute("successMessage","Check-in creado."); return "redirect:/checkins"; }
    @PostMapping("/checkins/{id}") public String actualizarCheckIn(@PathVariable Long id, CheckInForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,sistemaService.checkIn(id))); ra.addFlashAttribute("successMessage","Check-in actualizado."); return "redirect:/checkins"; }
    @PostMapping("/checkins/{id}/eliminar") public String eliminarCheckIn(@PathVariable Long id, RedirectAttributes ra){ sistemaService.eliminarCheckIn(id); ra.addFlashAttribute("successMessage","Check-in eliminado."); return "redirect:/checkins"; }
    @PostMapping("/incidentes") public String crearIncidente(IncidenteForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,new Incidente())); ra.addFlashAttribute("successMessage","Incidente creado."); return "redirect:/incidentes"; }
    @PostMapping("/incidentes/{id}") public String actualizarIncidente(@PathVariable Long id, IncidenteForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,sistemaService.incidente(id))); ra.addFlashAttribute("successMessage","Incidente actualizado."); return "redirect:/incidentes"; }
    @PostMapping("/incidentes/{id}/eliminar") public String eliminarIncidente(@PathVariable Long id, RedirectAttributes ra){ sistemaService.eliminarIncidente(id); ra.addFlashAttribute("successMessage","Incidente eliminado."); return "redirect:/incidentes"; }
    @PostMapping("/reasignaciones") public String crearReasignacion(ReasignacionForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,new Reasignacion())); ra.addFlashAttribute("successMessage","Reasignación creada."); return "redirect:/reasignaciones"; }
    @PostMapping("/reasignaciones/{id}") public String actualizarReasignacion(@PathVariable Long id, ReasignacionForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,sistemaService.reasignacion(id))); ra.addFlashAttribute("successMessage","Reasignación actualizada."); return "redirect:/reasignaciones"; }
    @PostMapping("/reasignaciones/{id}/eliminar") public String eliminarReasignacion(@PathVariable Long id, RedirectAttributes ra){ sistemaService.eliminarReasignacion(id); ra.addFlashAttribute("successMessage","Reasignación eliminada."); return "redirect:/reasignaciones"; }
    @PostMapping("/limpiezas") public String crearLimpieza(RegistroLimpiezaForm f, RedirectAttributes ra){ RegistroLimpieza existente=sistemaService.limpiezaPorTurno(f.getTurnoId()); sistemaService.guardar(map(f,existente!=null?existente:new RegistroLimpieza())); ra.addFlashAttribute("successMessage",existente!=null?"Limpieza actualizada para ese turno.":"Limpieza creada."); return "redirect:/limpiezas"; }
    @PostMapping("/limpiezas/{id}") public String actualizarLimpieza(@PathVariable Long id, RegistroLimpiezaForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,sistemaService.limpieza(id))); ra.addFlashAttribute("successMessage","Limpieza actualizada."); return "redirect:/limpiezas"; }
    @PostMapping("/limpiezas/{id}/eliminar") public String eliminarLimpieza(@PathVariable Long id, RedirectAttributes ra){ sistemaService.eliminarLimpieza(id); ra.addFlashAttribute("successMessage","Limpieza eliminada."); return "redirect:/limpiezas"; }
    @PostMapping("/notificaciones") public String crearNotificacion(NotificacionForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,new Notificacion())); ra.addFlashAttribute("successMessage","Notificación creada."); return "redirect:/notificaciones"; }
    @PostMapping("/notificaciones/{id}") public String actualizarNotificacion(@PathVariable Long id, NotificacionForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,sistemaService.notificacion(id))); ra.addFlashAttribute("successMessage","Notificación actualizada."); return "redirect:/notificaciones"; }
    @PostMapping("/notificaciones/{id}/eliminar") public String eliminarNotificacion(@PathVariable Long id, RedirectAttributes ra){ sistemaService.eliminarNotificacion(id); ra.addFlashAttribute("successMessage","Notificación eliminada."); return "redirect:/notificaciones"; }

    private Zona map(ZonaForm f, Zona e){ e.setNombre(f.getNombre()); e.setDescripcion(f.getDescripcion()); e.setUbicacion(f.getUbicacion()); e.setCapacidadMaxima(f.getCapacidadMaxima()); e.setActiva(f.getActiva()); return e; }
    private Turno map(TurnoForm f, Turno e){ e.setDocente(sistemaService.docente(f.getDocenteId())); e.setZona(sistemaService.zona(f.getZonaId())); e.setFecha(f.getFecha()); e.setHoraInicio(f.getHoraInicio()); e.setHoraFin(f.getHoraFin()); e.setFranja(f.getFranja()); e.setEstado(f.getEstado()); e.setAbiertoEn(f.getAbiertoEn()); e.setCerradoEn(f.getCerradoEn()); return e; }
    private CheckIn map(CheckInForm f, CheckIn e){ e.setTurno(sistemaService.turno(f.getTurnoId())); e.setDocente(sistemaService.docente(f.getDocenteId())); e.setZona(sistemaService.zona(f.getZonaId())); e.setTimestamp(f.getTimestamp()); e.setMetodo(f.getMetodo()); e.setEvidencia(f.getEvidencia()); e.setValido(f.getValido()); return e; }
    private Incidente map(IncidenteForm f, Incidente e){ e.setTurno(sistemaService.turno(f.getTurnoId())); e.setDocente(sistemaService.docente(f.getDocenteId())); e.setZona(sistemaService.zona(f.getZonaId())); e.setTipo(f.getTipo()); e.setSeveridad(f.getSeveridad()); e.setDescripcion(f.getDescripcion()); e.setObservacionSocial(f.getObservacionSocial()); e.setRegistradoEn(f.getRegistradoEn()); e.setRequiereSeguimiento(f.getRequiereSeguimiento()); return e; }
    private Reasignacion map(ReasignacionForm f, Reasignacion e){ e.setTurno(sistemaService.turno(f.getTurnoId())); e.setDocenteSolicitante(sistemaService.docente(f.getDocenteSolicitanteId())); e.setDocenteReemplazo(f.getDocenteReemplazoId()!=null?sistemaService.docente(f.getDocenteReemplazoId()):null); e.setMotivo(f.getMotivo()); e.setEstado(f.getEstado()); e.setPropuestaEn(f.getPropuestaEn()); e.setRespondidaEn(f.getRespondidaEn()); e.setSegundosVentana(f.getSegundosVentana()); return e; }
    private RegistroLimpieza map(RegistroLimpiezaForm f, RegistroLimpieza e){ e.setTurno(sistemaService.turno(f.getTurnoId())); e.setEscala(f.getEscala()); e.setObservaciones(f.getObservaciones()); e.setRegistradoEn(f.getRegistradoEn()); return e; }
    private Notificacion map(NotificacionForm f, Notificacion e){ e.setTurno(sistemaService.turno(f.getTurnoId())); e.setTipo(f.getTipo()); e.setMensaje(f.getMensaje()); e.setEnviadaEn(f.getEnviadaEn()); e.setLeida(f.getLeida()); e.setMinutosAnticipacion(f.getMinutosAnticipacion()); return e; }
}
