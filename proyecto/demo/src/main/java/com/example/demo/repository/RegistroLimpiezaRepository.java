/* Archivo documentado: Repositorio Spring Data JPA. Expone operaciones de acceso a datos para la entidad asociada sin duplicar SQL manual. */
package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.RegistroLimpieza;

public interface RegistroLimpiezaRepository extends JpaRepository<RegistroLimpieza, Long> {

    @Override
    @EntityGraph(attributePaths = {"turno", "docente", "zona"})
    java.util.List<RegistroLimpieza> findAll();

    Optional<RegistroLimpieza> findByTurnoId(Long turnoId);

    boolean existsByTurnoId(Long turnoId);

    boolean existsByTurnoIdAndIdNot(Long turnoId, Long id);
}
