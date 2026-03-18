package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reasignaciones")
/**
 * Solicitud y resolución de reemplazo cuando un docente no puede cubrir su turno.
 */
public class Reasignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turno_id", nullable = false, unique = true)
    private Turno turno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "docente_solicitante_id", nullable = false)
    private Docente docenteSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_reemplazo_id")
    private Docente docenteReemplazo;

    @Column(name = "motivo", nullable = false)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoReasignacion estado = EstadoReasignacion.PROPUESTA;

    @Column(name = "propuesta_en", nullable = false)
    private LocalDateTime propuestaEn;

    @Column(name = "respondida_en")
    private LocalDateTime respondidaEn;

    @Column(name = "segundos_ventana", nullable = false)
    private Integer segundosVentana;
}
