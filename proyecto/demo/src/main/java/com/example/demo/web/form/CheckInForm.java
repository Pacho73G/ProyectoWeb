package com.example.demo.web.form;

import java.time.LocalDateTime;

import com.example.demo.model.MetodoCheckIn;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckInForm {
    private Long id;
    @NotNull
    private Long turnoId;
    @NotNull
    private Long docenteId;
    @NotNull
    private Long zonaId;
    @NotNull
    private LocalDateTime timestamp;
    @NotNull
    private MetodoCheckIn metodo;
    @NotBlank
    private String evidencia;
    private Boolean valido = true;
}
