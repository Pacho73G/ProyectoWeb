package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.MapaCalor;

public interface MapaCalorRepository extends JpaRepository<MapaCalor, Long> {

    @Override
    @EntityGraph(attributePaths = "zona")
    java.util.List<MapaCalor> findAll();
}
