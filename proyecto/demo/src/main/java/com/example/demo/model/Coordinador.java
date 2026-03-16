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
public class Coordinador extends Usuario {

    @Column(name = "nivel", nullable = false)
    private String nivel;
}
