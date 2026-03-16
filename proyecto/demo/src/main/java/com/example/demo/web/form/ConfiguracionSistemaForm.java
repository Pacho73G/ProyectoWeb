package com.example.demo.web.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfiguracionSistemaForm {
    private Long id;
    private Long administradorId;
    private Integer minutosAlertaAusencia;
    private Integer segundosVentanaReasignacion;
    private Integer minutosInactividad;
    private Integer umbralIngreso;
    private Integer minutosRecordatorio1;
    private Integer minutosRecordatorio2;
}
