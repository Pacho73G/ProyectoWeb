package com.example.demo.web.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZonaForm {
    private Long id;
    @NotBlank
    private String nombre;
    private String descripcion;
    @NotBlank
    private String ubicacion;
    @NotNull
    @Min(0)
    private Integer capacidadMaxima;
    private Boolean activa = true;
}
