package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    @Override
    @EntityGraph(attributePaths = "turno")
    java.util.List<Notificacion> findAll();
}
