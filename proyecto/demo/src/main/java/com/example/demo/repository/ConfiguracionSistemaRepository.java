package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ConfiguracionSistema;

public interface ConfiguracionSistemaRepository extends JpaRepository<ConfiguracionSistema, Long> {

    @Override
    @EntityGraph(attributePaths = "administrador")
    java.util.List<ConfiguracionSistema> findAll();
}
