package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "administradores")
public class Administrador extends Usuario {

    @Column(name = "cargo", nullable = false)
    private String cargo;
}
