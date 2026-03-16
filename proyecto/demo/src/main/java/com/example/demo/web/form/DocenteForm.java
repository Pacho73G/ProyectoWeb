package com.example.demo.web.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocenteForm {
    private Long id;
    @NotBlank
    private String nombre;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String passwordHash;
    private Boolean activo = true;
    @NotBlank
    private String materias;
    @NotNull
    @Min(0)
    private Integer cargaActual = 0;
    @NotNull
    @Min(0)
    private Integer puntajeGamificacion = 0;
}
