/* Archivo documentado: Repositorio Spring Data JPA. Expone operaciones de acceso a datos para la entidad asociada sin duplicar SQL manual. */
package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.EstadoTurno;
import com.example.demo.model.Turno;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

    @Override
    @EntityGraph(attributePaths = {"docente", "zona"})
    List<Turno> findAll();

    // ── Turnos del día: usados por el endpoint /api/turnos/hoy ───────────────
    // El docente solo ve los turnos cuya fecha coincide con hoy.
    @EntityGraph(attributePaths = {"docente", "zona"})
    List<Turno> findByFecha(LocalDate fecha);

    // ── Cierre automático: turnos EN_CURSO que ya superaron su hora de fin ───
    // Usado por TurnoCierreScheduler cada minuto para cerrar turnos vencidos.
    @EntityGraph(attributePaths = {"docente", "zona"})
    List<Turno> findByEstadoAndFechaAndHoraFinLessThanEqual(
            EstadoTurno estado,
            LocalDate fecha,
            LocalTime horaFin
    );

    // Turnos EN_CURSO de días anteriores (olvidados sin cerrar)
    @EntityGraph(attributePaths = {"docente", "zona"})
    List<Turno> findByEstadoAndFechaLessThan(EstadoTurno estado, LocalDate fecha);

    // Turnos PENDIENTE cuyo abiertoEn ya llegó → enviar notificación de asignación.
    @EntityGraph(attributePaths = {"docente", "zona"})
    List<Turno> findByEstadoAndAbiertoEnIsNotNullAndAbiertoEnLessThanEqual(
            EstadoTurno estado, LocalDateTime abiertoEn);

    // Turnos PENDIENTE o EN_CURSO cuyo cerradoEn ya llegó → cerrar automáticamente.
    @EntityGraph(attributePaths = {"docente", "zona"})
    List<Turno> findByEstadoInAndCerradoEnIsNotNullAndCerradoEnLessThanEqual(
            List<EstadoTurno> estados, LocalDateTime cerradoEn);
}