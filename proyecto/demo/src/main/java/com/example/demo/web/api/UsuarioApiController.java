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

import com.example.demo.model.Docente;
import com.example.demo.service.CatalogQueryService;
import com.example.demo.service.UsuarioManagementService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioApiController {

    private final CatalogQueryService catalogQueryService;
    private final UsuarioManagementService usuarioManagementService;

    public UsuarioApiController(CatalogQueryService catalogQueryService, UsuarioManagementService usuarioManagementService) {
        this.catalogQueryService = catalogQueryService;
        this.usuarioManagementService = usuarioManagementService;
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> usuarios() {
        return ResponseEntity.ok(catalogQueryService.usuarios().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDTO> usuario(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.usuario(id)));
    }

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toDto(usuarioManagementService.guardar(ApiMapper.apply(request, null))));
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(
                usuarioManagementService.guardar(ApiMapper.apply(request, catalogQueryService.usuario(id)))
        ));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioManagementService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/docentes")
    public ResponseEntity<List<UsuarioDTO>> docentes() {
        return ResponseEntity.ok(catalogQueryService.docentes().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/docentes/{id}")
    public ResponseEntity<UsuarioDTO> docente(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.docente(id)));
    }

    @PostMapping("/docentes")
    public ResponseEntity<UsuarioDTO> crearDocente(@RequestBody DocenteRequest request) {
        Docente docente = ApiMapper.apply(request, new Docente());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiMapper.toDto(usuarioManagementService.guardar(docente)));
    }

    @PutMapping("/docentes/{id}")
    public ResponseEntity<UsuarioDTO> actualizarDocente(@PathVariable Long id, @RequestBody DocenteRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(
                usuarioManagementService.guardar(ApiMapper.apply(request, catalogQueryService.docente(id)))
        ));
    }

    @DeleteMapping("/docentes/{id}")
    public ResponseEntity<Void> eliminarDocente(@PathVariable Long id) {
        usuarioManagementService.eliminarDocente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/configuraciones")
    public ResponseEntity<List<ConfiguracionDTO>> configuraciones() {
        return ResponseEntity.ok(catalogQueryService.configuraciones().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/configuraciones/{id}")
    public ResponseEntity<ConfiguracionDTO> configuracion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.configuracion(id)));
    }

    @PostMapping("/configuraciones")
    public ResponseEntity<ConfiguracionDTO> crearConfiguracion(@RequestBody ConfiguracionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiMapper.toDto(
                usuarioManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.ConfiguracionSistema(), catalogQueryService))
        ));
    }

    @PutMapping("/configuraciones/{id}")
    public ResponseEntity<ConfiguracionDTO> actualizarConfiguracion(@PathVariable Long id, @RequestBody ConfiguracionRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(
                usuarioManagementService.guardar(ApiMapper.apply(request, catalogQueryService.configuracion(id), catalogQueryService))
        ));
    }

    @DeleteMapping("/configuraciones/{id}")
    public ResponseEntity<Void> eliminarConfiguracion(@PathVariable Long id) {
        usuarioManagementService.eliminarConfiguracion(id);
        return ResponseEntity.noContent().build();
    }
}
