/* Archivo documentado: Repositorio Spring Data JPA. Expone operaciones de acceso a datos para la entidad asociada sin duplicar SQL manual. */
package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Recorrido;

public interface RecorridoRepository extends JpaRepository<Recorrido, Long> {

    @Override
    @EntityGraph(attributePaths = {"docente", "turno"})
    java.util.List<Recorrido> findAll();
}
