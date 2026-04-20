/* Archivo documentado: Repositorio Spring Data JPA. Expone operaciones de acceso a datos para la entidad asociada sin duplicar SQL manual. */
package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Reconocimiento;

public interface ReconocimientoRepository extends JpaRepository<Reconocimiento, Long> {

    @Override
    @EntityGraph(attributePaths = {"metricaDocente", "metricaDocente.docente"})
    java.util.List<Reconocimiento> findAll();
}
