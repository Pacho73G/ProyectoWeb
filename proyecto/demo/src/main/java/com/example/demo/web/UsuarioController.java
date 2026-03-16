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
import com.example.demo.model.Usuario;
import com.example.demo.service.CatalogQueryService;
import com.example.demo.service.UsuarioManagementService;
import com.example.demo.web.form.UsuarioForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController extends BaseController {

    private final UsuarioManagementService usuarioManagementService;

    public UsuarioController(CatalogQueryService catalogQueryService, UsuarioManagementService usuarioManagementService) {
        super(catalogQueryService);
        this.usuarioManagementService = usuarioManagementService;
    }

    @GetMapping
    public String usuarios(Model model) {
        addShared(model);
        return "usuarios/usuarios";
    }

    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("edicionRolBloqueada", false);
        return prepareForm(model, "usuarios/usuario-form", new UsuarioForm(), "Nuevo usuario", "/usuarios");
    }

    @GetMapping("/{id}/editar")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario entity = catalogQueryService.usuario(id);
        UsuarioForm form = new UsuarioForm();
        form.setId(entity.getId());
        form.setNombre(entity.getNombre());
        form.setEmail(entity.getEmail());
        form.setPasswordHash(entity.getPasswordHash());
        form.setActivo(entity.getActivo());
        form.setRol(entity.getRol());
        if (entity instanceof Docente docente) {
            form.setDescriptor(docente.getMaterias());
            form.setCargaActual(docente.getCargaActual());
            form.setPuntajeGamificacion(docente.getPuntajeGamificacion());
        } else if (entity instanceof Coordinador coordinador) {
            form.setDescriptor(coordinador.getNivel());
        } else if (entity instanceof Administrador administrador) {
            form.setDescriptor(administrador.getCargo());
        }
        model.addAttribute("edicionRolBloqueada", true);
        return prepareForm(model, "usuarios/usuario-form", form, "Editar usuario", "/usuarios/" + id);
    }

    @PostMapping
    public String crearUsuario(@Valid UsuarioForm form, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("edicionRolBloqueada", false);
            return prepareForm(model, "usuarios/usuario-form", form, "Nuevo usuario", "/usuarios");
        }
        usuarioManagementService.guardar(mapUsuario(form, null));
        ra.addFlashAttribute("successMessage", "Usuario creado.");
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}")
    public String actualizarUsuario(@PathVariable Long id, @Valid UsuarioForm form, BindingResult bindingResult,
                                    Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("edicionRolBloqueada", true);
            return prepareForm(model, "usuarios/usuario-form", form, "Editar usuario", "/usuarios/" + id);
        }
        usuarioManagementService.guardar(mapUsuario(form, catalogQueryService.usuario(id)));
        ra.addFlashAttribute("successMessage", "Usuario actualizado.");
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes ra) {
        usuarioManagementService.eliminarUsuario(id);
        ra.addFlashAttribute("successMessage", "Usuario eliminado.");
        return "redirect:/usuarios";
    }

    private Usuario mapUsuario(UsuarioForm form, Usuario existing) {
        Usuario entity = existing;
        if (entity == null) {
            entity = switch (form.getRol()) {
                case DOCENTE -> new Docente();
                case COORDINADOR -> new Coordinador();
                case ADMINISTRADOR -> new Administrador();
            };
        }
        entity.setNombre(form.getNombre());
        entity.setEmail(form.getEmail());
        entity.setPasswordHash(form.getPasswordHash());
        entity.setActivo(form.getActivo());
        entity.setRol(existing != null ? existing.getRol() : form.getRol());

        if (entity instanceof Docente docente) {
            docente.setMaterias(form.getDescriptor());
            docente.setCargaActual(form.getCargaActual());
            docente.setPuntajeGamificacion(form.getPuntajeGamificacion());
        } else if (entity instanceof Coordinador coordinador) {
            coordinador.setNivel(form.getDescriptor());
        } else if (entity instanceof Administrador administrador) {
            administrador.setCargo(form.getDescriptor());
        }
        return entity;
    }
}
