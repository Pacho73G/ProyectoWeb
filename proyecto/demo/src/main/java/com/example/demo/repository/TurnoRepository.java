package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Turno;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

    @Override
    @EntityGraph(attributePaths = {"docente", "zona"})
    java.util.List<Turno> findAll();
}
