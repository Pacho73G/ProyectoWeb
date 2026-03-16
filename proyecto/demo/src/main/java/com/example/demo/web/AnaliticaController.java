package com.example.demo.web;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.CheckpointRecorrido;
import com.example.demo.model.MapaCalor;
import com.example.demo.model.MetricaDocente;
import com.example.demo.model.Reconocimiento;
import com.example.demo.model.Recorrido;
import com.example.demo.service.SistemaService;
import com.example.demo.web.form.CheckpointRecorridoForm;
import com.example.demo.web.form.MapaCalorForm;
import com.example.demo.web.form.MetricaDocenteForm;
import com.example.demo.web.form.ReconocimientoForm;
import com.example.demo.web.form.RecorridoForm;

@Controller
@RequestMapping
public class AnaliticaController extends BaseController {

    public AnaliticaController(SistemaService sistemaService) {
        super(sistemaService);
    }

    @GetMapping("/recorridos")
    public String recorridos(Model model) { addShared(model); return "analitica/recorridos"; }
    @GetMapping("/checkpoints")
    public String checkpoints(Model model) { addShared(model); return "analitica/checkpoints"; }
    @GetMapping("/mapas-calor")
    public String mapasCalor(Model model) { addShared(model); return "analitica/mapas-calor"; }
    @GetMapping("/metricas")
    public String metricas(Model model) { addShared(model); return "analitica/metricas"; }
    @GetMapping("/reconocimientos")
    public String reconocimientos(Model model) { addShared(model); return "analitica/reconocimientos"; }

    @GetMapping("/recorridos/nuevo")
    public String nuevoRecorrido(Model model) { addShared(model); RecorridoForm f=new RecorridoForm(); f.setIniciadoEn(LocalDateTime.now()); model.addAttribute("form", f); model.addAttribute("titulo", "Nuevo recorrido"); model.addAttribute("accion", "/recorridos"); return "analitica/recorrido-form"; }
    @GetMapping("/checkpoints/nuevo")
    public String nuevoCheckpoint(Model model) { addShared(model); CheckpointRecorridoForm f=new CheckpointRecorridoForm(); f.setEscaneadoEn(LocalDateTime.now()); model.addAttribute("form", f); model.addAttribute("titulo", "Nuevo checkpoint"); model.addAttribute("accion", "/checkpoints"); return "analitica/checkpoint-form"; }
    @GetMapping("/mapas-calor/nuevo")
    public String nuevoMapa(Model model) { addShared(model); MapaCalorForm f=new MapaCalorForm(); f.setPeriodoInicio(LocalDate.now().minusDays(30)); f.setPeriodoFin(LocalDate.now()); model.addAttribute("form", f); model.addAttribute("titulo", "Nuevo mapa de calor"); model.addAttribute("accion", "/mapas-calor"); return "analitica/mapa-form"; }
    @GetMapping("/metricas/nuevo")
    public String nuevaMetrica(Model model) { addShared(model); model.addAttribute("form", new MetricaDocenteForm()); model.addAttribute("titulo", "Nueva métrica"); model.addAttribute("accion", "/metricas"); return "analitica/metrica-form"; }
    @GetMapping("/reconocimientos/nuevo")
    public String nuevoReconocimiento(Model model) { addShared(model); ReconocimientoForm f=new ReconocimientoForm(); f.setOtorgadoEn(LocalDate.now()); model.addAttribute("form", f); model.addAttribute("titulo", "Nuevo reconocimiento"); model.addAttribute("accion", "/reconocimientos"); return "analitica/reconocimiento-form"; }

