package com.example.demo.web;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import com.example.demo.service.CatalogQueryService;
import com.example.demo.service.OperacionManagementService;
import com.example.demo.web.form.CheckInForm;
import com.example.demo.web.form.IncidenteForm;
import com.example.demo.web.form.NotificacionForm;
import com.example.demo.web.form.ReasignacionForm;
import com.example.demo.web.form.RegistroLimpiezaForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping
public class OperacionRegistroController extends BaseController {

    private final OperacionManagementService operacionManagementService;

    public OperacionRegistroController(CatalogQueryService catalogQueryService, OperacionManagementService operacionManagementService) {
        super(catalogQueryService);
        this.operacionManagementService = operacionManagementService;
    }

    @GetMapping("/checkins")
    public String checkins(Model model) { addShared(model); return "operacion/checkins"; }

    @GetMapping("/checkins/nuevo")
    public String nuevoCheckIn(Model model) {
        CheckInForm form = new CheckInForm();
        form.setTimestamp(LocalDateTime.now());
        return prepareForm(model, "operacion/checkin-form", form, "Nuevo check-in", "/checkins");
    }

    @GetMapping("/checkins/{id}/editar")
    public String editarCheckIn(@PathVariable Long id, Model model) {
        CheckIn entity = catalogQueryService.checkIn(id);
        CheckInForm form = new CheckInForm();
        form.setId(entity.getId());
        form.setTurnoId(entity.getTurno().getId());
        form.setDocenteId(entity.getDocente().getId());
        form.setZonaId(entity.getZona().getId());
        form.setTimestamp(entity.getTimestamp());
        form.setMetodo(entity.getMetodo());
        form.setEvidencia(entity.getEvidencia());
        form.setValido(entity.getValido());
        return prepareForm(model, "operacion/checkin-form", form, "Editar check-in", "/checkins/" + id);
    }

