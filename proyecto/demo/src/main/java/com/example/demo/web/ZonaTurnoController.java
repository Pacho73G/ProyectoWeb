package com.example.demo.web;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Turno;
import com.example.demo.model.Zona;
import com.example.demo.service.CatalogQueryService;
import com.example.demo.service.OperacionManagementService;
import com.example.demo.web.form.TurnoForm;
import com.example.demo.web.form.ZonaForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping
/**
 * CRUD de estructura operativa básica: zonas y turnos.
 */
public class ZonaTurnoController extends BaseController {

    private final OperacionManagementService operacionManagementService;

    public ZonaTurnoController(CatalogQueryService catalogQueryService, OperacionManagementService operacionManagementService) {
        super(catalogQueryService);
        this.operacionManagementService = operacionManagementService;
    }

    @GetMapping("/zonas")
    public String zonas(Model model) { addShared(model); return "operacion/zonas"; }

    @GetMapping("/zonas/nuevo")
    public String nuevaZona(Model model) { return prepareForm(model, "operacion/zona-form", new ZonaForm(), "Nueva zona", "/zonas"); }

    @GetMapping("/zonas/{id}/editar")
    public String editarZona(@PathVariable Long id, Model model) {
        Zona entity = catalogQueryService.zona(id);
        ZonaForm form = new ZonaForm();
        form.setId(entity.getId());
        form.setNombre(entity.getNombre());
        form.setDescripcion(entity.getDescripcion());
        form.setUbicacion(entity.getUbicacion());
        form.setCapacidadMaxima(entity.getCapacidadMaxima());
        form.setActiva(entity.getActiva());
        return prepareForm(model, "operacion/zona-form", form, "Editar zona", "/zonas/" + id);
    }

    @PostMapping("/zonas")
    public String crearZona(@Valid ZonaForm form, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/zona-form", form, "Nueva zona", "/zonas");
        }
        operacionManagementService.guardar(map(form, new Zona()));
        ra.addFlashAttribute("successMessage", "Zona creada.");
        return "redirect:/zonas";
    }

    @PostMapping("/zonas/{id}")
    public String actualizarZona(@PathVariable Long id, @Valid ZonaForm form, BindingResult bindingResult,
                                 Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/zona-form", form, "Editar zona", "/zonas/" + id);
        }
        operacionManagementService.guardar(map(form, catalogQueryService.zona(id)));
        ra.addFlashAttribute("successMessage", "Zona actualizada.");
        return "redirect:/zonas";
    }

    @PostMapping("/zonas/{id}/eliminar")
    public String eliminarZona(@PathVariable Long id, RedirectAttributes ra) {
        operacionManagementService.eliminarZona(id);
        ra.addFlashAttribute("successMessage", "Zona eliminada.");
        return "redirect:/zonas";
    }

    @GetMapping("/turnos")
    public String turnos(Model model) { addShared(model); return "operacion/turnos"; }

    @GetMapping("/turnos/nuevo")
    public String nuevoTurno(Model model) {
        TurnoForm form = new TurnoForm();
        form.setFecha(LocalDate.now());
        return prepareForm(model, "operacion/turno-form", form, "Nuevo turno", "/turnos");
    }

    @GetMapping("/turnos/{id}/editar")
    public String editarTurno(@PathVariable Long id, Model model) {
        Turno entity = catalogQueryService.turno(id);
        TurnoForm form = new TurnoForm();
        form.setId(entity.getId());
        form.setDocenteId(entity.getDocente().getId());
        form.setZonaId(entity.getZona().getId());
        form.setFecha(entity.getFecha());
        form.setHoraInicio(entity.getHoraInicio());
        form.setHoraFin(entity.getHoraFin());
        form.setFranja(entity.getFranja());
        form.setEstado(entity.getEstado());
        form.setAbiertoEn(entity.getAbiertoEn());
        form.setCerradoEn(entity.getCerradoEn());
        return prepareForm(model, "operacion/turno-form", form, "Editar turno", "/turnos/" + id);
    }

    @PostMapping("/turnos")
    public String crearTurno(@Valid TurnoForm form, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/turno-form", form, "Nuevo turno", "/turnos");
        }
        operacionManagementService.guardar(map(form, new Turno()));
        ra.addFlashAttribute("successMessage", "Turno creado.");
        return "redirect:/turnos";
    }

    @PostMapping("/turnos/{id}")
    public String actualizarTurno(@PathVariable Long id, @Valid TurnoForm form, BindingResult bindingResult,
                                  Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "operacion/turno-form", form, "Editar turno", "/turnos/" + id);
        }
        operacionManagementService.guardar(map(form, catalogQueryService.turno(id)));
        ra.addFlashAttribute("successMessage", "Turno actualizado.");
        return "redirect:/turnos";
    }

    @PostMapping("/turnos/{id}/eliminar")
    public String eliminarTurno(@PathVariable Long id, RedirectAttributes ra) {
        operacionManagementService.eliminarTurno(id);
        ra.addFlashAttribute("successMessage", "Turno eliminado.");
        return "redirect:/turnos";
    }

    private Zona map(ZonaForm form, Zona entity) {
        entity.setNombre(form.getNombre());
        entity.setDescripcion(form.getDescripcion() != null ? form.getDescripcion() : "");
        entity.setUbicacion(form.getUbicacion());
        entity.setCapacidadMaxima(form.getCapacidadMaxima());
        entity.setActiva(form.getActiva());
        return entity;
    }

    private Turno map(TurnoForm form, Turno entity) {
        entity.setDocente(catalogQueryService.docente(form.getDocenteId()));
        entity.setZona(catalogQueryService.zona(form.getZonaId()));
        entity.setFecha(form.getFecha());
        entity.setHoraInicio(form.getHoraInicio());
        entity.setHoraFin(form.getHoraFin());
        entity.setFranja(form.getFranja());
        entity.setEstado(form.getEstado());
        entity.setAbiertoEn(form.getAbiertoEn());
        entity.setCerradoEn(form.getCerradoEn());
        return entity;
    }
}
