package com.example.demo.web.form;

import java.time.LocalDateTime;

import com.example.demo.model.MetodoCheckIn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckInForm {
    private Long id;
    private Long turnoId;
    private Long docenteId;
    private Long zonaId;
    private LocalDateTime timestamp;
    private MetodoCheckIn metodo;
    private String evidencia;
    private Boolean valido = true;
}
