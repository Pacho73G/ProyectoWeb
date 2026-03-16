package com.example.demo.web.form;

import java.time.LocalDate;

import com.example.demo.model.TipoReconocimiento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReconocimientoForm {
    private Long id;
    private Long metricaDocenteId;
    private String titulo;
    private String descripcion;
    private TipoReconocimiento tipo;
    private LocalDate otorgadoEn;
    private String trimestre;
}