    @GetMapping("/recorridos/{id}/editar")
    public String editarRecorrido(@PathVariable Long id, Model model) { Recorrido e=sistemaService.recorrido(id); RecorridoForm f=new RecorridoForm(); f.setId(e.getId()); f.setDocenteId(e.getDocente().getId()); f.setTurnoId(e.getTurno().getId()); f.setIniciadoEn(e.getIniciadoEn()); f.setFinalizadoEn(e.getFinalizadoEn()); f.setEstado(e.getEstado()); f.setDuracionMinutos(e.getDuracionMinutos()); addShared(model); model.addAttribute("form", f); model.addAttribute("titulo", "Editar recorrido"); model.addAttribute("accion", "/recorridos/"+id); return "analitica/recorrido-form"; }
    @GetMapping("/checkpoints/{id}/editar")
    public String editarCheckpoint(@PathVariable Long id, Model model) { CheckpointRecorrido e=sistemaService.checkpoint(id); CheckpointRecorridoForm f=new CheckpointRecorridoForm(); f.setId(e.getId()); f.setZonaId(e.getZona().getId()); f.setRecorridoId(e.getRecorrido().getId()); f.setCodigoQR(e.getCodigoQR()); f.setDescripcion(e.getDescripcion()); f.setOrden(e.getOrden()); f.setEscaneadoEn(e.getEscaneadoEn()); addShared(model); model.addAttribute("form", f); model.addAttribute("titulo", "Editar checkpoint"); model.addAttribute("accion", "/checkpoints/"+id); return "analitica/checkpoint-form"; }
    @GetMapping("/mapas-calor/{id}/editar")
    public String editarMapa(@PathVariable Long id, Model model) { MapaCalor e=sistemaService.mapaCalor(id); MapaCalorForm f=new MapaCalorForm(); f.setId(e.getId()); f.setZonaId(e.getZona().getId()); f.setFranja(e.getFranja()); f.setTipoIncidente(e.getTipoIncidente()); f.setTotalIncidentes(e.getTotalIncidentes()); f.setPorcentaje(e.getPorcentaje()); f.setPeriodoInicio(e.getPeriodoInicio()); f.setPeriodoFin(e.getPeriodoFin()); addShared(model); model.addAttribute("form", f); model.addAttribute("titulo", "Editar mapa de calor"); model.addAttribute("accion", "/mapas-calor/"+id); return "analitica/mapa-form"; }
    @GetMapping("/metricas/{id}/editar")
    public String editarMetrica(@PathVariable Long id, Model model) { MetricaDocente e=sistemaService.metrica(id); MetricaDocenteForm f=new MetricaDocenteForm(); f.setId(e.getId()); f.setDocenteId(e.getDocente().getId()); f.setPuntualidad(e.getPuntualidad()); f.setCobertura(e.getCobertura()); f.setRetrasos(e.getRetrasos()); f.setRecorridosCompletados(e.getRecorridosCompletados()); f.setIncidentesRegistrados(e.getIncidentesRegistrados()); f.setReasignacionesAceptadas(e.getReasignacionesAceptadas()); f.setPuntajeTotal(e.getPuntajeTotal()); f.setPeriodo(e.getPeriodo()); addShared(model); model.addAttribute("form", f); model.addAttribute("titulo", "Editar métrica"); model.addAttribute("accion", "/metricas/"+id); return "analitica/metrica-form"; }
    @GetMapping("/reconocimientos/{id}/editar")
    public String editarReconocimiento(@PathVariable Long id, Model model) { Reconocimiento e=sistemaService.reconocimiento(id); ReconocimientoForm f=new ReconocimientoForm(); f.setId(e.getId()); f.setMetricaDocenteId(e.getMetricaDocente().getId()); f.setTitulo(e.getTitulo()); f.setDescripcion(e.getDescripcion()); f.setTipo(e.getTipo()); f.setOtorgadoEn(e.getOtorgadoEn()); f.setTrimestre(e.getTrimestre()); addShared(model); model.addAttribute("form", f); model.addAttribute("titulo", "Editar reconocimiento"); model.addAttribute("accion", "/reconocimientos/"+id); return "analitica/reconocimiento-form"; }

