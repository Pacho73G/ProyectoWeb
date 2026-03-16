package com.example.demo.web.form;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.demo.model.EstadoTurno;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurnoForm {
    private Long id;
    @NotNull
    private Long docenteId;
    @NotNull
    private Long zonaId;
    @NotNull
    private LocalDate fecha;
    @NotNull
    private LocalTime horaInicio;
    @NotNull
    private LocalTime horaFin;
    @NotBlank
    private String franja;
    @NotNull
    private EstadoTurno estado;
    private LocalDateTime abiertoEn;
    private LocalDateTime cerradoEn;
}
