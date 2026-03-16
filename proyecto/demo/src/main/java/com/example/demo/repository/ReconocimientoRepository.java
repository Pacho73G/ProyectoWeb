package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Reconocimiento;

public interface ReconocimientoRepository extends JpaRepository<Reconocimiento, Long> {

    @Override
    @EntityGraph(attributePaths = {"metricaDocente", "metricaDocente.docente"})
    java.util.List<Reconocimiento> findAll();
}
