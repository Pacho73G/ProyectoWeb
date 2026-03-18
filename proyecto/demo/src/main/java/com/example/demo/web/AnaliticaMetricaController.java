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

import com.example.demo.model.MapaCalor;
import com.example.demo.model.MetricaDocente;
import com.example.demo.model.Reconocimiento;
import com.example.demo.service.AnaliticaManagementService;
import com.example.demo.service.CatalogQueryService;
import com.example.demo.web.form.MapaCalorForm;
import com.example.demo.web.form.MetricaDocenteForm;
import com.example.demo.web.form.ReconocimientoForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping
/**
 * CRUD de analítica preventiva y gamificación institucional.
 */
public class AnaliticaMetricaController extends BaseController {

    private final AnaliticaManagementService analiticaManagementService;

    public AnaliticaMetricaController(CatalogQueryService catalogQueryService, AnaliticaManagementService analiticaManagementService) {
        super(catalogQueryService);
        this.analiticaManagementService = analiticaManagementService;
    }

    @GetMapping("/mapas-calor")
    public String mapasCalor(Model model) { addShared(model); return "analitica/mapas-calor"; }

    @GetMapping("/mapas-calor/nuevo")
    public String nuevoMapa(Model model) {
        MapaCalorForm form = new MapaCalorForm();
        form.setPeriodoInicio(LocalDate.now().minusDays(30));
        form.setPeriodoFin(LocalDate.now());
        return prepareForm(model, "analitica/mapa-form", form, "Nuevo mapa de calor", "/mapas-calor");
    }

    @GetMapping("/mapas-calor/{id}/editar")
    public String editarMapa(@PathVariable Long id, Model model) {
        MapaCalor entity = catalogQueryService.mapaCalor(id);
        MapaCalorForm form = new MapaCalorForm();
        form.setId(entity.getId());
        form.setZonaId(entity.getZona().getId());
        form.setFranja(entity.getFranja());
        form.setTipoIncidente(entity.getTipoIncidente());
        form.setTotalIncidentes(entity.getTotalIncidentes());
        form.setPorcentaje(entity.getPorcentaje());
        form.setPeriodoInicio(entity.getPeriodoInicio());
        form.setPeriodoFin(entity.getPeriodoFin());
        return prepareForm(model, "analitica/mapa-form", form, "Editar mapa de calor", "/mapas-calor/" + id);
    }

