package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.CatalogQueryService;

@Controller
/**
 * Controlador de entrada y vistas generales como dashboard y reportes.
 */
public class InicioController extends BaseController {

    public InicioController(CatalogQueryService catalogQueryService) {
        super(catalogQueryService);
    }

    @GetMapping("/")
    public String login() {
        return "inicio/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        addShared(model);
        return "inicio/dashboard";
    }

    @GetMapping("/reportes")
    public String reportes(Model model) {
        addShared(model);
        return "inicio/reportes";
    }
}
