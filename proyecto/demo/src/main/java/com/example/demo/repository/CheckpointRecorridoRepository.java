/* Archivo documentado: Repositorio Spring Data JPA. Expone operaciones de acceso a datos para la entidad asociada sin duplicar SQL manual. */
package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CheckpointRecorrido;

public interface CheckpointRecorridoRepository extends JpaRepository<CheckpointRecorrido, Long> {

    @Override
    @EntityGraph(attributePaths = {"zona", "recorrido"})
    java.util.List<CheckpointRecorrido> findAll();
}
