package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "docentes")
public class Docente extends Usuario {

    @Column(name = "materias", nullable = false)
    private String materias;

    @Column(name = "carga_actual", nullable = false)
    private Integer cargaActual = 0;

    @Column(name = "puntaje_gamificacion", nullable = false)
    private Integer puntajeGamificacion = 0;
}
