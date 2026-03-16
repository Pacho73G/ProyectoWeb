package com.example.demo.web.form;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroLimpiezaForm {
    private Long id;
    @NotNull
    private Long turnoId;
    @NotNull
    @Min(0)
    private Integer escala;
    private String observaciones;
    @NotNull
    private LocalDateTime registradoEn;
}
