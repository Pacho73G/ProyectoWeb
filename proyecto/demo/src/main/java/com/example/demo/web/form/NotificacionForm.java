package com.example.demo.web.form;

import java.time.LocalDateTime;

import com.example.demo.model.TipoNotificacion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificacionForm {
    private Long id;
    @NotNull
    private Long turnoId;
    @NotNull
    private TipoNotificacion tipo;
    private String mensaje;
    @NotNull
    private LocalDateTime enviadaEn;
    private Boolean leida = false;
    @NotNull
    @Min(0)
    private Integer minutosAnticipacion;
}
