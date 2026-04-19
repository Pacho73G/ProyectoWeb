package com.example.demo.web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.CatalogQueryService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ReporteApiController {

    private final CatalogQueryService catalogQueryService;

    public ReporteApiController(CatalogQueryService catalogQueryService) {
        this.catalogQueryService = catalogQueryService;
    }

    @GetMapping("/reportes/resumen")
    public ResponseEntity<ReporteResumenDTO> resumen() {
        return ResponseEntity.ok(new ReporteResumenDTO(
                catalogQueryService.totalDocentes(),
                catalogQueryService.totalTurnos(),
                catalogQueryService.totalIncidentes(),
                catalogQueryService.totalReasignaciones(),
                catalogQueryService.totalRecorridos(),
                catalogQueryService.totalReconocimientos()
        ));
    }
}
