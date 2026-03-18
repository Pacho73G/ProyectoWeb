package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Administrador;
import com.example.demo.model.Coordinador;
import com.example.demo.model.Docente;
import com.example.demo.model.RolUsuario;
import com.example.demo.service.CatalogQueryService;
import com.example.demo.service.UsuarioManagementService;
import com.example.demo.web.form.AdministradorForm;
import com.example.demo.web.form.CoordinadorForm;
import com.example.demo.web.form.DocenteForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping
/**
 * CRUD separado por subtipo para docentes, coordinadores y administradores.
 */
public class PerfilController extends BaseController {

    private final UsuarioManagementService usuarioManagementService;

    public PerfilController(CatalogQueryService catalogQueryService, UsuarioManagementService usuarioManagementService) {
        super(catalogQueryService);
        this.usuarioManagementService = usuarioManagementService;
    }

    @GetMapping("/docentes")
    public String docentes(Model model) { addShared(model); return "usuarios/docentes"; }

    @GetMapping("/docentes/nuevo")
    public String nuevoDocente(Model model) { return prepareForm(model, "usuarios/docente-form", new DocenteForm(), "Nuevo docente", "/docentes"); }

    @GetMapping("/docentes/{id}/editar")
    public String editarDocente(@PathVariable Long id, Model model) {
        Docente entity = catalogQueryService.docente(id);
        DocenteForm form = new DocenteForm();
        form.setId(entity.getId());
        form.setNombre(entity.getNombre());
        form.setEmail(entity.getEmail());
        form.setPasswordHash(entity.getPasswordHash());
        form.setActivo(entity.getActivo());
        form.setMaterias(entity.getMaterias());
        form.setCargaActual(entity.getCargaActual());
        form.setPuntajeGamificacion(entity.getPuntajeGamificacion());
        return prepareForm(model, "usuarios/docente-form", form, "Editar docente", "/docentes/" + id);
    }

