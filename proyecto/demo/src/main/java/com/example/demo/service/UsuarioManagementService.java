package com.example.demo.service;

import org.springframework.stereotype.Service;

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
public class UsuarioManagementService {

    private final UsuarioRepository usuarioRepository;
    private final DocenteRepository docenteRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final AdministradorRepository administradorRepository;
    private final ConfiguracionSistemaRepository configuracionSistemaRepository;

    public Usuario guardar(Usuario entity) { return usuarioRepository.save(entity); }
    public void eliminarUsuario(Long id) { usuarioRepository.deleteById(id); }
    public Docente guardar(Docente entity) { return docenteRepository.save(entity); }
    public void eliminarDocente(Long id) { docenteRepository.deleteById(id); }
    public Coordinador guardar(Coordinador entity) { return coordinadorRepository.save(entity); }
    public void eliminarCoordinador(Long id) { coordinadorRepository.deleteById(id); }
    public Administrador guardar(Administrador entity) { return administradorRepository.save(entity); }
    public void eliminarAdministrador(Long id) { administradorRepository.deleteById(id); }
    public ConfiguracionSistema guardar(ConfiguracionSistema entity) { return configuracionSistemaRepository.save(entity); }
    public void eliminarConfiguracion(Long id) { configuracionSistemaRepository.deleteById(id); }
}
