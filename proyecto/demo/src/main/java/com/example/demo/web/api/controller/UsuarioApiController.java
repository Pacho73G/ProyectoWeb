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

import com.example.demo.model.Docente;
import com.example.demo.service.CatalogQueryService;
import com.example.demo.service.UsuarioManagementService;
import com.example.demo.web.api.dto.ApiDtos.ConfiguracionDto;
import com.example.demo.web.api.dto.ApiDtos.UsuarioDto;
import com.example.demo.web.api.mapper.ApiMapper;
import com.example.demo.web.api.request.ApiRequests.ConfiguracionRequest;
import com.example.demo.web.api.request.ApiRequests.DocenteRequest;
import com.example.demo.web.api.request.ApiRequests.UsuarioRequest;

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
    public ResponseEntity<List<UsuarioDto>> usuarios() {
        return ResponseEntity.ok(catalogQueryService.usuarios().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDto> usuario(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.usuario(id)));
    }

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioDto> crearUsuario(@RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiMapper.toDto(usuarioManagementService.guardar(ApiMapper.apply(request, null))));
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDto> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(usuarioManagementService.guardar(ApiMapper.apply(request, catalogQueryService.usuario(id)))));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        catalogQueryService.usuario(id);
        usuarioManagementService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/docentes")
    public ResponseEntity<List<UsuarioDto>> docentes() {
        return ResponseEntity.ok(catalogQueryService.docentes().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/docentes/{id}")
    public ResponseEntity<UsuarioDto> docente(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.docente(id)));
    }

    @PostMapping("/docentes")
    public ResponseEntity<UsuarioDto> crearDocente(@RequestBody DocenteRequest request) {
        Docente docente = ApiMapper.apply(request, new Docente());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiMapper.toDto(usuarioManagementService.guardar(docente)));
    }

    @PutMapping("/docentes/{id}")
    public ResponseEntity<UsuarioDto> actualizarDocente(@PathVariable Long id, @RequestBody DocenteRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(usuarioManagementService.guardar(ApiMapper.apply(request, catalogQueryService.docente(id)))));
    }

    @DeleteMapping("/docentes/{id}")
    public ResponseEntity<Void> eliminarDocente(@PathVariable Long id) {
        catalogQueryService.docente(id);
        usuarioManagementService.eliminarDocente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/configuraciones")
    public ResponseEntity<List<ConfiguracionDto>> configuraciones() {
        return ResponseEntity.ok(catalogQueryService.configuraciones().stream().map(ApiMapper::toDto).toList());
    }

    @GetMapping("/configuraciones/{id}")
    public ResponseEntity<ConfiguracionDto> configuracion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiMapper.toDto(catalogQueryService.configuracion(id)));
    }

    @PostMapping("/configuraciones")
    public ResponseEntity<ConfiguracionDto> crearConfiguracion(@RequestBody ConfiguracionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiMapper.toDto(
                usuarioManagementService.guardar(ApiMapper.apply(request, new com.example.demo.model.ConfiguracionSistema(), catalogQueryService))
        ));
    }

    @PutMapping("/configuraciones/{id}")
    public ResponseEntity<ConfiguracionDto> actualizarConfiguracion(@PathVariable Long id, @RequestBody ConfiguracionRequest request) {
        return ResponseEntity.ok(ApiMapper.toDto(
                usuarioManagementService.guardar(ApiMapper.apply(request, catalogQueryService.configuracion(id), catalogQueryService))
        ));
    }

    @DeleteMapping("/configuraciones/{id}")
    public ResponseEntity<Void> eliminarConfiguracion(@PathVariable Long id) {
        catalogQueryService.configuracion(id);
        usuarioManagementService.eliminarConfiguracion(id);
        return ResponseEntity.noContent().build();
    }
}
