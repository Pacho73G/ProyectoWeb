package com.example.demo.web.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetricaDocenteForm {
    private Long id;
    @NotNull
    private Long docenteId;
    @NotNull @Min(0)
    private Integer puntualidad;
    @NotNull @Min(0)
    private Integer cobertura;
    @NotNull @Min(0)
    private Integer retrasos;
    @NotNull @Min(0)
    private Integer recorridosCompletados;
    @NotNull @Min(0)
    private Integer incidentesRegistrados;
    @NotNull @Min(0)
    private Integer reasignacionesAceptadas;
    @NotNull @Min(0)
    private Integer puntajeTotal;
    @NotBlank
    private String periodo;
}
