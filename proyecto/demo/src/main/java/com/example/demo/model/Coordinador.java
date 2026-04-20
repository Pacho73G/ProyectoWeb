/* Archivo documentado: Entidad del dominio persistida con JPA. Modela una parte del sistema de vigilancia docente y su estado en base de datos. */
package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coordinadores")
/**
 * Perfil que supervisa cobertura, incidentes y reasignaciones.
 */
public class Coordinador extends Usuario {

    @Column(name = "nivel", nullable = false)
    private String nivel;
}
