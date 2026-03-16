package com.example.demo.web.form;

import java.time.LocalDateTime;

import com.example.demo.model.TipoNotificacion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificacionForm {
    private Long id;
    private Long turnoId;
    private TipoNotificacion tipo;
    private String mensaje;
    private LocalDateTime enviadaEn;
    private Boolean leida = false;
    private Integer minutosAnticipacion;
}
