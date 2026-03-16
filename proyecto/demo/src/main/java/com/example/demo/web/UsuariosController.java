package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Administrador;
import com.example.demo.model.ConfiguracionSistema;
import com.example.demo.model.Coordinador;
import com.example.demo.model.Docente;
import com.example.demo.model.RolUsuario;
import com.example.demo.model.Usuario;
import com.example.demo.service.SistemaService;
import com.example.demo.web.form.AdministradorForm;
import com.example.demo.web.form.ConfiguracionSistemaForm;
import com.example.demo.web.form.CoordinadorForm;
import com.example.demo.web.form.DocenteForm;
import com.example.demo.web.form.UsuarioForm;

@Controller
@RequestMapping
public class UsuariosController extends BaseController {

    public UsuariosController(SistemaService sistemaService) {
        super(sistemaService);
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        addShared(model);
        return "usuarios/usuarios";
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuario(Model model) {
        addShared(model);
        model.addAttribute("form", new UsuarioForm());
        model.addAttribute("titulo", "Nuevo usuario");
        model.addAttribute("accion", "/usuarios");
        model.addAttribute("edicionRolBloqueada", false);
        return "usuarios/usuario-form";
    }

    @GetMapping("/usuarios/{id}/editar")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario entity = sistemaService.usuario(id);
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
        addShared(model);
        model.addAttribute("form", form);
        model.addAttribute("titulo", "Editar usuario");
        model.addAttribute("accion", "/usuarios/" + id);
        model.addAttribute("edicionRolBloqueada", true);
        return "usuarios/usuario-form";
    }

