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

import com.example.demo.model.CheckpointRecorrido;
import com.example.demo.model.Recorrido;
import com.example.demo.service.AnaliticaManagementService;
import com.example.demo.service.CatalogQueryService;
import com.example.demo.web.form.CheckpointRecorridoForm;
import com.example.demo.web.form.RecorridoForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping
/**
 * CRUD de vigilancia activa: recorridos y checkpoints de validación.
 */
public class RecorridoCheckpointController extends BaseController {

    private final AnaliticaManagementService analiticaManagementService;

    public RecorridoCheckpointController(CatalogQueryService catalogQueryService, AnaliticaManagementService analiticaManagementService) {
        super(catalogQueryService);
        this.analiticaManagementService = analiticaManagementService;
    }

    @GetMapping("/recorridos")
    public String recorridos(Model model) { addShared(model); return "analitica/recorridos"; }

    @GetMapping("/recorridos/nuevo")
    public String nuevoRecorrido(Model model) {
        RecorridoForm form = new RecorridoForm();
        form.setIniciadoEn(LocalDateTime.now());
        return prepareForm(model, "analitica/recorrido-form", form, "Nuevo recorrido", "/recorridos");
    }

    @GetMapping("/recorridos/{id}/editar")
    public String editarRecorrido(@PathVariable Long id, Model model) {
        Recorrido entity = catalogQueryService.recorrido(id);
        RecorridoForm form = new RecorridoForm();
        form.setId(entity.getId());
        form.setDocenteId(entity.getDocente().getId());
        form.setTurnoId(entity.getTurno().getId());
        form.setIniciadoEn(entity.getIniciadoEn());
        form.setFinalizadoEn(entity.getFinalizadoEn());
        form.setEstado(entity.getEstado());
        form.setDuracionMinutos(entity.getDuracionMinutos());
        return prepareForm(model, "analitica/recorrido-form", form, "Editar recorrido", "/recorridos/" + id);
    }

    @PostMapping("/recorridos")
    public String crearRecorrido(@Valid RecorridoForm form, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "analitica/recorrido-form", form, "Nuevo recorrido", "/recorridos");
        }
        analiticaManagementService.guardar(map(form, new Recorrido()));
        ra.addFlashAttribute("successMessage", "Recorrido creado.");
        return "redirect:/recorridos";
    }

    @PostMapping("/recorridos/{id}")
    public String actualizarRecorrido(@PathVariable Long id, @Valid RecorridoForm form, BindingResult bindingResult,
                                      Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "analitica/recorrido-form", form, "Editar recorrido", "/recorridos/" + id);
        }
        analiticaManagementService.guardar(map(form, catalogQueryService.recorrido(id)));
        ra.addFlashAttribute("successMessage", "Recorrido actualizado.");
        return "redirect:/recorridos";
    }

    @PostMapping("/recorridos/{id}/eliminar")
    public String eliminarRecorrido(@PathVariable Long id, RedirectAttributes ra) {
        analiticaManagementService.eliminarRecorrido(id);
        ra.addFlashAttribute("successMessage", "Recorrido eliminado.");
        return "redirect:/recorridos";
    }

    @GetMapping("/checkpoints")
    public String checkpoints(Model model) { addShared(model); return "analitica/checkpoints"; }

    @GetMapping("/checkpoints/nuevo")
    public String nuevoCheckpoint(Model model) {
        CheckpointRecorridoForm form = new CheckpointRecorridoForm();
        form.setEscaneadoEn(LocalDateTime.now());
        return prepareForm(model, "analitica/checkpoint-form", form, "Nuevo checkpoint", "/checkpoints");
    }

    @GetMapping("/checkpoints/{id}/editar")
    public String editarCheckpoint(@PathVariable Long id, Model model) {
        CheckpointRecorrido entity = catalogQueryService.checkpoint(id);
        CheckpointRecorridoForm form = new CheckpointRecorridoForm();
        form.setId(entity.getId());
        form.setZonaId(entity.getZona().getId());
        form.setRecorridoId(entity.getRecorrido().getId());
        form.setCodigoQR(entity.getCodigoQR());
        form.setDescripcion(entity.getDescripcion());
        form.setOrden(entity.getOrden());
        form.setEscaneadoEn(entity.getEscaneadoEn());
        return prepareForm(model, "analitica/checkpoint-form", form, "Editar checkpoint", "/checkpoints/" + id);
    }

    @PostMapping("/checkpoints")
    public String crearCheckpoint(@Valid CheckpointRecorridoForm form, BindingResult bindingResult, Model model,
                                  RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "analitica/checkpoint-form", form, "Nuevo checkpoint", "/checkpoints");
        }
        analiticaManagementService.guardar(map(form, new CheckpointRecorrido()));
        ra.addFlashAttribute("successMessage", "Checkpoint creado.");
        return "redirect:/checkpoints";
    }

    @PostMapping("/checkpoints/{id}")
    public String actualizarCheckpoint(@PathVariable Long id, @Valid CheckpointRecorridoForm form, BindingResult bindingResult,
                                       Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "analitica/checkpoint-form", form, "Editar checkpoint", "/checkpoints/" + id);
        }
        analiticaManagementService.guardar(map(form, catalogQueryService.checkpoint(id)));
        ra.addFlashAttribute("successMessage", "Checkpoint actualizado.");
        return "redirect:/checkpoints";
    }

    @PostMapping("/checkpoints/{id}/eliminar")
    public String eliminarCheckpoint(@PathVariable Long id, RedirectAttributes ra) {
        analiticaManagementService.eliminarCheckpoint(id);
        ra.addFlashAttribute("successMessage", "Checkpoint eliminado.");
        return "redirect:/checkpoints";
    }

    private Recorrido map(RecorridoForm form, Recorrido entity) {
        entity.setDocente(catalogQueryService.docente(form.getDocenteId()));
        entity.setTurno(catalogQueryService.turno(form.getTurnoId()));
        entity.setIniciadoEn(form.getIniciadoEn());
        entity.setFinalizadoEn(form.getFinalizadoEn());
        entity.setEstado(form.getEstado());
        entity.setDuracionMinutos(form.getDuracionMinutos());
        return entity;
    }

    private CheckpointRecorrido map(CheckpointRecorridoForm form, CheckpointRecorrido entity) {
        entity.setZona(catalogQueryService.zona(form.getZonaId()));
        entity.setRecorrido(catalogQueryService.recorrido(form.getRecorridoId()));
        entity.setCodigoQR(form.getCodigoQR());
        entity.setDescripcion(form.getDescripcion() != null ? form.getDescripcion() : "");
        entity.setOrden(form.getOrden());
        entity.setEscaneadoEn(form.getEscaneadoEn());
        return entity;
    }
}
