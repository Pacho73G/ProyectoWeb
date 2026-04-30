/* Archivo documentado: Repositorio Spring Data JPA. Expone operaciones de acceso a datos para la entidad asociada sin duplicar SQL manual. */
package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.TipoNotificacion;
import com.example.demo.model.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    @Override
    @EntityGraph(attributePaths = {"turno", "turno.docente", "turno.zona", "destinatario"})
    java.util.List<Notificacion> findAll();

    @EntityGraph(attributePaths = {"turno", "turno.docente", "turno.zona", "destinatario"})
    java.util.List<Notificacion> findAllByDestinatarioIdOrderByEnviadaEnDesc(Long destinatarioId);

    long countByDestinatarioIdAndLeidaFalse(Long destinatarioId);

    boolean existsByTurnoIdAndDestinatarioIdAndTipo(Long turnoId, Long destinatarioId, TipoNotificacion tipo);
}
