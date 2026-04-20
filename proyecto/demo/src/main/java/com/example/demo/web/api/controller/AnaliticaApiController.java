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

import com.example.demo.service.AnaliticaManagementService;
import com.example.demo.service.CatalogQueryService;
import com.example.demo.web.api.dto.ApiDtos.CheckpointDto;
import com.example.demo.web.api.dto.ApiDtos.MapaCalorDto;
import com.example.demo.web.api.dto.ApiDtos.MetricaDto;
import com.example.demo.web.api.dto.ApiDtos.ReconocimientoDto;
import com.example.demo.web.api.dto.ApiDtos.RecorridoDto;
import com.example.demo.web.api.mapper.ApiMapper;
import com.example.demo.web.api.request.ApiRequests.CheckpointRequest;
import com.example.demo.web.api.request.ApiRequests.MapaCalorRequest;
import com.example.demo.web.api.request.ApiRequests.MetricaRequest;
import com.example.demo.web.api.request.ApiRequests.ReconocimientoRequest;
import com.example.demo.web.api.request.ApiRequests.RecorridoRequest;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AnaliticaApiController {

    private final CatalogQueryService catalogQueryService;
    private final AnaliticaManagementService analiticaManagementService;

    public AnaliticaApiController(CatalogQueryService catalogQueryService, AnaliticaManagementService analiticaManagementService) {
        this.catalogQueryService = catalogQueryService;
        this.analiticaManagementService = analiticaManagementService;
    }

    @GetMapping("/mapas-calor")
    public ResponseEntity<List<MapaCalorDto>> mapasCalor() {
        return ResponseEntity.ok(catalogQueryService.mapasCalor().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/mapas-calor/{id}")
    public ResponseEntity<MapaCalorDto> mapaCalor(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.mapaCalor(id)));
    }

    @PostMapping("/mapas-calor")
    public ResponseEntity<MapaCalorDto> crearMapaCalor(@RequestBody MapaCalorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(analiticaManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.MapaCalor(), catalogQueryService))));
    }

    @PutMapping("/mapas-calor/{id}")
    public ResponseEntity<MapaCalorDto> actualizarMapaCalor(@PathVariable Long id, @RequestBody MapaCalorRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(analiticaManagementService.guardar(ApiMapper.apply(request, catalogQueryService.mapaCalor(id), catalogQueryService))));
    }

    @DeleteMapping("/mapas-calor/{id}")
    public ResponseEntity<Void> eliminarMapaCalor(@PathVariable Long id) {
        catalogQueryService.mapaCalor(id);
        analiticaManagementService.eliminarMapaCalor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/metricas")
    public ResponseEntity<List<MetricaDto>> metricas() {
        return ResponseEntity.ok(catalogQueryService.metricas().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/metricas/{id}")
    public ResponseEntity<MetricaDto> metrica(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.metrica(id)));
    }

    @PostMapping("/metricas")
    public ResponseEntity<MetricaDto> crearMetrica(@RequestBody MetricaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(analiticaManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.MetricaDocente(), catalogQueryService))));
    }

    @PutMapping("/metricas/{id}")
    public ResponseEntity<MetricaDto> actualizarMetrica(@PathVariable Long id, @RequestBody MetricaRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(analiticaManagementService.guardar(ApiMapper.apply(request, catalogQueryService.metrica(id), catalogQueryService))));
    }

    @DeleteMapping("/metricas/{id}")
    public ResponseEntity<Void> eliminarMetrica(@PathVariable Long id) {
        catalogQueryService.metrica(id);
        analiticaManagementService.eliminarMetrica(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reconocimientos")
    public ResponseEntity<List<ReconocimientoDto>> reconocimientos() {
        return ResponseEntity.ok(catalogQueryService.reconocimientos().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/reconocimientos/{id}")
    public ResponseEntity<ReconocimientoDto> reconocimiento(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.reconocimiento(id)));
    }

    @PostMapping("/reconocimientos")
    public ResponseEntity<ReconocimientoDto> crearReconocimiento(@RequestBody ReconocimientoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(analiticaManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.Reconocimiento(), catalogQueryService))));
    }

    @PutMapping("/reconocimientos/{id}")
    public ResponseEntity<ReconocimientoDto> actualizarReconocimiento(@PathVariable Long id, @RequestBody ReconocimientoRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(analiticaManagementService.guardar(ApiMapper.apply(request, catalogQueryService.reconocimiento(id), catalogQueryService))));
    }

    @DeleteMapping("/reconocimientos/{id}")
    public ResponseEntity<Void> eliminarReconocimiento(@PathVariable Long id) {
        catalogQueryService.reconocimiento(id);
        analiticaManagementService.eliminarReconocimiento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recorridos")
    public ResponseEntity<List<RecorridoDto>> recorridos() {
        return ResponseEntity.ok(catalogQueryService.recorridos().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/recorridos/{id}")
    public ResponseEntity<RecorridoDto> recorrido(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.recorrido(id)));
    }

    @PostMapping("/recorridos")
    public ResponseEntity<RecorridoDto> crearRecorrido(@RequestBody RecorridoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(analiticaManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.Recorrido(), catalogQueryService))));
    }

    @PutMapping("/recorridos/{id}")
    public ResponseEntity<RecorridoDto> actualizarRecorrido(@PathVariable Long id, @RequestBody RecorridoRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(analiticaManagementService.guardar(ApiMapper.apply(request, catalogQueryService.recorrido(id), catalogQueryService))));
    }

    @DeleteMapping("/recorridos/{id}")
    public ResponseEntity<Void> eliminarRecorrido(@PathVariable Long id) {
        catalogQueryService.recorrido(id);
        analiticaManagementService.eliminarRecorrido(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/checkpoints")
    public ResponseEntity<List<CheckpointDto>> checkpoints() {
        return ResponseEntity.ok(catalogQueryService.checkpoints().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/checkpoints/{id}")
    public ResponseEntity<CheckpointDto> checkpoint(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.checkpoint(id)));
    }

    @PostMapping("/checkpoints")
    public ResponseEntity<CheckpointDto> crearCheckpoint(@RequestBody CheckpointRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(analiticaManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.CheckpointRecorrido(), catalogQueryService))));
    }

    @PutMapping("/checkpoints/{id}")
    public ResponseEntity<CheckpointDto> actualizarCheckpoint(@PathVariable Long id, @RequestBody CheckpointRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(analiticaManagementService.guardar(ApiMapper.apply(request, catalogQueryService.checkpoint(id), catalogQueryService))));
    }

    @DeleteMapping("/checkpoints/{id}")
    public ResponseEntity<Void> eliminarCheckpoint(@PathVariable Long id) {
        catalogQueryService.checkpoint(id);
        analiticaManagementService.eliminarCheckpoint(id);
        return ResponseEntity.noContent().build();
    }
}
