package com.example.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final LegacyCompatiblePasswordEncoder passwordEncoder;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService userDetailsService,
                          LegacyCompatiblePasswordEncoder passwordEncoder) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.getWriter().write(new ObjectMapper().writeValueAsString(
                                    Map.of("message", "Debes iniciar sesión para acceder a este recurso.")
                            ));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.getWriter().write(new ObjectMapper().writeValueAsString(
                                    Map.of("message", "No tienes permisos para ejecutar esta acción.")
                            ));
                        })
                )
                .authenticationProvider(authenticationProvider())
                // El filtro JWT debe ejecutarse antes del filtro estándar de usuario/contraseña
                // para que cada request llegue ya autenticado si trae token válido.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/zonas/**", "/api/turnos/**", "/api/checkins/**", "/api/incidentes/**",
                                "/api/reasignaciones/**", "/api/limpiezas/**", "/api/notificaciones/**", "/api/mapas-calor/**",
                                "/api/metricas/**", "/api/reconocimientos/**", "/api/recorridos/**", "/api/checkpoints/**",
                                "/api/reportes/**", "/api/docentes/**").hasAnyRole("ADMINISTRADOR", "COORDINADOR", "DOCENTE")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**", "/api/configuraciones/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/usuarios/**", "/api/configuraciones/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/zonas/**", "/api/turnos/**").hasRole("ADMINISTRADOR")
                        // El DOCENTE necesita PUT en turnos para registrar check-in (PENDIENTE→EN_CURSO).
                        // El scheduler cierra automáticamente los turnos, pero el docente también puede
                        // finalizarlos manualmente desde su vista. El admin conserva control total.
                        .requestMatchers(HttpMethod.PUT, "/api/turnos/**").hasAnyRole("ADMINISTRADOR", "DOCENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/zonas/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/zonas/**", "/api/turnos/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/checkins/**", "/api/incidentes/**", "/api/reasignaciones/**", "/api/recorridos/**").hasAnyRole("ADMINISTRADOR", "COORDINADOR", "DOCENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/checkins/**", "/api/incidentes/**", "/api/reasignaciones/**", "/api/recorridos/**").hasAnyRole("ADMINISTRADOR", "COORDINADOR", "DOCENTE")
                        .requestMatchers(HttpMethod.POST, "/api/limpiezas/**").hasAnyRole("ADMINISTRADOR", "DOCENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/limpiezas/**").hasAnyRole("ADMINISTRADOR", "DOCENTE")
                        .requestMatchers(HttpMethod.GET, "/api/notificaciones/unread-count", "/api/notificaciones/mark-read").hasAnyRole("ADMINISTRADOR", "COORDINADOR", "DOCENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/notificaciones/mark-read").hasAnyRole("ADMINISTRADOR", "COORDINADOR", "DOCENTE")
                        .requestMatchers(HttpMethod.POST, "/api/mapas-calor/**", "/api/metricas/**", "/api/reconocimientos/**", "/api/checkpoints/**").hasAnyRole("ADMINISTRADOR", "COORDINADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/mapas-calor/**", "/api/metricas/**", "/api/reconocimientos/**", "/api/checkpoints/**").hasAnyRole("ADMINISTRADOR", "COORDINADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/mapas-calor/**", "/api/metricas/**", "/api/reconocimientos/**", "/api/checkpoints/**").hasAnyRole("ADMINISTRADOR", "COORDINADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/checkins/**", "/api/incidentes/**", "/api/reasignaciones/**", "/api/limpiezas/**", "/api/notificaciones/**", "/api/recorridos/**").hasAnyRole("ADMINISTRADOR", "COORDINADOR")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // DaoAuthenticationProvider usa nuestro UserDetailsService + BCrypt para validar login.
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Este bean valida BCrypt nuevo y también contraseñas legacy
        // en texto plano que ya existían en la base antes de migrar auth.
        return passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
