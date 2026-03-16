package com.example.demo.web.form;

import java.time.LocalDateTime;

import com.example.demo.model.SeveridadIncidente;
import com.example.demo.model.TipoIncidente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidenteForm {
    private Long id;
    private Long turnoId;
    private Long docenteId;
    private Long zonaId;
    private TipoIncidente tipo;
    private SeveridadIncidente severidad;
    private String descripcion;
    private String observacionSocial;
    private LocalDateTime registradoEn;
    private Boolean requiereSeguimiento = false;
}
