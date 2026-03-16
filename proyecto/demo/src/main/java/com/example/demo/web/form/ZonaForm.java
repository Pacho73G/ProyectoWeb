package com.example.demo.web.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZonaForm {
    private Long id;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private Integer capacidadMaxima;
    private Boolean activa = true;
}
