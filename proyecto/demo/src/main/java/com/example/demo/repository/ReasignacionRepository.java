/* Archivo documentado: Repositorio Spring Data JPA. Expone operaciones de acceso a datos para la entidad asociada sin duplicar SQL manual. */
package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Reasignacion;

public interface ReasignacionRepository extends JpaRepository<Reasignacion, Long> {

    @Override
    @EntityGraph(attributePaths = {"turno", "docenteSolicitante", "docenteReemplazo"})
    java.util.List<Reasignacion> findAll();

    boolean existsByTurnoId(Long turnoId);

    boolean existsByTurnoIdAndIdNot(Long turnoId, Long id);
}