    @PostMapping("/docentes")
    public String crearDocente(@Valid DocenteForm form, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "usuarios/docente-form", form, "Nuevo docente", "/docentes");
        }
        usuarioManagementService.guardar(map(form, new Docente()));
        ra.addFlashAttribute("successMessage", "Docente creado.");
        return "redirect:/docentes";
    }

    @PostMapping("/docentes/{id}")
    public String actualizarDocente(@PathVariable Long id, @Valid DocenteForm form, BindingResult bindingResult,
                                    Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "usuarios/docente-form", form, "Editar docente", "/docentes/" + id);
        }
        usuarioManagementService.guardar(map(form, catalogQueryService.docente(id)));
        ra.addFlashAttribute("successMessage", "Docente actualizado.");
        return "redirect:/docentes";
    }

    @PostMapping("/docentes/{id}/eliminar")
    public String eliminarDocente(@PathVariable Long id, RedirectAttributes ra) {
        usuarioManagementService.eliminarDocente(id);
        ra.addFlashAttribute("successMessage", "Docente eliminado.");
        return "redirect:/docentes";
    }

    @GetMapping("/coordinadores")
    public String coordinadores(Model model) { addShared(model); return "usuarios/coordinadores"; }

    @GetMapping("/coordinadores/nuevo")
    public String nuevoCoordinador(Model model) { return prepareForm(model, "usuarios/coordinador-form", new CoordinadorForm(), "Nuevo coordinador", "/coordinadores"); }

    @GetMapping("/coordinadores/{id}/editar")
    public String editarCoordinador(@PathVariable Long id, Model model) {
        Coordinador entity = catalogQueryService.coordinador(id);
        CoordinadorForm form = new CoordinadorForm();
        form.setId(entity.getId());
        form.setNombre(entity.getNombre());
        form.setEmail(entity.getEmail());
        form.setPasswordHash(entity.getPasswordHash());
        form.setActivo(entity.getActivo());
        form.setNivel(entity.getNivel());
        return prepareForm(model, "usuarios/coordinador-form", form, "Editar coordinador", "/coordinadores/" + id);
    }

    @PostMapping("/coordinadores")
    public String crearCoordinador(@Valid CoordinadorForm form, BindingResult bindingResult, Model model,
                                   RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "usuarios/coordinador-form", form, "Nuevo coordinador", "/coordinadores");
        }
        usuarioManagementService.guardar(map(form, new Coordinador()));
        ra.addFlashAttribute("successMessage", "Coordinador creado.");
        return "redirect:/coordinadores";
    }

    @PostMapping("/coordinadores/{id}")
    public String actualizarCoordinador(@PathVariable Long id, @Valid CoordinadorForm form, BindingResult bindingResult,
                                        Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "usuarios/coordinador-form", form, "Editar coordinador", "/coordinadores/" + id);
        }
        usuarioManagementService.guardar(map(form, catalogQueryService.coordinador(id)));
        ra.addFlashAttribute("successMessage", "Coordinador actualizado.");
        return "redirect:/coordinadores";
    }

    @PostMapping("/coordinadores/{id}/eliminar")
    public String eliminarCoordinador(@PathVariable Long id, RedirectAttributes ra) {
        usuarioManagementService.eliminarCoordinador(id);
        ra.addFlashAttribute("successMessage", "Coordinador eliminado.");
        return "redirect:/coordinadores";
    }

    @GetMapping("/administradores")
    public String administradores(Model model) { addShared(model); return "usuarios/administradores"; }

    @GetMapping("/administradores/nuevo")
    public String nuevoAdministrador(Model model) { return prepareForm(model, "usuarios/administrador-form", new AdministradorForm(), "Nuevo administrador", "/administradores"); }

    @GetMapping("/administradores/{id}/editar")
    public String editarAdministrador(@PathVariable Long id, Model model) {
        Administrador entity = catalogQueryService.administrador(id);
        AdministradorForm form = new AdministradorForm();
        form.setId(entity.getId());
        form.setNombre(entity.getNombre());
        form.setEmail(entity.getEmail());
        form.setPasswordHash(entity.getPasswordHash());
        form.setActivo(entity.getActivo());
        form.setCargo(entity.getCargo());
        return prepareForm(model, "usuarios/administrador-form", form, "Editar administrador", "/administradores/" + id);
    }

    @PostMapping("/administradores")
    public String crearAdministrador(@Valid AdministradorForm form, BindingResult bindingResult, Model model,
                                     RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "usuarios/administrador-form", form, "Nuevo administrador", "/administradores");
        }
        usuarioManagementService.guardar(map(form, new Administrador()));
        ra.addFlashAttribute("successMessage", "Administrador creado.");
        return "redirect:/administradores";
    }

    @PostMapping("/administradores/{id}")
    public String actualizarAdministrador(@PathVariable Long id, @Valid AdministradorForm form, BindingResult bindingResult,
                                          Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model, "usuarios/administrador-form", form, "Editar administrador", "/administradores/" + id);
        }
        usuarioManagementService.guardar(map(form, catalogQueryService.administrador(id)));
        ra.addFlashAttribute("successMessage", "Administrador actualizado.");
        return "redirect:/administradores";
    }

    @PostMapping("/administradores/{id}/eliminar")
    public String eliminarAdministrador(@PathVariable Long id, RedirectAttributes ra) {
        usuarioManagementService.eliminarAdministrador(id);
        ra.addFlashAttribute("successMessage", "Administrador eliminado.");
        return "redirect:/administradores";
    }

    private Docente map(DocenteForm form, Docente entity) {
        entity.setNombre(form.getNombre());
        entity.setEmail(form.getEmail());
        entity.setPasswordHash(form.getPasswordHash());
        entity.setActivo(form.getActivo());
        entity.setMaterias(form.getMaterias());
        entity.setCargaActual(form.getCargaActual());
        entity.setPuntajeGamificacion(form.getPuntajeGamificacion());
        entity.setRol(RolUsuario.DOCENTE);
        return entity;
    }

    private Coordinador map(CoordinadorForm form, Coordinador entity) {
        entity.setNombre(form.getNombre());
        entity.setEmail(form.getEmail());
        entity.setPasswordHash(form.getPasswordHash());
        entity.setActivo(form.getActivo());
        entity.setNivel(form.getNivel());
        entity.setRol(RolUsuario.COORDINADOR);
        return entity;
    }

    private Administrador map(AdministradorForm form, Administrador entity) {
        entity.setNombre(form.getNombre());
        entity.setEmail(form.getEmail());
        entity.setPasswordHash(form.getPasswordHash());
        entity.setActivo(form.getActivo());
        entity.setCargo(form.getCargo());
        entity.setRol(RolUsuario.ADMINISTRADOR);
        return entity;
    }
}
