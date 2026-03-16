package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.RegistroLimpieza;

public interface RegistroLimpiezaRepository extends JpaRepository<RegistroLimpieza, Long> {

    @Override
    @EntityGraph(attributePaths = "turno")
    java.util.List<RegistroLimpieza> findAll();

    Optional<RegistroLimpieza> findByTurnoId(Long turnoId);
}
