package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.ConfiguracionSistema;
import com.example.demo.service.CatalogQueryService;
import com.example.demo.service.UsuarioManagementService;
import com.example.demo.web.form.ConfiguracionSistemaForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/configuraciones")
public class ConfiguracionController extends BaseController {

    private final UsuarioManagementService usuarioManagementService;

    public ConfiguracionController(CatalogQueryService catalogQueryService, UsuarioManagementService usuarioManagementService) {
        super(catalogQueryService);
        this.usuarioManagementService = usuarioManagementService;
    }

    @GetMapping
    public String configuraciones(Model model) { addShared(model); return "usuarios/configuraciones"; }

    @GetMapping("/nuevo")
    public String nuevaConfiguracion(Model model) {
        return prepareForm(model, "usuarios/configuracion-form", new ConfiguracionSistemaForm(), "Nueva configuración", "/configuraciones");
    }

    @GetMapping("/{id}/editar")
    public String editarConfiguracion(@PathVariable Long id, Model model) {
        ConfiguracionSistema entity = catalogQueryService.configuracion(id);
        ConfiguracionSistemaForm form = new ConfiguracionSistemaForm();
        form.setId(entity.getId());
        form.setAdministradorId(entity.getAdministrador().getId());
        form.setMinutosAlertaAusencia(entity.getMinutosAlertaAusencia());
        form.setSegundosVentanaReasignacion(entity.getSegundosVentanaReasignacion());
        form.setMinutosInactividad(entity.getMinutosInactividad());
        form.setUmbralIngreso(entity.getUmbralIngreso());
        form.setMinutosRecordatorio1(entity.getMinutosRecordatorio1());
        form.setMinutosRecordatorio2(entity.getMinutosRecordatorio2());
        return prepareForm(model, "usuarios/configuracion-form", form, "Editar configuración", "/configuraciones/" + id);
    }

    @PostMapping
    public String crearConfiguracion(@Valid ConfiguracionSistemaForm form, BindingResult bindingResult, Model model,
                                     RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "usuarios/configuracion-form", form, "Nueva configuración", "/configuraciones");
        }
        usuarioManagementService.guardar(map(form, new ConfiguracionSistema()));
        ra.addFlashAttribute("successMessage", "Configuración creada.");
        return "redirect:/configuraciones";
    }

    @PostMapping("/{id}")
    public String actualizarConfiguracion(@PathVariable Long id, @Valid ConfiguracionSistemaForm form,
                                          BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "usuarios/configuracion-form", form, "Editar configuración", "/configuraciones/" + id);
        }
        usuarioManagementService.guardar(map(form, catalogQueryService.configuracion(id)));
        ra.addFlashAttribute("successMessage", "Configuración actualizada.");
        return "redirect:/configuraciones";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarConfiguracion(@PathVariable Long id, RedirectAttributes ra) {
        usuarioManagementService.eliminarConfiguracion(id);
        ra.addFlashAttribute("successMessage", "Configuración eliminada.");
        return "redirect:/configuraciones";
    }

    private ConfiguracionSistema map(ConfiguracionSistemaForm form, ConfiguracionSistema entity) {
        entity.setAdministrador(catalogQueryService.administrador(form.getAdministradorId()));
        entity.setMinutosAlertaAusencia(form.getMinutosAlertaAusencia());
        entity.setSegundosVentanaReasignacion(form.getSegundosVentanaReasignacion());
        entity.setMinutosInactividad(form.getMinutosInactividad());
        entity.setUmbralIngreso(form.getUmbralIngreso());
        entity.setMinutosRecordatorio1(form.getMinutosRecordatorio1());
        entity.setMinutosRecordatorio2(form.getMinutosRecordatorio2());
        return entity;
    }
}
