package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.MetricaDocente;

public interface MetricaDocenteRepository extends JpaRepository<MetricaDocente, Long> {

    @Override
    @EntityGraph(attributePaths = "docente")
    java.util.List<MetricaDocente> findAll();
}
