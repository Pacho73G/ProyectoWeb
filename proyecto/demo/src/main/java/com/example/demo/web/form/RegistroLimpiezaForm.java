package com.example.demo.web.form;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroLimpiezaForm {
    private Long id;
    private Long turnoId;
    private Integer escala;
    private String observaciones;
    private LocalDateTime registradoEn;
}