    @PostMapping("/usuarios")
    public String crearUsuario(UsuarioForm form, RedirectAttributes ra) {
        sistemaService.guardar(mapUsuario(form, null));
        ra.addFlashAttribute("successMessage", "Usuario creado.");
        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/{id}")
    public String actualizarUsuario(@PathVariable Long id, UsuarioForm form, RedirectAttributes ra) {
        sistemaService.guardar(mapUsuario(form, sistemaService.usuario(id)));
        ra.addFlashAttribute("successMessage", "Usuario actualizado.");
        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes ra) {
        sistemaService.eliminarUsuario(id);
        ra.addFlashAttribute("successMessage", "Usuario eliminado.");
        return "redirect:/usuarios";
    }

    @GetMapping("/docentes")
    public String docentes(Model model) {
        addShared(model);
        return "usuarios/docentes";
    }

    @GetMapping("/docentes/nuevo")
    public String nuevoDocente(Model model) {
        addShared(model);
        model.addAttribute("form", new DocenteForm());
        model.addAttribute("titulo", "Nuevo docente");
        model.addAttribute("accion", "/docentes");
        return "usuarios/docente-form";
    }

    @GetMapping("/docentes/{id}/editar")
    public String editarDocente(@PathVariable Long id, Model model) {
        Docente entity = sistemaService.docente(id);
        DocenteForm form = new DocenteForm();
        form.setId(entity.getId());
        form.setNombre(entity.getNombre());
        form.setEmail(entity.getEmail());
        form.setPasswordHash(entity.getPasswordHash());
        form.setActivo(entity.getActivo());
        form.setMaterias(entity.getMaterias());
        form.setCargaActual(entity.getCargaActual());
        form.setPuntajeGamificacion(entity.getPuntajeGamificacion());
        addShared(model);
        model.addAttribute("form", form);
        model.addAttribute("titulo", "Editar docente");
        model.addAttribute("accion", "/docentes/" + id);
        return "usuarios/docente-form";
    }

    @PostMapping("/docentes")
    public String crearDocente(DocenteForm form, RedirectAttributes ra) {
        sistemaService.guardar(map(form, new Docente()));
        ra.addFlashAttribute("successMessage", "Docente creado.");
        return "redirect:/docentes";
    }

    @PostMapping("/docentes/{id}")
    public String actualizarDocente(@PathVariable Long id, DocenteForm form, RedirectAttributes ra) {
        sistemaService.guardar(map(form, sistemaService.docente(id)));
        ra.addFlashAttribute("successMessage", "Docente actualizado.");
        return "redirect:/docentes";
    }

    @PostMapping("/docentes/{id}/eliminar")
    public String eliminarDocente(@PathVariable Long id, RedirectAttributes ra) {
        sistemaService.eliminarDocente(id);
        ra.addFlashAttribute("successMessage", "Docente eliminado.");
        return "redirect:/docentes";
    }

    @GetMapping("/coordinadores")
    public String coordinadores(Model model) {
        addShared(model);
        return "usuarios/coordinadores";
    }

    @GetMapping("/coordinadores/nuevo")
    public String nuevoCoordinador(Model model) {
        addShared(model);
        model.addAttribute("form", new CoordinadorForm());
        model.addAttribute("titulo", "Nuevo coordinador");
        model.addAttribute("accion", "/coordinadores");
        return "usuarios/coordinador-form";
    }

    @GetMapping("/coordinadores/{id}/editar")
    public String editarCoordinador(@PathVariable Long id, Model model) {
        Coordinador entity = sistemaService.coordinador(id);
        CoordinadorForm form = new CoordinadorForm();
        form.setId(entity.getId());
        form.setNombre(entity.getNombre());
        form.setEmail(entity.getEmail());
        form.setPasswordHash(entity.getPasswordHash());
        form.setActivo(entity.getActivo());
        form.setNivel(entity.getNivel());
        addShared(model);
        model.addAttribute("form", form);
        model.addAttribute("titulo", "Editar coordinador");
        model.addAttribute("accion", "/coordinadores/" + id);
        return "usuarios/coordinador-form";
    }

    @PostMapping("/coordinadores")
    public String crearCoordinador(CoordinadorForm form, RedirectAttributes ra) {
        sistemaService.guardar(map(form, new Coordinador()));
        ra.addFlashAttribute("successMessage", "Coordinador creado.");
        return "redirect:/coordinadores";
    }

    @PostMapping("/coordinadores/{id}")
    public String actualizarCoordinador(@PathVariable Long id, CoordinadorForm form, RedirectAttributes ra) {
        sistemaService.guardar(map(form, sistemaService.coordinador(id)));
        ra.addFlashAttribute("successMessage", "Coordinador actualizado.");
        return "redirect:/coordinadores";
    }

    @PostMapping("/coordinadores/{id}/eliminar")
    public String eliminarCoordinador(@PathVariable Long id, RedirectAttributes ra) {
        sistemaService.eliminarCoordinador(id);
        ra.addFlashAttribute("successMessage", "Coordinador eliminado.");
        return "redirect:/coordinadores";
    }

    @GetMapping("/administradores")
    public String administradores(Model model) {
        addShared(model);
        return "usuarios/administradores";
    }

    @GetMapping("/administradores/nuevo")
    public String nuevoAdministrador(Model model) {
        addShared(model);
        model.addAttribute("form", new AdministradorForm());
        model.addAttribute("titulo", "Nuevo administrador");
        model.addAttribute("accion", "/administradores");
        return "usuarios/administrador-form";
    }

    @GetMapping("/administradores/{id}/editar")
    public String editarAdministrador(@PathVariable Long id, Model model) {
        Administrador entity = sistemaService.administrador(id);
        AdministradorForm form = new AdministradorForm();
        form.setId(entity.getId());
        form.setNombre(entity.getNombre());
        form.setEmail(entity.getEmail());
        form.setPasswordHash(entity.getPasswordHash());
        form.setActivo(entity.getActivo());
        form.setCargo(entity.getCargo());
        addShared(model);
        model.addAttribute("form", form);
        model.addAttribute("titulo", "Editar administrador");
        model.addAttribute("accion", "/administradores/" + id);
        return "usuarios/administrador-form";
    }

    @PostMapping("/administradores")
    public String crearAdministrador(AdministradorForm form, RedirectAttributes ra) {
        sistemaService.guardar(map(form, new Administrador()));
        ra.addFlashAttribute("successMessage", "Administrador creado.");
        return "redirect:/administradores";
    }

    @PostMapping("/administradores/{id}")
    public String actualizarAdministrador(@PathVariable Long id, AdministradorForm form, RedirectAttributes ra) {
        sistemaService.guardar(map(form, sistemaService.administrador(id)));
        ra.addFlashAttribute("successMessage", "Administrador actualizado.");
        return "redirect:/administradores";
    }

    @PostMapping("/administradores/{id}/eliminar")
    public String eliminarAdministrador(@PathVariable Long id, RedirectAttributes ra) {
        sistemaService.eliminarAdministrador(id);
        ra.addFlashAttribute("successMessage", "Administrador eliminado.");
        return "redirect:/administradores";
    }

    @GetMapping("/configuraciones")
    public String configuraciones(Model model) {
        addShared(model);
        return "usuarios/configuraciones";
    }

    @GetMapping("/configuraciones/nuevo")
    public String nuevaConfiguracion(Model model) {
        addShared(model);
        model.addAttribute("form", new ConfiguracionSistemaForm());
        model.addAttribute("titulo", "Nueva configuración");
        model.addAttribute("accion", "/configuraciones");
        return "usuarios/configuracion-form";
    }

    @GetMapping("/configuraciones/{id}/editar")
    public String editarConfiguracion(@PathVariable Long id, Model model) {
        ConfiguracionSistema entity = sistemaService.configuracion(id);
        ConfiguracionSistemaForm form = new ConfiguracionSistemaForm();
        form.setId(entity.getId());
        form.setAdministradorId(entity.getAdministrador().getId());
        form.setMinutosAlertaAusencia(entity.getMinutosAlertaAusencia());
        form.setSegundosVentanaReasignacion(entity.getSegundosVentanaReasignacion());
        form.setMinutosInactividad(entity.getMinutosInactividad());
        form.setUmbralIngreso(entity.getUmbralIngreso());
        form.setMinutosRecordatorio1(entity.getMinutosRecordatorio1());
        form.setMinutosRecordatorio2(entity.getMinutosRecordatorio2());
        addShared(model);
        model.addAttribute("form", form);
        model.addAttribute("titulo", "Editar configuración");
        model.addAttribute("accion", "/configuraciones/" + id);
        return "usuarios/configuracion-form";
    }

    @PostMapping("/configuraciones")
    public String crearConfiguracion(ConfiguracionSistemaForm form, RedirectAttributes ra) {
        sistemaService.guardar(map(form, new ConfiguracionSistema()));
        ra.addFlashAttribute("successMessage", "Configuración creada.");
        return "redirect:/configuraciones";
    }

    @PostMapping("/configuraciones/{id}")
    public String actualizarConfiguracion(@PathVariable Long id, ConfiguracionSistemaForm form, RedirectAttributes ra) {
        sistemaService.guardar(map(form, sistemaService.configuracion(id)));
        ra.addFlashAttribute("successMessage", "Configuración actualizada.");
        return "redirect:/configuraciones";
    }

    @PostMapping("/configuraciones/{id}/eliminar")
    public String eliminarConfiguracion(@PathVariable Long id, RedirectAttributes ra) {
        sistemaService.eliminarConfiguracion(id);
        ra.addFlashAttribute("successMessage", "Configuración eliminada.");
        return "redirect:/configuraciones";
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

    private ConfiguracionSistema map(ConfiguracionSistemaForm form, ConfiguracionSistema entity) {
        entity.setAdministrador(sistemaService.administrador(form.getAdministradorId()));
        entity.setMinutosAlertaAusencia(form.getMinutosAlertaAusencia());
        entity.setSegundosVentanaReasignacion(form.getSegundosVentanaReasignacion());
        entity.setMinutosInactividad(form.getMinutosInactividad());
        entity.setUmbralIngreso(form.getUmbralIngreso());
        entity.setMinutosRecordatorio1(form.getMinutosRecordatorio1());
        entity.setMinutosRecordatorio2(form.getMinutosRecordatorio2());
        return entity;
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
