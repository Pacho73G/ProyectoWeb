package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CheckIn;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    @Override
    @EntityGraph(attributePaths = {"turno", "turno.zona", "docente", "zona"})
    java.util.List<CheckIn> findAll();
}
