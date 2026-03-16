package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Coordinador;

public interface CoordinadorRepository extends JpaRepository<Coordinador, Long> {
}
