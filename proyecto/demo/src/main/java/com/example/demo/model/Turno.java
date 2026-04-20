/* Archivo documentado: Entidad del dominio persistida con JPA. Modela una parte del sistema de vigilancia docente y su estado en base de datos. */
package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "turnos")
/**
 * Unidad central de operación: asigna un docente a una zona en una franja y fecha concretas.
 */
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "franja", nullable = false)
    private String franja;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTurno estado = EstadoTurno.PENDIENTE;

    @Column(name = "abierto_en")
    private LocalDateTime abiertoEn;

    @Column(name = "cerrado_en")
    private LocalDateTime cerradoEn;

    @OneToOne(mappedBy = "turno", fetch = FetchType.LAZY)
    private CheckIn checkIn;

    @OneToOne(mappedBy = "turno", fetch = FetchType.LAZY)
    private Reasignacion reasignacion;

    @OneToOne(mappedBy = "turno", fetch = FetchType.LAZY)
    private RegistroLimpieza registroLimpieza;

    @OneToMany(mappedBy = "turno", fetch = FetchType.LAZY)
    private List<Incidente> incidentes = new ArrayList<>();

    @OneToMany(mappedBy = "turno", fetch = FetchType.LAZY)
    private List<Recorrido> recorridos = new ArrayList<>();

    @OneToMany(mappedBy = "turno", fetch = FetchType.LAZY)
    private List<Notificacion> notificaciones = new ArrayList<>();
}
