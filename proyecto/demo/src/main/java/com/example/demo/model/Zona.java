/* Archivo documentado: Entidad del dominio persistida con JPA. Modela una parte del sistema de vigilancia docente y su estado en base de datos. */
package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "zonas")
/**
 * Área física del colegio donde se ejecuta vigilancia y se asocian incidentes/checkpoints.
 */
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "ubicacion", nullable = false)
    private String ubicacion;

    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;

    @OneToMany(mappedBy = "zona", fetch = FetchType.LAZY)
    private List<Turno> turnos = new ArrayList<>();

    @OneToMany(mappedBy = "zona", fetch = FetchType.LAZY)
    private List<CheckIn> checkIns = new ArrayList<>();

    @OneToMany(mappedBy = "zona", fetch = FetchType.LAZY)
    private List<Incidente> incidentes = new ArrayList<>();

    @OneToMany(mappedBy = "zona", fetch = FetchType.LAZY)
    private List<CheckpointRecorrido> checkpoints = new ArrayList<>();

    @OneToMany(mappedBy = "zona", fetch = FetchType.LAZY)
    private List<MapaCalor> mapasCalor = new ArrayList<>();
}
