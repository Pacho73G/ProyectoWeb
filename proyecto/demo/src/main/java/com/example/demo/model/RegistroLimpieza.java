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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "registros_limpieza")
/**
 * Cierre del turno con evaluación básica del estado de limpieza de la zona.
 */
public class RegistroLimpieza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turno_id", unique = true)
    private Turno turno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;

    @Column(name = "escala", nullable = false)
    private Integer escala;

    @Column(name = "observaciones", nullable = false)
    private String observaciones;

    @Column(name = "asignada_en", nullable = false)
    private LocalDateTime asignadaEn;

    @Column(name = "registrado_en", nullable = false)
    private LocalDateTime registradoEn;

    @Column(name = "completada", nullable = false)
    private Boolean completada = false;
}
