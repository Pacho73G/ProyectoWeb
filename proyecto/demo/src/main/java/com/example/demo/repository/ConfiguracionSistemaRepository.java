/* Archivo documentado: Repositorio Spring Data JPA. Expone operaciones de acceso a datos para la entidad asociada sin duplicar SQL manual. */
package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ConfiguracionSistema;

public interface ConfiguracionSistemaRepository extends JpaRepository<ConfiguracionSistema, Long> {

    @Override
    @EntityGraph(attributePaths = "administrador")
    java.util.List<ConfiguracionSistema> findAll();

    boolean existsByAdministradorId(Long administradorId);

    boolean existsByAdministradorIdAndIdNot(Long administradorId, Long id);
}
