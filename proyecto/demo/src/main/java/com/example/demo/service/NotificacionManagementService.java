package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.EstadoTurno;
import com.example.demo.model.Incidente;
import com.example.demo.model.Notificacion;
import com.example.demo.model.Reasignacion;
import com.example.demo.model.Recorrido;
import com.example.demo.model.RegistroLimpieza;
import com.example.demo.model.RolUsuario;
import com.example.demo.model.TipoNotificacion;
import com.example.demo.model.Turno;
import com.example.demo.model.Usuario;
import com.example.demo.repository.NotificacionRepository;
import com.example.demo.repository.TurnoRepository;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
/**
 * Centraliza la generación y consulta de notificaciones del sistema.
 * Aquí viven las reglas que convierten eventos operativos en avisos persistentes.
 */
public class NotificacionManagementService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final TurnoRepository turnoRepository;

    public List<Notificacion> notificacionesPorUsuario(Long usuarioId) {
        // Antes de listar, se actualizan alertas automáticas derivadas del estado de turnos.
        registrarAlertasAusenciaPendientes();
        return notificacionRepository.findAllByDestinatarioIdOrderByEnviadaEnDesc(usuarioId);
    }

    public long contarNoLeidas(Long usuarioId) {
        // El conteo también refleja alertas generadas dinámicamente por ausencias.
        registrarAlertasAusenciaPendientes();
        return notificacionRepository.countByDestinatarioIdAndLeidaFalse(usuarioId);
    }

    public void marcarComoLeidas(Long usuarioId) {
        List<Notificacion> notificaciones = notificacionRepository.findAllByDestinatarioIdOrderByEnviadaEnDesc(usuarioId);
        boolean changed = false;
        for (Notificacion notificacion : notificaciones) {
            // Se evita reescritura innecesaria sobre registros ya leídos.
            if (!Boolean.TRUE.equals(notificacion.getLeida())) {
                notificacion.setLeida(true);
                changed = true;
            }
        }
        if (changed) {
            notificacionRepository.saveAll(notificaciones);
        }
    }

    public void notificarAsignacionTurno(Turno turno) {
        crearNotificacion(
                turno,
                turno.getDocente(),
                TipoNotificacion.ASIGNACION_TURNO,
                "Nuevo turno asignado",
                "Se te asignó el turno " + turno.getFranja() + " para el " + turno.getFecha() + " en " + turno.getZona().getNombre() + ".",
                0
        );
    }

    public void notificarCheckIn(Turno turno) {
        crearNotificacion(
                turno,
                turno.getDocente(),
                TipoNotificacion.CONFIRMACION_CHECKIN,
                "Turno iniciado",
                "Registraste el inicio del turno " + turno.getFranja() + ".",
                0
        );

        String mensaje = turno.getDocente().getNombre() + " inició el turno " + turno.getFranja() + " en " + turno.getZona().getNombre() + ".";
        notificarRol(turno, RolUsuario.COORDINADOR, TipoNotificacion.CONFIRMACION_CHECKIN, "Check-in de docente", mensaje, false);
        notificarRol(turno, RolUsuario.ADMINISTRADOR, TipoNotificacion.CONFIRMACION_CHECKIN, "Check-in de docente", mensaje, false);
    }

    public void notificarCierreTurno(Turno turno) {
        crearNotificacion(
                turno,
                turno.getDocente(),
                TipoNotificacion.CIERRE_TURNO,
                "Turno finalizado",
                "Finalizaste el turno " + turno.getFranja() + ".",
                0
        );

        String mensaje = turno.getDocente().getNombre() + " finalizó el turno " + turno.getFranja() + " en " + turno.getZona().getNombre() + ".";
        notificarRol(turno, RolUsuario.COORDINADOR, TipoNotificacion.CIERRE_TURNO, "Cierre de turno", mensaje, false);
        notificarRol(turno, RolUsuario.ADMINISTRADOR, TipoNotificacion.CIERRE_TURNO, "Cierre de turno", mensaje, false);
    }

    public void notificarIncidente(Incidente incidente) {
        // Un incidente puede existir con o sin turno; el mensaje debe cubrir ambos casos.
        String contexto = incidente.getTurno() != null
                ? " durante el turno " + incidente.getTurno().getFranja()
                : " fuera de turno";
        String mensaje = incidente.getDocente().getNombre() + " registró un reporte en " + incidente.getZona().getNombre()
                + contexto + ".";
        notificarRol(incidente.getTurno(), RolUsuario.COORDINADOR, TipoNotificacion.REPORTE_INCIDENTE, "Nuevo reporte docente", mensaje, false);
        notificarRol(incidente.getTurno(), RolUsuario.ADMINISTRADOR, TipoNotificacion.REPORTE_INCIDENTE, "Nuevo reporte docente", mensaje, false);
    }

    public void notificarAsignacionLimpieza(RegistroLimpieza limpieza) {
        // La limpieza puede venir asociada a turno o ser una asignación independiente.
        String mensaje = "Se te asignó una limpieza en " + limpieza.getZona().getNombre()
                + (limpieza.getTurno() != null ? " para el turno " + limpieza.getTurno().getFranja() + "." : ".");
        crearNotificacion(
                limpieza.getTurno(),
                limpieza.getDocente(),
                TipoNotificacion.ASIGNACION_LIMPIEZA,
                "Nueva limpieza asignada",
                mensaje,
                0
        );
    }

    public void notificarRecorrido(Recorrido recorrido) {
        String mensaje = recorrido.getDocente().getNombre() + " registró un recorrido del turno " + recorrido.getTurno().getFranja() + ".";
        notificarRol(recorrido.getTurno(), RolUsuario.COORDINADOR, TipoNotificacion.REGISTRO_RECORRIDO, "Nuevo recorrido", mensaje, false);
        notificarRol(recorrido.getTurno(), RolUsuario.ADMINISTRADOR, TipoNotificacion.REGISTRO_RECORRIDO, "Nuevo recorrido", mensaje, false);
    }

    public void notificarReasignacion(Reasignacion reasignacion) {
        String mensaje = reasignacion.getDocenteSolicitante().getNombre() + " solicitó reasignación para el turno "
                + reasignacion.getTurno().getFranja() + ".";
        notificarRol(reasignacion.getTurno(), RolUsuario.COORDINADOR, TipoNotificacion.PROPUESTA_REEMPLAZO, "Solicitud de reasignación", mensaje, false);
        notificarRol(reasignacion.getTurno(), RolUsuario.ADMINISTRADOR, TipoNotificacion.PROPUESTA_REEMPLAZO, "Solicitud de reasignación", mensaje, false);
    }

    public void notificarRespuestaReasignacion(Reasignacion reasignacion) {
        if (reasignacion.getDocenteSolicitante() == null || reasignacion.getDocenteReemplazo() == null) {
            return;
        }
        String mensaje = reasignacion.getDocenteReemplazo().getNombre() + " respondió " + reasignacion.getEstado().name()
                + " a la solicitud del turno " + reasignacion.getTurno().getFranja() + ".";
        crearNotificacion(
                reasignacion.getTurno(),
                reasignacion.getDocenteSolicitante(),
                TipoNotificacion.PROPUESTA_REEMPLAZO,
                "Respuesta de reasignación",
                mensaje,
                0
        );
    }

    public void registrarAlertasAusenciaPendientes() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Turno> turnos = turnoRepository.findAll();

        for (Turno turno : turnos) {
            // Solo interesa detectar turnos pendientes que ya debieron iniciar.
            if (turno.getEstado() != EstadoTurno.PENDIENTE) {
                continue;
            }

            LocalDate fecha = turno.getFecha();
            if (fecha == null) {
                continue;
            }

            LocalDateTime inicio = LocalDateTime.of(fecha, turno.getHoraInicio());
            if (!inicio.isBefore(ahora)) {
                continue;
            }

            // Cuando el turno ya venció sin apertura, se marca sin cobertura y se alerta.
            turno.setEstado(EstadoTurno.SIN_COBERTURA);
            turnoRepository.save(turno);

            String mensaje = "El docente " + turno.getDocente().getNombre() + " no inició el turno " + turno.getFranja()
                    + " en " + turno.getZona().getNombre() + ".";
            notificarRol(turno, RolUsuario.COORDINADOR, TipoNotificacion.ALERTA_AUSENCIA, "Alerta de ausencia", mensaje, true);
            notificarRol(turno, RolUsuario.ADMINISTRADOR, TipoNotificacion.ALERTA_AUSENCIA, "Alerta de ausencia", mensaje, true);
        }
    }

    private void notificarRol(Turno turno, RolUsuario rol, TipoNotificacion tipo, String titulo, String mensaje, boolean evitarDuplicado) {
        for (Usuario destinatario : usuarioRepository.findAllByRol(rol)) {
            // Se crea una notificación por cada usuario del rol destino.
            crearNotificacion(turno, destinatario, tipo, titulo, mensaje, 0, evitarDuplicado);
        }
    }

    private void crearNotificacion(Turno turno, Usuario destinatario, TipoNotificacion tipo, String titulo, String mensaje, Integer minutosAnticipacion) {
        crearNotificacion(turno, destinatario, tipo, titulo, mensaje, minutosAnticipacion, false);
    }

    private void crearNotificacion(Turno turno, Usuario destinatario, TipoNotificacion tipo, String titulo, String mensaje,
                                   Integer minutosAnticipacion, boolean evitarDuplicado) {
        if (destinatario == null) {
            return;
        }
        // Las alertas automáticas no deben multiplicarse al refrescar o volver a consultar.
        if (evitarDuplicado && turno != null
                && notificacionRepository.existsByTurnoIdAndDestinatarioIdAndTipo(turno.getId(), destinatario.getId(), tipo)) {
            return;
        }

        // Toda notificación queda persistida para que sea compartida entre sesiones y roles.
        Notificacion entity = new Notificacion();
        entity.setTurno(turno);
        entity.setDestinatario(destinatario);
        entity.setTipo(tipo);
        entity.setTitulo(titulo);
        entity.setMensaje(mensaje);
        entity.setEnviadaEn(LocalDateTime.now());
        entity.setLeida(false);
        entity.setMinutosAnticipacion(minutosAnticipacion != null ? minutosAnticipacion : 0);
        notificacionRepository.save(entity);
    }
}
