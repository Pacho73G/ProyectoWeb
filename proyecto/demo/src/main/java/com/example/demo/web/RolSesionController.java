package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
/**
 * Simula la selección de rol en ausencia de autenticación real para la primera entrega.
 */
public class RolSesionController {

    @GetMapping("/seleccionar-rol")
    public String seleccionarRol(@RequestParam String rol, HttpSession session) {
        String rolNormalizado = switch (rol) {
            case "profesor" -> "docente";
            case "director" -> "administrador";
            default -> rol;
        };
        session.setAttribute("rolActivo", rolNormalizado);
        return "redirect:/dashboard";
    }
}
