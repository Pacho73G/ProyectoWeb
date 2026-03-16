package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Incidente;

public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

    @Override
    @EntityGraph(attributePaths = {"turno", "docente", "zona"})
    java.util.List<Incidente> findAll();
}