    @PostMapping("/recorridos") public String crearRecorrido(RecorridoForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,new Recorrido())); ra.addFlashAttribute("successMessage","Recorrido creado."); return "redirect:/recorridos"; }
    @PostMapping("/recorridos/{id}") public String actualizarRecorrido(@PathVariable Long id, RecorridoForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,sistemaService.recorrido(id))); ra.addFlashAttribute("successMessage","Recorrido actualizado."); return "redirect:/recorridos"; }
    @PostMapping("/recorridos/{id}/eliminar") public String eliminarRecorrido(@PathVariable Long id, RedirectAttributes ra){ sistemaService.eliminarRecorrido(id); ra.addFlashAttribute("successMessage","Recorrido eliminado."); return "redirect:/recorridos"; }
    @PostMapping("/checkpoints") public String crearCheckpoint(CheckpointRecorridoForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,new CheckpointRecorrido())); ra.addFlashAttribute("successMessage","Checkpoint creado."); return "redirect:/checkpoints"; }
    @PostMapping("/checkpoints/{id}") public String actualizarCheckpoint(@PathVariable Long id, CheckpointRecorridoForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,sistemaService.checkpoint(id))); ra.addFlashAttribute("successMessage","Checkpoint actualizado."); return "redirect:/checkpoints"; }
    @PostMapping("/checkpoints/{id}/eliminar") public String eliminarCheckpoint(@PathVariable Long id, RedirectAttributes ra){ sistemaService.eliminarCheckpoint(id); ra.addFlashAttribute("successMessage","Checkpoint eliminado."); return "redirect:/checkpoints"; }
    @PostMapping("/mapas-calor") public String crearMapa(MapaCalorForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,new MapaCalor())); ra.addFlashAttribute("successMessage","Mapa de calor creado."); return "redirect:/mapas-calor"; }
    @PostMapping("/mapas-calor/{id}") public String actualizarMapa(@PathVariable Long id, MapaCalorForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,sistemaService.mapaCalor(id))); ra.addFlashAttribute("successMessage","Mapa de calor actualizado."); return "redirect:/mapas-calor"; }
    @PostMapping("/mapas-calor/{id}/eliminar") public String eliminarMapa(@PathVariable Long id, RedirectAttributes ra){ sistemaService.eliminarMapaCalor(id); ra.addFlashAttribute("successMessage","Mapa de calor eliminado."); return "redirect:/mapas-calor"; }
    @PostMapping("/metricas") public String crearMetrica(MetricaDocenteForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,new MetricaDocente())); ra.addFlashAttribute("successMessage","Métrica creada."); return "redirect:/metricas"; }
    @PostMapping("/metricas/{id}") public String actualizarMetrica(@PathVariable Long id, MetricaDocenteForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,sistemaService.metrica(id))); ra.addFlashAttribute("successMessage","Métrica actualizada."); return "redirect:/metricas"; }
    @PostMapping("/metricas/{id}/eliminar") public String eliminarMetrica(@PathVariable Long id, RedirectAttributes ra){ sistemaService.eliminarMetrica(id); ra.addFlashAttribute("successMessage","Métrica eliminada."); return "redirect:/metricas"; }
    @PostMapping("/reconocimientos") public String crearReconocimiento(ReconocimientoForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,new Reconocimiento())); ra.addFlashAttribute("successMessage","Reconocimiento creado."); return "redirect:/reconocimientos"; }
    @PostMapping("/reconocimientos/{id}") public String actualizarReconocimiento(@PathVariable Long id, ReconocimientoForm f, RedirectAttributes ra){ sistemaService.guardar(map(f,sistemaService.reconocimiento(id))); ra.addFlashAttribute("successMessage","Reconocimiento actualizado."); return "redirect:/reconocimientos"; }
    @PostMapping("/reconocimientos/{id}/eliminar") public String eliminarReconocimiento(@PathVariable Long id, RedirectAttributes ra){ sistemaService.eliminarReconocimiento(id); ra.addFlashAttribute("successMessage","Reconocimiento eliminado."); return "redirect:/reconocimientos"; }

    private Recorrido map(RecorridoForm f, Recorrido e){ e.setDocente(sistemaService.docente(f.getDocenteId())); e.setTurno(sistemaService.turno(f.getTurnoId())); e.setIniciadoEn(f.getIniciadoEn()); e.setFinalizadoEn(f.getFinalizadoEn()); e.setEstado(f.getEstado()); e.setDuracionMinutos(f.getDuracionMinutos()); return e; }
    private CheckpointRecorrido map(CheckpointRecorridoForm f, CheckpointRecorrido e){ e.setZona(sistemaService.zona(f.getZonaId())); e.setRecorrido(sistemaService.recorrido(f.getRecorridoId())); e.setCodigoQR(f.getCodigoQR()); e.setDescripcion(f.getDescripcion()); e.setOrden(f.getOrden()); e.setEscaneadoEn(f.getEscaneadoEn()); return e; }
    private MapaCalor map(MapaCalorForm f, MapaCalor e){ e.setZona(sistemaService.zona(f.getZonaId())); e.setFranja(f.getFranja()); e.setTipoIncidente(f.getTipoIncidente()); e.setTotalIncidentes(f.getTotalIncidentes()); e.setPorcentaje(f.getPorcentaje()); e.setPeriodoInicio(f.getPeriodoInicio()); e.setPeriodoFin(f.getPeriodoFin()); return e; }
    private MetricaDocente map(MetricaDocenteForm f, MetricaDocente e){ e.setDocente(sistemaService.docente(f.getDocenteId())); e.setPuntualidad(f.getPuntualidad()); e.setCobertura(f.getCobertura()); e.setRetrasos(f.getRetrasos()); e.setRecorridosCompletados(f.getRecorridosCompletados()); e.setIncidentesRegistrados(f.getIncidentesRegistrados()); e.setReasignacionesAceptadas(f.getReasignacionesAceptadas()); e.setPuntajeTotal(f.getPuntajeTotal()); e.setPeriodo(f.getPeriodo()); return e; }
    private Reconocimiento map(ReconocimientoForm f, Reconocimiento e){ e.setMetricaDocente(sistemaService.metrica(f.getMetricaDocenteId())); e.setTitulo(f.getTitulo()); e.setDescripcion(f.getDescripcion()); e.setTipo(f.getTipo()); e.setOtorgadoEn(f.getOtorgadoEn()); e.setTrimestre(f.getTrimestre()); return e; }
}
