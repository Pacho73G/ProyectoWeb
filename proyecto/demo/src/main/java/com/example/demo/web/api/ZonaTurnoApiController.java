package com.example.demo.web.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.CatalogQueryService;
import com.example.demo.service.OperacionManagementService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ZonaTurnoApiController {

    private final CatalogQueryService catalogQueryService;
    private final OperacionManagementService operacionManagementService;

    public ZonaTurnoApiController(CatalogQueryService catalogQueryService, OperacionManagementService operacionManagementService) {
        this.catalogQueryService = catalogQueryService;
        this.operacionManagementService = operacionManagementService;
    }

    @GetMapping("/zonas")
    public ResponseEntity<List<ZonaDTO>> zonas() {
        return ResponseEntity.ok(catalogQueryService.zonas().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/zonas/{id}")
    public ResponseEntity<ZonaDTO> zona(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.zona(id)));
    }

    @PostMapping("/zonas")
    public ResponseEntity<ZonaDTO> crearZona(@RequestBody ZonaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(operacionManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.Zona()))));
    }

    @PutMapping("/zonas/{id}")
    public ResponseEntity<ZonaDTO> actualizarZona(@PathVariable Long id, @RequestBody ZonaRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(
                operacionManagementService.guardar(ApiMapper.apply(request, catalogQueryService.zona(id)))
        ));
    }

    @DeleteMapping("/zonas/{id}")
    public ResponseEntity<Void> eliminarZona(@PathVariable Long id) {
        operacionManagementService.eliminarZona(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/turnos")
    public ResponseEntity<List<TurnoDTO>> turnos() {
        return ResponseEntity.ok(catalogQueryService.turnos().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/turnos/{id}")
    public ResponseEntity<TurnoDTO> turno(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.turno(id)));
    }

    @PostMapping("/turnos")
    public ResponseEntity<TurnoDTO> crearTurno(@RequestBody TurnoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(operacionManagementService.guardar(
                        ApiMapper.apply(request, new com.example.demo.model.Turno(), catalogQueryService)
                )));
    }

    @PutMapping("/turnos/{id}")
    public ResponseEntity<TurnoDTO> actualizarTurno(@PathVariable Long id, @RequestBody TurnoRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(operacionManagementService.guardar(
                ApiMapper.apply(request, catalogQueryService.turno(id), catalogQueryService)
        )));
    }

    @DeleteMapping("/turnos/{id}")
    public ResponseEntity<Void> eliminarTurno(@PathVariable Long id) {
        operacionManagementService.eliminarTurno(id);
        return ResponseEntity.noContent().build();
    }
}
