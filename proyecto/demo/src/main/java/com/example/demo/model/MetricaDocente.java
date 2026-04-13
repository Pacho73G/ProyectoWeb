package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "metricas_docente")
/**
 * Consolidado de gamificación y desempeño docente por periodo.
 */
public class MetricaDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @Column(name = "puntualidad", nullable = false)
    private Integer puntualidad;

    @Column(name = "cobertura", nullable = false)
    private Integer cobertura;

    @Column(name = "retrasos", nullable = false)
    private Integer retrasos;

    @Column(name = "recorridos_completados", nullable = false)
    private Integer recorridosCompletados;

    @Column(name = "incidentes_registrados", nullable = false)
    private Integer incidentesRegistrados;

    @Column(name = "reasignaciones_aceptadas", nullable = false)
    private Integer reasignacionesAceptadas;

    @Column(name = "puntaje_total", nullable = false)
    private Integer puntajeTotal;

    @Column(name = "periodo", nullable = false)
    private String periodo;

    @OneToMany(mappedBy = "metricaDocente", fetch = FetchType.LAZY)
    private List<Reconocimiento> reconocimientos = new ArrayList<>();
}
