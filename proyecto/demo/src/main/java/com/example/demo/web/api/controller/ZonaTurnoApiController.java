/* Archivo documentado: Controlador REST del backend. Expone endpoints JSON consumidos por la SPA React para consultar y modificar datos del sistema. */
package com.example.demo.web.api.controller;

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
import com.example.demo.service.NotificacionManagementService;
import com.example.demo.service.OperacionManagementService;
import com.example.demo.web.api.dto.ApiDtos.TurnoDto;
import com.example.demo.web.api.dto.ApiDtos.ZonaDto;
import com.example.demo.web.api.mapper.ApiMapper;
import com.example.demo.web.api.request.ApiRequests.TurnoRequest;
import com.example.demo.web.api.request.ApiRequests.ZonaRequest;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ZonaTurnoApiController {

    private final CatalogQueryService catalogQueryService;
    private final OperacionManagementService operacionManagementService;
    private final NotificacionManagementService notificacionManagementService;

    public ZonaTurnoApiController(CatalogQueryService catalogQueryService, OperacionManagementService operacionManagementService,
                                  NotificacionManagementService notificacionManagementService) {
        this.catalogQueryService = catalogQueryService;
        this.operacionManagementService = operacionManagementService;
        this.notificacionManagementService = notificacionManagementService;
    }

    @GetMapping("/zonas")
    public ResponseEntity<List<ZonaDto>> zonas() {
        return ResponseEntity.ok(catalogQueryService.zonas().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/zonas/{id}")
    public ResponseEntity<ZonaDto> zona(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.zona(id)));
    }

    @PostMapping("/zonas")
    public ResponseEntity<ZonaDto> crearZona(@RequestBody ZonaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(operacionManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.Zona()))));
    }

    @PutMapping("/zonas/{id}")
    public ResponseEntity<ZonaDto> actualizarZona(@PathVariable Long id, @RequestBody ZonaRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(operacionManagementService.guardar(ApiMapper.apply(request, catalogQueryService.zona(id)))));
    }

    @DeleteMapping("/zonas/{id}")
    public ResponseEntity<Void> eliminarZona(@PathVariable Long id) {
        catalogQueryService.zona(id);
        operacionManagementService.eliminarZona(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/turnos")
    public ResponseEntity<List<TurnoDto>> turnos() {
        return ResponseEntity.ok(catalogQueryService.turnos().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/turnos/{id}")
    public ResponseEntity<TurnoDto> turno(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.turno(id)));
    }

    @PostMapping("/turnos")
    public ResponseEntity<TurnoDto> crearTurno(@RequestBody TurnoRequest request) {
        com.example.demo.model.Turno turno = operacionManagementService.guardar(
                ApiMapper.apply(request, new com.example.demo.model.Turno(), catalogQueryService)
        );
        notificacionManagementService.notificarAsignacionTurno(turno);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiMapper.toDto(turno));
    }

    @PutMapping("/turnos/{id}")
    public ResponseEntity<TurnoDto> actualizarTurno(@PathVariable Long id, @RequestBody TurnoRequest request) {
        com.example.demo.model.Turno turno = catalogQueryService.turno(id);
        com.example.demo.model.EstadoTurno estadoAnterior = turno.getEstado();
        turno = operacionManagementService.guardar(ApiMapper.apply(request, turno, catalogQueryService));

        if (turno.getEstado() == com.example.demo.model.EstadoTurno.EN_CURSO && estadoAnterior != com.example.demo.model.EstadoTurno.EN_CURSO) {
            notificacionManagementService.notificarCheckIn(turno);
        }
        if (turno.getEstado() == com.example.demo.model.EstadoTurno.CERRADO && estadoAnterior != com.example.demo.model.EstadoTurno.CERRADO) {
            notificacionManagementService.notificarCierreTurno(turno);
        }

        return ResponseEntity.ok(ApiMapper.toDto(turno));
    }

    @DeleteMapping("/turnos/{id}")
    public ResponseEntity<Void> eliminarTurno(@PathVariable Long id) {
        catalogQueryService.turno(id);
        operacionManagementService.eliminarTurno(id);
        return ResponseEntity.noContent().build();
    }
}
