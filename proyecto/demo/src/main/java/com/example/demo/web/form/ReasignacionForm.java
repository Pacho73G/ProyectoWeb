package com.example.demo.web.form;

import java.time.LocalDateTime;

import com.example.demo.model.EstadoReasignacion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReasignacionForm {
    private Long id;
    private Long turnoId;
    private Long docenteSolicitanteId;
    private Long docenteReemplazoId;
    private String motivo;
    private EstadoReasignacion estado;
    private LocalDateTime propuestaEn;
    private LocalDateTime respondidaEn;
    private Integer segundosVentana;
}
