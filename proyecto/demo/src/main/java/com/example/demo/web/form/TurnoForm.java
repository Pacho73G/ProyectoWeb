package com.example.demo.web.form;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.demo.model.EstadoTurno;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurnoForm {
    private Long id;
    private Long docenteId;
    private Long zonaId;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String franja;
    private EstadoTurno estado;
    private LocalDateTime abiertoEn;
    private LocalDateTime cerradoEn;
}
