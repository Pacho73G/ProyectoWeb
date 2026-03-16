package com.example.demo.web.form;

import java.time.LocalDate;

import com.example.demo.model.TipoReconocimiento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReconocimientoForm {
    private Long id;
    @NotNull
    private Long metricaDocenteId;
    @NotBlank
    private String titulo;
    private String descripcion;
    @NotNull
    private TipoReconocimiento tipo;
    @NotNull
    private LocalDate otorgadoEn;
    @NotBlank
    private String trimestre;
}
