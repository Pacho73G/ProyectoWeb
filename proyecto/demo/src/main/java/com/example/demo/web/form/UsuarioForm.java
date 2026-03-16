package com.example.demo.web.form;

import com.example.demo.model.RolUsuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioForm {
    private Long id;
    private String nombre;
    private String email;
    private String passwordHash;
    private Boolean activo = true;
    private RolUsuario rol = RolUsuario.DOCENTE;
    private String descriptor;
    private Integer cargaActual = 0;
    private Integer puntajeGamificacion = 0;
}
