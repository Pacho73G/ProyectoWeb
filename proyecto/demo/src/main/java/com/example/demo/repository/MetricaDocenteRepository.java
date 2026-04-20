/* Archivo documentado: Repositorio Spring Data JPA. Expone operaciones de acceso a datos para la entidad asociada sin duplicar SQL manual. */
package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.MetricaDocente;

public interface MetricaDocenteRepository extends JpaRepository<MetricaDocente, Long> {

    @Override
    @EntityGraph(attributePaths = "docente")
    java.util.List<MetricaDocente> findAll();
}
