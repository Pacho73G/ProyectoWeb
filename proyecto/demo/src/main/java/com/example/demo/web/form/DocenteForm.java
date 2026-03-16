package com.example.demo.web.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocenteForm {
    private Long id;
    private String nombre;
    private String email;
    private String passwordHash;
    private Boolean activo = true;
    private String materias;
    private Integer cargaActual = 0;
    private Integer puntajeGamificacion = 0;
}
