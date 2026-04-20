/* Archivo documentado: Entidad del dominio persistida con JPA. Modela una parte del sistema de vigilancia docente y su estado en base de datos. */
package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "docentes")
/**
 * Perfil operativo que ejecuta turnos, check-ins, recorridos e incidentes.
 */
public class Docente extends Usuario {

    @Column(name = "materias", nullable = false)
    private String materias;

    @Column(name = "carga_actual", nullable = false)
    private Integer cargaActual = 0;

    @Column(name = "puntaje_gamificacion", nullable = false)
    private Integer puntajeGamificacion = 0;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private List<Turno> turnos = new ArrayList<>();

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private List<CheckIn> checkIns = new ArrayList<>();

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private List<Incidente> incidentes = new ArrayList<>();

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private List<Recorrido> recorridos = new ArrayList<>();

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private List<MetricaDocente> metricas = new ArrayList<>();

    @OneToMany(mappedBy = "docenteSolicitante", fetch = FetchType.LAZY)
    private List<Reasignacion> reasignacionesSolicitadas = new ArrayList<>();

    @OneToMany(mappedBy = "docenteReemplazo", fetch = FetchType.LAZY)
    private List<Reasignacion> reasignacionesAceptadas = new ArrayList<>();
}
