package com.example.demo.scheduler;

import com.example.demo.model.EstadoTurno;
import com.example.demo.model.Turno;
import com.example.demo.repository.TurnoRepository;
import com.example.demo.service.NotificacionManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Scheduler que cierra automáticamente los turnos EN_CURSO cuando llega su hora de fin.
 *
 * <p>Se ejecuta cada minuto. Por cada turno EN_CURSO cuya {horaFin} ya pasó
 * (hoy o en días anteriores que quedaron olvidados), hace:
 * <ol>
 *   <li>Cambia el estado a { CERRADO}.</li>
 *   <li>Registra { cerradoEn} con la hora exacta del cierre automático.</li>
 *   <li>Dispara { notificarCierreTurno()} para avisar al docente, coordinadores
 *       y administradores.</li>
 * </ol>
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TurnoCierreScheduler {

    private final TurnoRepository turnoRepository;
    private final NotificacionManagementService notificacionManagementService;

    /**
     * Se ejecuta al inicio del minuto (:00 de cada minuto).
     * fixedDelay garantiza que una ejecución no empiece hasta que
     * la anterior haya terminado, evitando solapamientos.
     */
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void cerrarTurnosVencidos() {
        LocalDate hoy   = LocalDate.now();
        LocalTime ahora = LocalTime.now();
        LocalDateTime ahoraDateTime = LocalDateTime.now();

        // ── 1. Enviar notificación de asignación cuando llega abiertoEn ──────
        // El POST de creación ya NO dispara la notificación; lo hace este scheduler.
        List<Turno> listoParaNotificar = turnoRepository
                .findByEstadoAndAbiertoEnIsNotNullAndAbiertoEnLessThanEqual(
                        EstadoTurno.PENDIENTE, ahoraDateTime);

        for (Turno turno : listoParaNotificar) {
            try {
                // notificarAsignacionTurno ya tiene evitarDuplicado=true, es seguro llamarlo.
                notificacionManagementService.notificarAsignacionTurno(turno);
                log.info("[TurnoCierreScheduler] Notificación de asignación enviada para turno #{} '{}'.",
                        turno.getId(), turno.getFranja());
            } catch (Exception e) {
                log.error("[TurnoCierreScheduler] Error al notificar turno #{}: {}",
                        turno.getId(), e.getMessage(), e);
            }
        }

        // ── 2. Cerrar turnos cuyo cerradoEn ya llegó (definido por el admin) ─
        List<Turno> porCerradoEn = turnoRepository
                .findByEstadoInAndCerradoEnIsNotNullAndCerradoEnLessThanEqual(
                        List.of(EstadoTurno.PENDIENTE, EstadoTurno.EN_CURSO), ahoraDateTime);

        // ── 3. Turnos EN_CURSO de HOY cuya horaFin ya pasó (seguridad adicional) ─
        List<Turno> vencidosHoy = turnoRepository
                .findByEstadoAndFechaAndHoraFinLessThanEqual(
                        EstadoTurno.EN_CURSO, hoy, ahora);

        // ── 4. Turnos EN_CURSO de DÍAS ANTERIORES (olvidados sin cerrar) ─────
        List<Turno> vencidosAnteriores = turnoRepository
                .findByEstadoAndFechaLessThan(EstadoTurno.EN_CURSO, hoy);

        var turnosACerrar = new java.util.ArrayList<>(porCerradoEn);
        for (Turno t : vencidosHoy) {
            if (turnosACerrar.stream().noneMatch(x -> x.getId().equals(t.getId()))) {
                turnosACerrar.add(t);
            }
        }
        for (Turno t : vencidosAnteriores) {
            if (turnosACerrar.stream().noneMatch(x -> x.getId().equals(t.getId()))) {
                turnosACerrar.add(t);
            }
        }

        if (!turnosACerrar.isEmpty()) {
            log.info("[TurnoCierreScheduler] Cerrando {} turno(s) vencido(s).", turnosACerrar.size());
        }

        for (Turno turno : turnosACerrar) {
            try {
                turno.setEstado(EstadoTurno.CERRADO);
                // Solo registrar cerradoEn real si el admin no puso uno (o ya pasó).
                if (turno.getCerradoEn() == null || turno.getCerradoEn().isAfter(ahoraDateTime)) {
                    turno.setCerradoEn(ahoraDateTime);
                }
                turnoRepository.save(turno);
                notificacionManagementService.notificarCierreTurno(turno);
                log.info("[TurnoCierreScheduler] Turno #{} '{}' cerrado automáticamente.",
                        turno.getId(), turno.getFranja());
            } catch (Exception e) {
                log.error("[TurnoCierreScheduler] Error al cerrar turno #{}: {}",
                        turno.getId(), e.getMessage(), e);
            }
        }
    }
}
