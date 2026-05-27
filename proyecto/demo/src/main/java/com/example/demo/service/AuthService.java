package com.example.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtService;
import com.example.demo.security.LegacyCompatiblePasswordEncoder;
import com.example.demo.security.SecurityUser;
import com.example.demo.web.api.dto.ApiDtos.AuthResponseDto;
import com.example.demo.web.api.request.ApiRequests.LoginRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final LegacyCompatiblePasswordEncoder passwordEncoder;

    public AuthResponseDto login(LoginRequest request) {
        // Aquí se valida email + contraseña contra Spring Security.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Usuario usuario = securityUser.getUsuario();
        upgradePasswordIfNeeded(usuario, request.password());
        return buildAuthResponse(usuario, jwtService.generateToken(securityUser));
    }

    public AuthResponseDto me(String email) {
        // Este endpoint se usa después del login para reconstruir el perfil desde el token.
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario autenticado no encontrado."));
        return buildAuthResponse(usuario, null);
    }

    private AuthResponseDto buildAuthResponse(Usuario usuario, String token) {
        return new AuthResponseDto(
                token,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name(),
                usuario.getActivo()
        );
    }

    private void upgradePasswordIfNeeded(Usuario usuario, String rawPassword) {
        if (!passwordEncoder.needsUpgrade(usuario.getPasswordHash())) {
            return;
        }
        // Después del primer login exitoso, el password legacy se reemplaza por BCrypt
        // para dejar la base alineada con el esquema de seguridad nuevo.
        usuario.setPasswordHash(passwordEncoder.encode(rawPassword));
        usuarioRepository.save(usuario);
    }
}
