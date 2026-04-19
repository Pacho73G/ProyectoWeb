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

import com.example.demo.service.AnaliticaManagementService;
import com.example.demo.service.CatalogQueryService;

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
    public ResponseEntity<List<MapaCalorDTO>> mapasCalor() {
        return ResponseEntity.ok(catalogQueryService.mapasCalor().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/mapas-calor/{id}")
    public ResponseEntity<MapaCalorDTO> mapaCalor(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.mapaCalor(id)));
    }

    @PostMapping("/mapas-calor")
    public ResponseEntity<MapaCalorDTO> crearMapaCalor(@RequestBody MapaCalorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(analiticaManagementService.guardar(
                        ApiMapper.apply(request, new com.example.demo.model.MapaCalor(), catalogQueryService)
                )));
    }

    @PutMapping("/mapas-calor/{id}")
    public ResponseEntity<MapaCalorDTO> actualizarMapaCalor(@PathVariable Long id, @RequestBody MapaCalorRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(analiticaManagementService.guardar(
                ApiMapper.apply(request, catalogQueryService.mapaCalor(id), catalogQueryService)
        )));
    }

    @DeleteMapping("/mapas-calor/{id}")
    public ResponseEntity<Void> eliminarMapaCalor(@PathVariable Long id) {
        analiticaManagementService.eliminarMapaCalor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/metricas")
    public ResponseEntity<List<MetricaDTO>> metricas() {
        return ResponseEntity.ok(catalogQueryService.metricas().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/metricas/{id}")
    public ResponseEntity<MetricaDTO> metrica(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.metrica(id)));
    }

    @PostMapping("/metricas")
    public ResponseEntity<MetricaDTO> crearMetrica(@RequestBody MetricaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(analiticaManagementService.guardar(
                        ApiMapper.apply(request, new com.example.demo.model.MetricaDocente(), catalogQueryService)
                )));
    }

    @PutMapping("/metricas/{id}")
    public ResponseEntity<MetricaDTO> actualizarMetrica(@PathVariable Long id, @RequestBody MetricaRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(analiticaManagementService.guardar(
                ApiMapper.apply(request, catalogQueryService.metrica(id), catalogQueryService)
        )));
    }

    @DeleteMapping("/metricas/{id}")
    public ResponseEntity<Void> eliminarMetrica(@PathVariable Long id) {
        analiticaManagementService.eliminarMetrica(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reconocimientos")
    public ResponseEntity<List<ReconocimientoDTO>> reconocimientos() {
        return ResponseEntity.ok(catalogQueryService.reconocimientos().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/reconocimientos/{id}")
    public ResponseEntity<ReconocimientoDTO> reconocimiento(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.reconocimiento(id)));
    }

    @PostMapping("/reconocimientos")
    public ResponseEntity<ReconocimientoDTO> crearReconocimiento(@RequestBody ReconocimientoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(analiticaManagementService.guardar(
                        ApiMapper.apply(request, new com.example.demo.model.Reconocimiento(), catalogQueryService)
                )));
    }

    @PutMapping("/reconocimientos/{id}")
    public ResponseEntity<ReconocimientoDTO> actualizarReconocimiento(@PathVariable Long id, @RequestBody ReconocimientoRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(analiticaManagementService.guardar(
                ApiMapper.apply(request, catalogQueryService.reconocimiento(id), catalogQueryService)
        )));
    }

    @DeleteMapping("/reconocimientos/{id}")
    public ResponseEntity<Void> eliminarReconocimiento(@PathVariable Long id) {
        analiticaManagementService.eliminarReconocimiento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recorridos")
    public ResponseEntity<List<RecorridoDTO>> recorridos() {
        return ResponseEntity.ok(catalogQueryService.recorridos().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/recorridos/{id}")
    public ResponseEntity<RecorridoDTO> recorrido(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.recorrido(id)));
    }

    @PostMapping("/recorridos")
    public ResponseEntity<RecorridoDTO> crearRecorrido(@RequestBody RecorridoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(analiticaManagementService.guardar(
                        ApiMapper.apply(request, new com.example.demo.model.Recorrido(), catalogQueryService)
                )));
    }

    @PutMapping("/recorridos/{id}")
    public ResponseEntity<RecorridoDTO> actualizarRecorrido(@PathVariable Long id, @RequestBody RecorridoRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(analiticaManagementService.guardar(
                ApiMapper.apply(request, catalogQueryService.recorrido(id), catalogQueryService)
        )));
    }

    @DeleteMapping("/recorridos/{id}")
    public ResponseEntity<Void> eliminarRecorrido(@PathVariable Long id) {
        analiticaManagementService.eliminarRecorrido(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/checkpoints")
    public ResponseEntity<List<CheckpointDTO>> checkpoints() {
        return ResponseEntity.ok(catalogQueryService.checkpoints().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/checkpoints/{id}")
    public ResponseEntity<CheckpointDTO> checkpoint(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.checkpoint(id)));
    }

    @PostMapping("/checkpoints")
    public ResponseEntity<CheckpointDTO> crearCheckpoint(@RequestBody CheckpointRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(analiticaManagementService.guardar(
                        ApiMapper.apply(request, new com.example.demo.model.CheckpointRecorrido(), catalogQueryService)
                )));
    }

    @PutMapping("/checkpoints/{id}")
    public ResponseEntity<CheckpointDTO> actualizarCheckpoint(@PathVariable Long id, @RequestBody CheckpointRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(analiticaManagementService.guardar(
                ApiMapper.apply(request, catalogQueryService.checkpoint(id), catalogQueryService)
        )));
    }

    @DeleteMapping("/checkpoints/{id}")
    public ResponseEntity<Void> eliminarCheckpoint(@PathVariable Long id) {
        analiticaManagementService.eliminarCheckpoint(id);
        return ResponseEntity.noContent().build();
    }
}
