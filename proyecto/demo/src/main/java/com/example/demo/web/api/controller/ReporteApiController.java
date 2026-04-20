/* Archivo documentado: Controlador REST del backend. Expone endpoints JSON consumidos por la SPA React para consultar y modificar datos del sistema. */
package com.example.demo.web.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.CatalogQueryService;
import com.example.demo.web.api.dto.ApiDtos.ReporteResumenDto;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ReporteApiController {

    private final CatalogQueryService catalogQueryService;

    public ReporteApiController(CatalogQueryService catalogQueryService) {
        this.catalogQueryService = catalogQueryService;
    }

    @GetMapping("/reportes/resumen")
    public ResponseEntity<ReporteResumenDto> resumen() {
        return ResponseEntity.ok(new ReporteResumenDto(
                catalogQueryService.totalDocentes(),
                catalogQueryService.totalTurnos(),
                catalogQueryService.totalIncidentes(),
                catalogQueryService.totalReasignaciones(),
                catalogQueryService.totalRecorridos(),
                catalogQueryService.totalReconocimientos()
        ));
    }
}
