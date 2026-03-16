package com.example.demo.web.form;

import java.time.LocalDate;

import com.example.demo.model.TipoIncidente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapaCalorForm {
    private Long id;
    private Long zonaId;
    private String franja;
    private TipoIncidente tipoIncidente;
    private Integer totalIncidentes;
    private Float porcentaje;
    private LocalDate periodoInicio;
    private LocalDate periodoFin;
}
