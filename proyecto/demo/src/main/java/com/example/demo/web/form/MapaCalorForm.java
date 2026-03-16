package com.example.demo.web.form;

import java.time.LocalDate;

import com.example.demo.model.TipoIncidente;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapaCalorForm {
    private Long id;
    @NotNull
    private Long zonaId;
    @NotBlank
    private String franja;
    @NotNull
    private TipoIncidente tipoIncidente;
    @NotNull
    @Min(0)
    private Integer totalIncidentes;
    @NotNull
    @DecimalMin("0.0")
    private Float porcentaje;
    @NotNull
    private LocalDate periodoInicio;
    @NotNull
    private LocalDate periodoFin;
}