    @PostMapping("/mapas-calor")
    public String crearMapa(@Valid MapaCalorForm form, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "analitica/mapa-form", form, "Nuevo mapa de calor", "/mapas-calor");
        }
        analiticaManagementService.guardar(map(form, new MapaCalor()));
        ra.addFlashAttribute("successMessage", "Mapa de calor creado.");
        return "redirect:/mapas-calor";
    }

    @PostMapping("/mapas-calor/{id}")
    public String actualizarMapa(@PathVariable Long id, @Valid MapaCalorForm form, BindingResult bindingResult,
                                 Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "analitica/mapa-form", form, "Editar mapa de calor", "/mapas-calor/" + id);
        }
        analiticaManagementService.guardar(map(form, catalogQueryService.mapaCalor(id)));
        ra.addFlashAttribute("successMessage", "Mapa de calor actualizado.");
        return "redirect:/mapas-calor";
    }

    @PostMapping("/mapas-calor/{id}/eliminar")
    public String eliminarMapa(@PathVariable Long id, RedirectAttributes ra) {
        analiticaManagementService.eliminarMapaCalor(id);
        ra.addFlashAttribute("successMessage", "Mapa de calor eliminado.");
        return "redirect:/mapas-calor";
    }

    @GetMapping("/metricas")
    public String metricas(Model model) { addShared(model); return "analitica/metricas"; }

    @GetMapping("/metricas/nuevo")
    public String nuevaMetrica(Model model) {
        return prepareForm(model, "analitica/metrica-form", new MetricaDocenteForm(), "Nueva métrica", "/metricas");
    }

    @GetMapping("/metricas/{id}/editar")
    public String editarMetrica(@PathVariable Long id, Model model) {
        MetricaDocente entity = catalogQueryService.metrica(id);
        MetricaDocenteForm form = new MetricaDocenteForm();
        form.setId(entity.getId());
        form.setDocenteId(entity.getDocente().getId());
        form.setPuntualidad(entity.getPuntualidad());
        form.setCobertura(entity.getCobertura());
        form.setRetrasos(entity.getRetrasos());
        form.setRecorridosCompletados(entity.getRecorridosCompletados());
        form.setIncidentesRegistrados(entity.getIncidentesRegistrados());
        form.setReasignacionesAceptadas(entity.getReasignacionesAceptadas());
        form.setPuntajeTotal(entity.getPuntajeTotal());
        form.setPeriodo(entity.getPeriodo());
        return prepareForm(model, "analitica/metrica-form", form, "Editar métrica", "/metricas/" + id);
    }

    @PostMapping("/metricas")
    public String crearMetrica(@Valid MetricaDocenteForm form, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "analitica/metrica-form", form, "Nueva métrica", "/metricas");
        }
        analiticaManagementService.guardar(map(form, new MetricaDocente()));
        ra.addFlashAttribute("successMessage", "Métrica creada.");
        return "redirect:/metricas";
    }

    @PostMapping("/metricas/{id}")
    public String actualizarMetrica(@PathVariable Long id, @Valid MetricaDocenteForm form, BindingResult bindingResult,
                                    Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "analitica/metrica-form", form, "Editar métrica", "/metricas/" + id);
        }
        analiticaManagementService.guardar(map(form, catalogQueryService.metrica(id)));
        ra.addFlashAttribute("successMessage", "Métrica actualizada.");
        return "redirect:/metricas";
    }

    @PostMapping("/metricas/{id}/eliminar")
    public String eliminarMetrica(@PathVariable Long id, RedirectAttributes ra) {
        analiticaManagementService.eliminarMetrica(id);
        ra.addFlashAttribute("successMessage", "Métrica eliminada.");
        return "redirect:/metricas";
    }

    @GetMapping("/reconocimientos")
    public String reconocimientos(Model model) { addShared(model); return "analitica/reconocimientos"; }

    @GetMapping("/reconocimientos/nuevo")
    public String nuevoReconocimiento(Model model) {
        ReconocimientoForm form = new ReconocimientoForm();
        form.setOtorgadoEn(LocalDate.now());
        return prepareForm(model, "analitica/reconocimiento-form", form, "Nuevo reconocimiento", "/reconocimientos");
    }

    @GetMapping("/reconocimientos/{id}/editar")
    public String editarReconocimiento(@PathVariable Long id, Model model) {
        Reconocimiento entity = catalogQueryService.reconocimiento(id);
        ReconocimientoForm form = new ReconocimientoForm();
        form.setId(entity.getId());
        form.setMetricaDocenteId(entity.getMetricaDocente().getId());
        form.setTitulo(entity.getTitulo());
        form.setDescripcion(entity.getDescripcion());
        form.setTipo(entity.getTipo());
        form.setOtorgadoEn(entity.getOtorgadoEn());
        form.setTrimestre(entity.getTrimestre());
        return prepareForm(model, "analitica/reconocimiento-form", form, "Editar reconocimiento", "/reconocimientos/" + id);
    }

    @PostMapping("/reconocimientos")
    public String crearReconocimiento(@Valid ReconocimientoForm form, BindingResult bindingResult, Model model,
                                      RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "analitica/reconocimiento-form", form, "Nuevo reconocimiento", "/reconocimientos");
        }
        analiticaManagementService.guardar(map(form, new Reconocimiento()));
        ra.addFlashAttribute("successMessage", "Reconocimiento creado.");
        return "redirect:/reconocimientos";
    }

    @PostMapping("/reconocimientos/{id}")
    public String actualizarReconocimiento(@PathVariable Long id, @Valid ReconocimientoForm form, BindingResult bindingResult,
                                           Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "analitica/reconocimiento-form", form, "Editar reconocimiento", "/reconocimientos/" + id);
        }
        analiticaManagementService.guardar(map(form, catalogQueryService.reconocimiento(id)));
        ra.addFlashAttribute("successMessage", "Reconocimiento actualizado.");
        return "redirect:/reconocimientos";
    }

    @PostMapping("/reconocimientos/{id}/eliminar")
    public String eliminarReconocimiento(@PathVariable Long id, RedirectAttributes ra) {
        analiticaManagementService.eliminarReconocimiento(id);
        ra.addFlashAttribute("successMessage", "Reconocimiento eliminado.");
        return "redirect:/reconocimientos";
    }

    private MapaCalor map(MapaCalorForm form, MapaCalor entity) {
        entity.setZona(catalogQueryService.zona(form.getZonaId()));
        entity.setFranja(form.getFranja());
        entity.setTipoIncidente(form.getTipoIncidente());
        entity.setTotalIncidentes(form.getTotalIncidentes());
        entity.setPorcentaje(form.getPorcentaje());
        entity.setPeriodoInicio(form.getPeriodoInicio());
        entity.setPeriodoFin(form.getPeriodoFin());
        return entity;
    }

    private MetricaDocente map(MetricaDocenteForm form, MetricaDocente entity) {
        entity.setDocente(catalogQueryService.docente(form.getDocenteId()));
        entity.setPuntualidad(form.getPuntualidad());
        entity.setCobertura(form.getCobertura());
        entity.setRetrasos(form.getRetrasos());
        entity.setRecorridosCompletados(form.getRecorridosCompletados());
        entity.setIncidentesRegistrados(form.getIncidentesRegistrados());
        entity.setReasignacionesAceptadas(form.getReasignacionesAceptadas());
        entity.setPuntajeTotal(form.getPuntajeTotal());
        entity.setPeriodo(form.getPeriodo());
        return entity;
    }

    private Reconocimiento map(ReconocimientoForm form, Reconocimiento entity) {
        entity.setMetricaDocente(catalogQueryService.metrica(form.getMetricaDocenteId()));
        entity.setTitulo(form.getTitulo());
        entity.setDescripcion(form.getDescripcion() != null ? form.getDescripcion() : "");
        entity.setTipo(form.getTipo());
        entity.setOtorgadoEn(form.getOtorgadoEn());
        entity.setTrimestre(form.getTrimestre());
        return entity;
    }
}
