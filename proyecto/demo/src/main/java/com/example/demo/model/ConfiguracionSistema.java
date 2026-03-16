package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "configuraciones_sistema")
public class ConfiguracionSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "administrador_id", nullable = false, unique = true)
    private Administrador administrador;

    @Column(name = "minutos_alerta_ausencia", nullable = false)
    private Integer minutosAlertaAusencia;

    @Column(name = "segundos_ventana_reasignacion", nullable = false)
    private Integer segundosVentanaReasignacion;

    @Column(name = "minutos_inactividad", nullable = false)
    private Integer minutosInactividad;

    @Column(name = "umbral_ingreso", nullable = false)
    private Integer umbralIngreso;

    @Column(name = "minutos_recordatorio_1", nullable = false)
    private Integer minutosRecordatorio1;

    @Column(name = "minutos_recordatorio_2", nullable = false)
    private Integer minutosRecordatorio2;
}
