package com.example.demo.web.form;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckpointRecorridoForm {
    private Long id;
    private Long zonaId;
    private Long recorridoId;
    private String codigoQR;
    private String descripcion;
    private Integer orden;
    private LocalDateTime escaneadoEn;
}
