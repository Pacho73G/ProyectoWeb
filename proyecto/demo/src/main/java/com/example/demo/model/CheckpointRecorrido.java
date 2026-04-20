/* Archivo documentado: Entidad del dominio persistida con JPA. Modela una parte del sistema de vigilancia docente y su estado en base de datos. */
package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "checkpoints_recorrido")
/**
 * Punto de validación por QR dentro de una zona para comprobar movilidad del docente.
 */
public class CheckpointRecorrido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recorrido_id", nullable = false)
    private Recorrido recorrido;

    @Column(name = "codigo_qr", nullable = false)
    private String codigoQR;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "orden_checkpoint", nullable = false)
    private Integer orden;

    @Column(name = "escaneado_en")
    private LocalDateTime escaneadoEn;
}
