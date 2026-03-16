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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "incidentes")
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turno_id", nullable = false)
    private Turno turno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoIncidente tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "severidad", nullable = false)
    private SeveridadIncidente severidad;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "observacion_social")
    private String observacionSocial;

    @Column(name = "registrado_en", nullable = false)
    private LocalDateTime registradoEn;

    @Column(name = "requiere_seguimiento", nullable = false)
    private Boolean requiereSeguimiento = false;
}
