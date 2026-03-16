package com.example.demo.web.form;

import java.time.LocalDateTime;

import com.example.demo.model.EstadoRecorrido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecorridoForm {
    private Long id;
    private Long docenteId;
    private Long turnoId;
    private LocalDateTime iniciadoEn;
    private LocalDateTime finalizadoEn;
    private EstadoRecorrido estado;
    private Integer duracionMinutos;
}
