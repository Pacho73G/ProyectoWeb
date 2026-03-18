package com.example.demo.model;

import java.time.LocalDate;

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
@Table(name = "mapas_calor")
/**
 * Resumen analítico de incidentes por zona, franja y periodo.
 */
public class MapaCalor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;

    @Column(name = "franja", nullable = false)
    private String franja;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_incidente", nullable = false)
    private TipoIncidente tipoIncidente;

    @Column(name = "total_incidentes", nullable = false)
    private Integer totalIncidentes;

    @Column(name = "porcentaje", nullable = false)
    private Float porcentaje;

    @Column(name = "periodo_inicio", nullable = false)
    private LocalDate periodoInicio;

    @Column(name = "periodo_fin", nullable = false)
    private LocalDate periodoFin;
}
