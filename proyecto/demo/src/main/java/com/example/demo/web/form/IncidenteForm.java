package com.example.demo.web.form;

import java.time.LocalDateTime;

import com.example.demo.model.SeveridadIncidente;
import com.example.demo.model.TipoIncidente;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidenteForm {
    private Long id;
    @NotNull
    private Long turnoId;
    @NotNull
    private Long docenteId;
    @NotNull
    private Long zonaId;
    @NotNull
    private TipoIncidente tipo;
    @NotNull
    private SeveridadIncidente severidad;
    private String descripcion;
    private String observacionSocial;
    @NotNull
    private LocalDateTime registradoEn;
    private Boolean requiereSeguimiento = false;
}
