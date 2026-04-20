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
import com.example.demo.service.OperacionManagementService;
import com.example.demo.web.api.dto.ApiDtos.CheckInDto;
import com.example.demo.web.api.dto.ApiDtos.IncidenteDto;
import com.example.demo.web.api.dto.ApiDtos.LimpiezaDto;
import com.example.demo.web.api.dto.ApiDtos.NotificacionDto;
import com.example.demo.web.api.dto.ApiDtos.ReasignacionDto;
import com.example.demo.web.api.mapper.ApiMapper;
import com.example.demo.web.api.request.ApiRequests.CheckInRequest;
import com.example.demo.web.api.request.ApiRequests.IncidenteRequest;
import com.example.demo.web.api.request.ApiRequests.LimpiezaRequest;
import com.example.demo.web.api.request.ApiRequests.NotificacionRequest;
import com.example.demo.web.api.request.ApiRequests.ReasignacionRequest;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class OperacionApiController {

    private final CatalogQueryService catalogQueryService;
    private final OperacionManagementService operacionManagementService;

    public OperacionApiController(CatalogQueryService catalogQueryService, OperacionManagementService operacionManagementService) {
        this.catalogQueryService = catalogQueryService;
        this.operacionManagementService = operacionManagementService;
    }

    @GetMapping("/checkins")
    public ResponseEntity<List<CheckInDto>> checkins() {
        return ResponseEntity.ok(catalogQueryService.checkIns().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/checkins/{id}")
    public ResponseEntity<CheckInDto> checkin(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.checkIn(id)));
    }

    @PostMapping("/checkins")
    public ResponseEntity<CheckInDto> crearCheckin(@RequestBody CheckInRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(operacionManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.CheckIn(), catalogQueryService))));
    }

    @PutMapping("/checkins/{id}")
    public ResponseEntity<CheckInDto> actualizarCheckin(@PathVariable Long id, @RequestBody CheckInRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(operacionManagementService.guardar(ApiMapper.apply(request, catalogQueryService.checkIn(id), catalogQueryService))));
    }

    @DeleteMapping("/checkins/{id}")
    public ResponseEntity<Void> eliminarCheckin(@PathVariable Long id) {
        catalogQueryService.checkIn(id);
        operacionManagementService.eliminarCheckIn(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/incidentes")
    public ResponseEntity<List<IncidenteDto>> incidentes() {
        return ResponseEntity.ok(catalogQueryService.incidentes().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/incidentes/{id}")
    public ResponseEntity<IncidenteDto> incidente(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.incidente(id)));
    }

    @PostMapping("/incidentes")
    public ResponseEntity<IncidenteDto> crearIncidente(@RequestBody IncidenteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(operacionManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.Incidente(), catalogQueryService))));
    }

    @PutMapping("/incidentes/{id}")
    public ResponseEntity<IncidenteDto> actualizarIncidente(@PathVariable Long id, @RequestBody IncidenteRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(operacionManagementService.guardar(ApiMapper.apply(request, catalogQueryService.incidente(id), catalogQueryService))));
    }

    @DeleteMapping("/incidentes/{id}")
    public ResponseEntity<Void> eliminarIncidente(@PathVariable Long id) {
        catalogQueryService.incidente(id);
        operacionManagementService.eliminarIncidente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reasignaciones")
    public ResponseEntity<List<ReasignacionDto>> reasignaciones() {
        return ResponseEntity.ok(catalogQueryService.reasignaciones().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/reasignaciones/{id}")
    public ResponseEntity<ReasignacionDto> reasignacion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.reasignacion(id)));
    }

    @PostMapping("/reasignaciones")
    public ResponseEntity<ReasignacionDto> crearReasignacion(@RequestBody ReasignacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(operacionManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.Reasignacion(), catalogQueryService))));
    }

    @PutMapping("/reasignaciones/{id}")
    public ResponseEntity<ReasignacionDto> actualizarReasignacion(@PathVariable Long id, @RequestBody ReasignacionRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(operacionManagementService.guardar(ApiMapper.apply(request, catalogQueryService.reasignacion(id), catalogQueryService))));
    }

    @DeleteMapping("/reasignaciones/{id}")
    public ResponseEntity<Void> eliminarReasignacion(@PathVariable Long id) {
        catalogQueryService.reasignacion(id);
        operacionManagementService.eliminarReasignacion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/limpiezas")
    public ResponseEntity<List<LimpiezaDto>> limpiezas() {
        return ResponseEntity.ok(catalogQueryService.limpiezas().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/limpiezas/{id}")
    public ResponseEntity<LimpiezaDto> limpieza(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.limpieza(id)));
    }

    @PostMapping("/limpiezas")
    public ResponseEntity<LimpiezaDto> crearLimpieza(@RequestBody LimpiezaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(operacionManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.RegistroLimpieza(), catalogQueryService))));
    }

    @PutMapping("/limpiezas/{id}")
    public ResponseEntity<LimpiezaDto> actualizarLimpieza(@PathVariable Long id, @RequestBody LimpiezaRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(operacionManagementService.guardar(ApiMapper.apply(request, catalogQueryService.limpieza(id), catalogQueryService))));
    }

    @DeleteMapping("/limpiezas/{id}")
    public ResponseEntity<Void> eliminarLimpieza(@PathVariable Long id) {
        catalogQueryService.limpieza(id);
        operacionManagementService.eliminarLimpieza(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/notificaciones")
    public ResponseEntity<List<NotificacionDto>> notificaciones() {
        return ResponseEntity.ok(catalogQueryService.notificaciones().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/notificaciones/{id}")
    public ResponseEntity<NotificacionDto> notificacion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.notificacion(id)));
    }

    @PostMapping("/notificaciones")
    public ResponseEntity<NotificacionDto> crearNotificacion(@RequestBody NotificacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(operacionManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.Notificacion(), catalogQueryService))));
    }

    @PutMapping("/notificaciones/{id}")
    public ResponseEntity<NotificacionDto> actualizarNotificacion(@PathVariable Long id, @RequestBody NotificacionRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(operacionManagementService.guardar(ApiMapper.apply(request, catalogQueryService.notificacion(id), catalogQueryService))));
    }

    @DeleteMapping("/notificaciones/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        catalogQueryService.notificacion(id);
        operacionManagementService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
