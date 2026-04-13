package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "administradores")
/**
 * Perfil encargado de estructura del sistema: usuarios, zonas, turnos y configuración.
 */
public class Administrador extends Usuario {

    @Column(name = "cargo", nullable = false)
    private String cargo;

    @OneToOne(mappedBy = "administrador", fetch = FetchType.LAZY)
    private ConfiguracionSistema configuracionSistema;
}
