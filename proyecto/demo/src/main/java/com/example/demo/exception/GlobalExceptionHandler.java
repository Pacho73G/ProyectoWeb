package com.example.demo.exception;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            RecursoNoEncontradoException.class,
            RecursoDuplicadoException.class,
            RelacionInvalidaException.class
    })
    public String handleBusinessExceptions(RuntimeException ex, HttpServletRequest request) {
        return redirectWithError(request, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        return redirectWithError(request, resolveIntegrityMessage(ex));
    }

    @ExceptionHandler(Exception.class)
    public String handleUnexpected(Exception ex, HttpServletRequest request, Model model) {
        model.addAttribute("title", "Error del sistema");
        model.addAttribute("message", "Ocurrió un problema inesperado al procesar la solicitud.");
        model.addAttribute("detail", ex.getMessage());
        model.addAttribute("backUrl", safePath(request.getHeader("Referer")));
        return "error/app-error";
    }

    private String redirectWithError(HttpServletRequest request, String message) {
        String target = safePath(request.getHeader("Referer"));
        String separator = target.contains("?") ? "&" : "?";
        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return "redirect:" + target + separator + "error=" + encoded;
    }

    private String safePath(String referer) {
        if (referer == null || referer.isBlank()) {
            return "/dashboard";
        }
        try {
            URI uri = URI.create(referer);
            String path = uri.getRawPath();
            String query = uri.getRawQuery();
            if (path == null || path.isBlank()) {
                return "/dashboard";
            }
            return query == null || query.isBlank() ? path : path + "?" + query;
        } catch (IllegalArgumentException ex) {
            return "/dashboard";
        }
    }

    private String resolveIntegrityMessage(DataIntegrityViolationException ex) {
        String message = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();
        String normalized = message != null ? message.toLowerCase() : "";

        if (normalized.contains("zonas_nombre_key")) {
            return "La zona ya existe.";
        }
        if (normalized.contains("usuarios_email_key")) {
            return "El correo del usuario ya existe.";
        }
        if (normalized.contains("fk_")) {
            return "No se puede eliminar porque tiene registros asociados.";
        }
        if (normalized.contains("checkins_turno_id_key")) {
            return "Ya existe un check-in para el turno seleccionado.";
        }
        if (normalized.contains("reasignaciones_turno_id_key")) {
            return "Ya existe una reasignación para el turno seleccionado.";
        }
        if (normalized.contains("registros_limpieza_turno_id_key")) {
            return "Ya existe un registro de limpieza para el turno seleccionado.";
        }
        return "No se pudo completar la operación por restricciones de datos.";
    }
}
