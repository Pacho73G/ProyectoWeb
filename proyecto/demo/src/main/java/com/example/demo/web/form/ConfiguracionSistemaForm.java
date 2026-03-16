package com.example.demo.web.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfiguracionSistemaForm {
    private Long id;
    @NotNull
    private Long administradorId;
    @NotNull @Min(0)
    private Integer minutosAlertaAusencia;
    @NotNull @Min(0)
    private Integer segundosVentanaReasignacion;
    @NotNull @Min(0)
    private Integer minutosInactividad;
    @NotNull @Min(0)
    private Integer umbralIngreso;
    @NotNull @Min(0)
    private Integer minutosRecordatorio1;
    @NotNull @Min(0)
    private Integer minutosRecordatorio2;
}
