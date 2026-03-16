package com.example.demo.web.form;

import java.time.LocalDateTime;

import com.example.demo.model.EstadoRecorrido;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecorridoForm {
    private Long id;
    @NotNull
    private Long docenteId;
    @NotNull
    private Long turnoId;
    @NotNull
    private LocalDateTime iniciadoEn;
    private LocalDateTime finalizadoEn;
    @NotNull
    private EstadoRecorrido estado;
    @NotNull
    @Min(0)
    private Integer duracionMinutos;
}
