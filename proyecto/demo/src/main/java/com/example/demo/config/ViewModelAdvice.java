package com.example.demo.config;

import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.service.SistemaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class ViewModelAdvice {

    private final SistemaService sistemaService;

    @ModelAttribute("globalStats")
    public GlobalStats globalStats() {
        return new GlobalStats(
                sistemaService.totalDocentes(),
                sistemaService.totalTurnos(),
                sistemaService.totalIncidentes(),
                sistemaService.totalReasignaciones(),
                sistemaService.totalRecorridos(),
                sistemaService.totalReconocimientos());
    }

    @ModelAttribute("rolActivo")
    public String rolActivo(HttpSession session) {
        Object value = session.getAttribute("rolActivo");
        return value != null ? value.toString() : "coordinador";
    }

    @ModelAttribute("currentPath")
    public String currentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("errorMessage")
    public String errorMessage(HttpServletRequest request) {
        String error = request.getParameter("error");
        return error != null && !error.isBlank() ? error : null;
    }

    @ModelAttribute("rolUi")
    public RolUi rolUi(@ModelAttribute("rolActivo") String rolActivo) {
        return switch (rolActivo) {
            case "docente" -> new RolUi(
                    "docente",
                    "blue",
                    "DO",
                    "Docente",
                    "Vista de docente",
                    List.of(
                            new NavItem("Dashboard", "/dashboard", "dashboard"),
                            new NavItem("Mis turnos", "/turnos", "calendar"),
                            new NavItem("Check-in", "/checkins", "checkin"),
                            new NavItem("Recorridos", "/recorridos", "route"),
                            new NavItem("Incidentes", "/incidentes", "alert"),
                            new NavItem("Reasignaciones", "/reasignaciones", "refresh"),
                            new NavItem("Limpieza", "/limpiezas", "sparkles"),
                            new NavItem("Notificaciones", "/notificaciones", "bell"),
                            new NavItem("Mis métricas", "/metricas", "trophy"),
                            new NavItem("Reconocimientos", "/reconocimientos", "medal")
                    ));
            case "administrador" -> new RolUi(
                    "administrador",
                    "purple",
                    "AD",
                    "Administrador",
                    "Vista de administrador",
                    List.of(
                            new NavItem("Dashboard", "/dashboard", "dashboard"),
                            new NavItem("Usuarios", "/usuarios", "user"),
                            new NavItem("Turnos", "/turnos", "calendar"),
                            new NavItem("Zonas", "/zonas", "pin"),
                            new NavItem("Checkpoints", "/checkpoints", "checkpoint"),
                            new NavItem("Configuración", "/configuraciones", "settings"),
                            new NavItem("Incidentes", "/incidentes", "alert"),
                            new NavItem("Reasignaciones", "/reasignaciones", "refresh"),
                            new NavItem("Métricas", "/metricas", "trophy"),
                            new NavItem("Mapas de calor", "/mapas-calor", "map"),
                            new NavItem("Reportes", "/reportes", "report"),
                            new NavItem("Notificaciones", "/notificaciones", "bell")
                    ));
            default -> new RolUi(
                    "coordinador",
                    "green",
                    "CO",
                    "Coordinador",
                    "Vista de coordinador",
                    List.of(
                            new NavItem("Dashboard en vivo", "/dashboard", "dashboard"),
                            new NavItem("Turnos", "/turnos", "calendar"),
                            new NavItem("Cobertura", "/zonas", "pin"),
                            new NavItem("Check-ins", "/checkins", "checkin"),
                            new NavItem("Incidentes", "/incidentes", "alert"),
                            new NavItem("Reasignaciones", "/reasignaciones", "refresh"),
                            new NavItem("Recorridos", "/recorridos", "route"),
                            new NavItem("Mapas de calor", "/mapas-calor", "map"),
                            new NavItem("Métricas", "/metricas", "trophy"),
                            new NavItem("Reconocimientos", "/reconocimientos", "medal"),
                            new NavItem("Reportes", "/reportes", "report"),
                            new NavItem("Notificaciones", "/notificaciones", "bell")
                    ));
        };
    }

    @ModelAttribute("permisos")
    public RolePermissions permisos(@ModelAttribute("rolActivo") String rolActivo) {
        return new RolePermissions(rolActivo);
    }

    public record GlobalStats(long docentes, long turnos, long incidentes, long reasignaciones, long recorridos,
                              long reconocimientos) {
    }

    public record NavItem(String label, String href, String icon) {
    }

    public record RolUi(String key, String accentClass, String initials, String profileName, String label,
                        List<NavItem> navItems) {
    }

    public static final class RolePermissions {

        private final String role;

        public RolePermissions(String role) {
            this.role = role;
        }

        public boolean allows(String resource, String action) {
            return switch (role) {
                case "administrador" -> allowsAdministrador(resource, action);
                case "docente" -> allowsDocente(resource, action);
                default -> allowsCoordinador(resource, action);
            };
        }

        public boolean showsActions(String resource) {
            return allows(resource, "edit") || allows(resource, "delete");
        }

        private boolean allowsAdministrador(String resource, String action) {
            return switch (resource) {
                case "usuarios", "docentes", "coordinadores", "administradores",
                        "turnos", "zonas", "checkpoints", "configuraciones" -> managed(action);
                case "notificaciones", "metricas", "mapas-calor", "reconocimientos" -> managed(action);
                case "incidentes", "reasignaciones", "recorridos", "checkins", "limpiezas" -> readOnly(action);
                default -> false;
            };
        }

        private boolean allowsDocente(String resource, String action) {
            return switch (resource) {
                case "turnos", "notificaciones", "metricas", "reconocimientos" -> readOnly(action);
                case "checkins", "incidentes", "recorridos", "limpiezas" -> createOnly(action);
                case "reasignaciones" -> "view".equals(action) || "create".equals(action);
                default -> false;
            };
        }

        private boolean allowsCoordinador(String resource, String action) {
            return switch (resource) {
                case "turnos", "zonas", "checkins", "incidentes", "mapas-calor", "metricas", "notificaciones",
                        "recorridos", "reconocimientos" -> readOnly(action);
                case "reasignaciones" -> "view".equals(action) || "create".equals(action) || "edit".equals(action);
                default -> false;
            };
        }

        private boolean managed(String action) {
            return "view".equals(action) || "create".equals(action) || "edit".equals(action) || "delete".equals(action)
                    || "save".equals(action);
        }

        private boolean readOnly(String action) {
            return "view".equals(action);
        }

        private boolean createOnly(String action) {
            return "view".equals(action) || "create".equals(action) || "save".equals(action);
        }
    }
}