    @PostMapping("/checkins")
    public String crearCheckIn(@Valid CheckInForm form, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/checkin-form", form, "Nuevo check-in", "/checkins");
        }
        operacionManagementService.guardar(map(form, new CheckIn()));
        ra.addFlashAttribute("successMessage", "Check-in creado.");
        return "redirect:/checkins";
    }

    @PostMapping("/checkins/{id}")
    public String actualizarCheckIn(@PathVariable Long id, @Valid CheckInForm form, BindingResult bindingResult,
                                    Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/checkin-form", form, "Editar check-in", "/checkins/" + id);
        }
        operacionManagementService.guardar(map(form, catalogQueryService.checkIn(id)));
        ra.addFlashAttribute("successMessage", "Check-in actualizado.");
        return "redirect:/checkins";
    }

    @PostMapping("/checkins/{id}/eliminar")
    public String eliminarCheckIn(@PathVariable Long id, RedirectAttributes ra) {
        operacionManagementService.eliminarCheckIn(id);
        ra.addFlashAttribute("successMessage", "Check-in eliminado.");
        return "redirect:/checkins";
    }

    @GetMapping("/incidentes")
    public String incidentes(Model model) { addShared(model); return "operacion/incidentes"; }

    @GetMapping("/incidentes/nuevo")
    public String nuevoIncidente(Model model) {
        IncidenteForm form = new IncidenteForm();
        form.setRegistradoEn(LocalDateTime.now());
        return prepareForm(model, "operacion/incidente-form", form, "Nuevo incidente", "/incidentes");
    }

    @GetMapping("/incidentes/{id}/editar")
    public String editarIncidente(@PathVariable Long id, Model model) {
        Incidente entity = catalogQueryService.incidente(id);
        IncidenteForm form = new IncidenteForm();
        form.setId(entity.getId());
        form.setTurnoId(entity.getTurno().getId());
        form.setDocenteId(entity.getDocente().getId());
        form.setZonaId(entity.getZona().getId());
        form.setTipo(entity.getTipo());
        form.setSeveridad(entity.getSeveridad());
        form.setDescripcion(entity.getDescripcion());
        form.setObservacionSocial(entity.getObservacionSocial());
        form.setRegistradoEn(entity.getRegistradoEn());
        form.setRequiereSeguimiento(entity.getRequiereSeguimiento());
        return prepareForm(model, "operacion/incidente-form", form, "Editar incidente", "/incidentes/" + id);
    }

    @PostMapping("/incidentes")
    public String crearIncidente(@Valid IncidenteForm form, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/incidente-form", form, "Nuevo incidente", "/incidentes");
        }
        operacionManagementService.guardar(map(form, new Incidente()));
        ra.addFlashAttribute("successMessage", "Incidente creado.");
        return "redirect:/incidentes";
    }

    @PostMapping("/incidentes/{id}")
    public String actualizarIncidente(@PathVariable Long id, @Valid IncidenteForm form, BindingResult bindingResult,
                                      Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/incidente-form", form, "Editar incidente", "/incidentes/" + id);
        }
        operacionManagementService.guardar(map(form, catalogQueryService.incidente(id)));
        ra.addFlashAttribute("successMessage", "Incidente actualizado.");
        return "redirect:/incidentes";
    }

    @PostMapping("/incidentes/{id}/eliminar")
    public String eliminarIncidente(@PathVariable Long id, RedirectAttributes ra) {
        operacionManagementService.eliminarIncidente(id);
        ra.addFlashAttribute("successMessage", "Incidente eliminado.");
        return "redirect:/incidentes";
    }

    @GetMapping("/reasignaciones")
    public String reasignaciones(Model model) { addShared(model); return "operacion/reasignaciones"; }

    @GetMapping("/reasignaciones/nuevo")
    public String nuevaReasignacion(Model model) {
        ReasignacionForm form = new ReasignacionForm();
        form.setPropuestaEn(LocalDateTime.now());
        return prepareForm(model, "operacion/reasignacion-form", form, "Nueva reasignación", "/reasignaciones");
    }

    @GetMapping("/reasignaciones/{id}/editar")
    public String editarReasignacion(@PathVariable Long id, Model model) {
        Reasignacion entity = catalogQueryService.reasignacion(id);
        ReasignacionForm form = new ReasignacionForm();
        form.setId(entity.getId());
        form.setTurnoId(entity.getTurno().getId());
        form.setDocenteSolicitanteId(entity.getDocenteSolicitante().getId());
        form.setDocenteReemplazoId(entity.getDocenteReemplazo() != null ? entity.getDocenteReemplazo().getId() : null);
        form.setMotivo(entity.getMotivo());
        form.setEstado(entity.getEstado());
        form.setPropuestaEn(entity.getPropuestaEn());
        form.setRespondidaEn(entity.getRespondidaEn());
        form.setSegundosVentana(entity.getSegundosVentana());
        return prepareForm(model, "operacion/reasignacion-form", form, "Editar reasignación", "/reasignaciones/" + id);
    }

    @PostMapping("/reasignaciones")
    public String crearReasignacion(@Valid ReasignacionForm form, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/reasignacion-form", form, "Nueva reasignación", "/reasignaciones");
        }
        operacionManagementService.guardar(map(form, new Reasignacion()));
        ra.addFlashAttribute("successMessage", "Reasignación creada.");
        return "redirect:/reasignaciones";
    }

    @PostMapping("/reasignaciones/{id}")
    public String actualizarReasignacion(@PathVariable Long id, @Valid ReasignacionForm form, BindingResult bindingResult,
                                         Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/reasignacion-form", form, "Editar reasignación", "/reasignaciones/" + id);
        }
        operacionManagementService.guardar(map(form, catalogQueryService.reasignacion(id)));
        ra.addFlashAttribute("successMessage", "Reasignación actualizada.");
        return "redirect:/reasignaciones";
    }

    @PostMapping("/reasignaciones/{id}/eliminar")
    public String eliminarReasignacion(@PathVariable Long id, RedirectAttributes ra) {
        operacionManagementService.eliminarReasignacion(id);
        ra.addFlashAttribute("successMessage", "Reasignación eliminada.");
        return "redirect:/reasignaciones";
    }

    @GetMapping("/limpiezas")
    public String limpiezas(Model model) { addShared(model); return "operacion/limpiezas"; }

    @GetMapping("/limpiezas/nuevo")
    public String nuevaLimpieza(Model model) {
        RegistroLimpiezaForm form = new RegistroLimpiezaForm();
        form.setRegistradoEn(LocalDateTime.now());
        return prepareForm(model, "operacion/limpieza-form", form, "Nuevo registro de limpieza", "/limpiezas");
    }

    @GetMapping("/limpiezas/{id}/editar")
    public String editarLimpieza(@PathVariable Long id, Model model) {
        RegistroLimpieza entity = catalogQueryService.limpieza(id);
        RegistroLimpiezaForm form = new RegistroLimpiezaForm();
        form.setId(entity.getId());
        form.setTurnoId(entity.getTurno().getId());
        form.setEscala(entity.getEscala());
        form.setObservaciones(entity.getObservaciones());
        form.setRegistradoEn(entity.getRegistradoEn());
        return prepareForm(model, "operacion/limpieza-form", form, "Editar limpieza", "/limpiezas/" + id);
    }

    @PostMapping("/limpiezas")
    public String crearLimpieza(@Valid RegistroLimpiezaForm form, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/limpieza-form", form, "Nuevo registro de limpieza", "/limpiezas");
        }
        RegistroLimpieza existente = catalogQueryService.limpiezaPorTurno(form.getTurnoId());
        operacionManagementService.guardar(map(form, existente != null ? existente : new RegistroLimpieza()));
        ra.addFlashAttribute("successMessage", existente != null ? "Limpieza actualizada para ese turno." : "Limpieza creada.");
        return "redirect:/limpiezas";
    }

    @PostMapping("/limpiezas/{id}")
    public String actualizarLimpieza(@PathVariable Long id, @Valid RegistroLimpiezaForm form, BindingResult bindingResult,
                                     Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/limpieza-form", form, "Editar limpieza", "/limpiezas/" + id);
        }
        operacionManagementService.guardar(map(form, catalogQueryService.limpieza(id)));
        ra.addFlashAttribute("successMessage", "Limpieza actualizada.");
        return "redirect:/limpiezas";
    }

    @PostMapping("/limpiezas/{id}/eliminar")
    public String eliminarLimpieza(@PathVariable Long id, RedirectAttributes ra) {
        operacionManagementService.eliminarLimpieza(id);
        ra.addFlashAttribute("successMessage", "Limpieza eliminada.");
        return "redirect:/limpiezas";
    }

    @GetMapping("/notificaciones")
    public String notificaciones(Model model) { addShared(model); return "operacion/notificaciones"; }

    @GetMapping("/notificaciones/nuevo")
    public String nuevaNotificacion(Model model) {
        NotificacionForm form = new NotificacionForm();
        form.setEnviadaEn(LocalDateTime.now());
        return prepareForm(model, "operacion/notificacion-form", form, "Nueva notificación", "/notificaciones");
    }

    @GetMapping("/notificaciones/{id}/editar")
    public String editarNotificacion(@PathVariable Long id, Model model) {
        Notificacion entity = catalogQueryService.notificacion(id);
        NotificacionForm form = new NotificacionForm();
        form.setId(entity.getId());
        form.setTurnoId(entity.getTurno().getId());
        form.setTipo(entity.getTipo());
        form.setMensaje(entity.getMensaje());
        form.setEnviadaEn(entity.getEnviadaEn());
        form.setLeida(entity.getLeida());
        form.setMinutosAnticipacion(entity.getMinutosAnticipacion());
        return prepareForm(model, "operacion/notificacion-form", form, "Editar notificación", "/notificaciones/" + id);
    }

    @PostMapping("/notificaciones")
    public String crearNotificacion(@Valid NotificacionForm form, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/notificacion-form", form, "Nueva notificación", "/notificaciones");
        }
        operacionManagementService.guardar(map(form, new Notificacion()));
        ra.addFlashAttribute("successMessage", "Notificación creada.");
        return "redirect:/notificaciones";
    }

    @PostMapping("/notificaciones/{id}")
    public String actualizarNotificacion(@PathVariable Long id, @Valid NotificacionForm form, BindingResult bindingResult,
                                         Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/notificacion-form", form, "Editar notificación", "/notificaciones/" + id);
        }
        operacionManagementService.guardar(map(form, catalogQueryService.notificacion(id)));
        ra.addFlashAttribute("successMessage", "Notificación actualizada.");
        return "redirect:/notificaciones";
    }

    @PostMapping("/notificaciones/{id}/eliminar")
    public String eliminarNotificacion(@PathVariable Long id, RedirectAttributes ra) {
        operacionManagementService.eliminarNotificacion(id);
        ra.addFlashAttribute("successMessage", "Notificación eliminada.");
        return "redirect:/notificaciones";
    }

    private CheckIn map(CheckInForm form, CheckIn entity) {
        entity.setTurno(catalogQueryService.turno(form.getTurnoId()));
        entity.setDocente(catalogQueryService.docente(form.getDocenteId()));
        entity.setZona(catalogQueryService.zona(form.getZonaId()));
        entity.setTimestamp(form.getTimestamp());
        entity.setMetodo(form.getMetodo());
        entity.setEvidencia(form.getEvidencia());
        entity.setValido(form.getValido());
        return entity;
    }

    private Incidente map(IncidenteForm form, Incidente entity) {
        entity.setTurno(catalogQueryService.turno(form.getTurnoId()));
        entity.setDocente(catalogQueryService.docente(form.getDocenteId()));
        entity.setZona(catalogQueryService.zona(form.getZonaId()));
        entity.setTipo(form.getTipo());
        entity.setSeveridad(form.getSeveridad());
        entity.setDescripcion(form.getDescripcion() != null ? form.getDescripcion() : "");
        entity.setObservacionSocial(form.getObservacionSocial());
        entity.setRegistradoEn(form.getRegistradoEn());
        entity.setRequiereSeguimiento(form.getRequiereSeguimiento());
        return entity;
    }

    private Reasignacion map(ReasignacionForm form, Reasignacion entity) {
        entity.setTurno(catalogQueryService.turno(form.getTurnoId()));
        entity.setDocenteSolicitante(catalogQueryService.docente(form.getDocenteSolicitanteId()));
        entity.setDocenteReemplazo(form.getDocenteReemplazoId() != null ? catalogQueryService.docente(form.getDocenteReemplazoId()) : null);
        entity.setMotivo(form.getMotivo() != null ? form.getMotivo() : "");
        entity.setEstado(form.getEstado());
        entity.setPropuestaEn(form.getPropuestaEn());
        entity.setRespondidaEn(form.getRespondidaEn());
        entity.setSegundosVentana(form.getSegundosVentana());
        return entity;
    }

    private RegistroLimpieza map(RegistroLimpiezaForm form, RegistroLimpieza entity) {
        entity.setTurno(catalogQueryService.turno(form.getTurnoId()));
        entity.setEscala(form.getEscala());
        entity.setObservaciones(form.getObservaciones() != null ? form.getObservaciones() : "");
        entity.setRegistradoEn(form.getRegistradoEn());
        return entity;
    }

    private Notificacion map(NotificacionForm form, Notificacion entity) {
        entity.setTurno(catalogQueryService.turno(form.getTurnoId()));
        entity.setTipo(form.getTipo());
        entity.setMensaje(form.getMensaje() != null ? form.getMensaje() : "");
        entity.setEnviadaEn(form.getEnviadaEn());
        entity.setLeida(form.getLeida());
        entity.setMinutosAnticipacion(form.getMinutosAnticipacion());
        return entity;
    }
}
