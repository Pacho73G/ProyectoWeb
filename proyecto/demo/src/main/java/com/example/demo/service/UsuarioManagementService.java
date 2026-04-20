/* Archivo documentado: Servicio de escritura para usuarios y configuración del sistema. Encapsula reglas de guardado y eliminación relacionadas con perfiles y ajustes globales. */
package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.exception.RecursoDuplicadoException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.model.Administrador;
import com.example.demo.model.ConfiguracionSistema;
import com.example.demo.model.Coordinador;
import com.example.demo.model.Docente;
import com.example.demo.model.Usuario;
import com.example.demo.repository.AdministradorRepository;
import com.example.demo.repository.ConfiguracionSistemaRepository;
import com.example.demo.repository.CoordinadorRepository;
import com.example.demo.repository.DocenteRepository;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
/**
 * Encapsula altas, cambios y bajas del módulo administrativo de usuarios y configuración.
 */
public class UsuarioManagementService {

    private final UsuarioRepository usuarioRepository;
    private final DocenteRepository docenteRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final AdministradorRepository administradorRepository;
    private final ConfiguracionSistemaRepository configuracionSistemaRepository;

    public Usuario guardar(Usuario entity) {
        validateUsuario(entity);
        return usuarioRepository.save(entity);
    }

    public void eliminarUsuario(Long id) {
        ensureExists(usuarioRepository.existsById(id), "El usuario solicitado no existe.");
        usuarioRepository.deleteById(id);
    }

    public Docente guardar(Docente entity) {
        validateUsuario(entity);
        return docenteRepository.save(entity);
    }

    public void eliminarDocente(Long id) {
        ensureExists(docenteRepository.existsById(id), "El docente solicitado no existe.");
        docenteRepository.deleteById(id);
    }

    public Coordinador guardar(Coordinador entity) {
        validateUsuario(entity);
        return coordinadorRepository.save(entity);
    }

    public void eliminarCoordinador(Long id) {
        ensureExists(coordinadorRepository.existsById(id), "El coordinador solicitado no existe.");
        coordinadorRepository.deleteById(id);
    }

    public Administrador guardar(Administrador entity) {
        validateUsuario(entity);
        return administradorRepository.save(entity);
    }

    public void eliminarAdministrador(Long id) {
        ensureExists(administradorRepository.existsById(id), "El administrador solicitado no existe.");
        administradorRepository.deleteById(id);
    }

    public ConfiguracionSistema guardar(ConfiguracionSistema entity) {
        validateConfiguracion(entity);
        return configuracionSistemaRepository.save(entity);
    }

    public void eliminarConfiguracion(Long id) {
        ensureExists(configuracionSistemaRepository.existsById(id), "La configuración solicitada no existe.");
        configuracionSistemaRepository.deleteById(id);
    }

    private void validateUsuario(Usuario entity) {
        // El email se trata como identificador único visible en la operación administrativa.
        boolean exists = entity.getId() == null
                ? usuarioRepository.existsByEmailIgnoreCase(entity.getEmail())
                : usuarioRepository.existsByEmailIgnoreCaseAndIdNot(entity.getEmail(), entity.getId());
        if (exists) {
            throw new RecursoDuplicadoException("El correo del usuario ya existe.");
        }
    }

    private void validateConfiguracion(ConfiguracionSistema entity) {
        Long administradorId = entity.getAdministrador() != null ? entity.getAdministrador().getId() : null;
        if (administradorId == null) {
            return;
        }
        boolean exists = entity.getId() == null
                ? configuracionSistemaRepository.existsByAdministradorId(administradorId)
                : configuracionSistemaRepository.existsByAdministradorIdAndIdNot(administradorId, entity.getId());
        if (exists) {
            throw new RecursoDuplicadoException("El administrador seleccionado ya tiene una configuración asignada.");
        }
    }

    private void ensureExists(boolean exists, String message) {
        if (!exists) {
            throw new RecursoNoEncontradoException(message);
        }
    }
}
