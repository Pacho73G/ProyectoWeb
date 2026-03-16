package com.example.demo.web.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetricaDocenteForm {
    private Long id;
    private Long docenteId;
    private Integer puntualidad;
    private Integer cobertura;
    private Integer retrasos;
    private Integer recorridosCompletados;
    private Integer incidentesRegistrados;
    private Integer reasignacionesAceptadas;
    private Integer puntajeTotal;
    private String periodo;
}
