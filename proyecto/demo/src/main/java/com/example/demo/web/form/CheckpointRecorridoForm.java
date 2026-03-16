package com.example.demo.web.form;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckpointRecorridoForm {
    private Long id;
    @NotNull
    private Long zonaId;
    @NotNull
    private Long recorridoId;
    @NotBlank
    private String codigoQR;
    private String descripcion;
    @NotNull
    @Min(0)
    private Integer orden;
    @NotNull
    private LocalDateTime escaneadoEn;
}
