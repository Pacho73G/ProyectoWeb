package com.example.demo.web.form;

import java.time.LocalDateTime;

import com.example.demo.model.EstadoReasignacion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReasignacionForm {
    private Long id;
    @NotNull
    private Long turnoId;
    @NotNull
    private Long docenteSolicitanteId;
    private Long docenteReemplazoId;
    private String motivo;
    @NotNull
    private EstadoReasignacion estado;
    @NotNull
    private LocalDateTime propuestaEn;
    private LocalDateTime respondidaEn;
    @NotNull
    @Min(0)
    private Integer segundosVentana;
}
