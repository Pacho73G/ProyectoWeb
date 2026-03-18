package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.exception.RecursoDuplicadoException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.model.CheckIn;
import com.example.demo.model.Incidente;
import com.example.demo.model.Notificacion;
import com.example.demo.model.Reasignacion;
import com.example.demo.model.RegistroLimpieza;
import com.example.demo.model.Turno;
import com.example.demo.model.Zona;
import com.example.demo.repository.CheckInRepository;
import com.example.demo.repository.IncidenteRepository;
import com.example.demo.repository.NotificacionRepository;
import com.example.demo.repository.ReasignacionRepository;
import com.example.demo.repository.RegistroLimpiezaRepository;
import com.example.demo.repository.TurnoRepository;
import com.example.demo.repository.ZonaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperacionManagementService {

    private final ZonaRepository zonaRepository;
    private final TurnoRepository turnoRepository;
    private final CheckInRepository checkInRepository;
    private final IncidenteRepository incidenteRepository;
    private final ReasignacionRepository reasignacionRepository;
    private final RegistroLimpiezaRepository registroLimpiezaRepository;
    private final NotificacionRepository notificacionRepository;

    public Zona guardar(Zona entity) {
        validateZona(entity);
        return zonaRepository.save(entity);
    }

    public void eliminarZona(Long id) {
        ensureExists(zonaRepository.existsById(id), "La zona solicitada no existe.");
        zonaRepository.deleteById(id);
    }

    public Turno guardar(Turno entity) { return turnoRepository.save(entity); }

    public void eliminarTurno(Long id) {
        ensureExists(turnoRepository.existsById(id), "El turno solicitado no existe.");
        turnoRepository.deleteById(id);
    }

    public CheckIn guardar(CheckIn entity) {
        validateCheckIn(entity);
        return checkInRepository.save(entity);
    }

    public void eliminarCheckIn(Long id) {
        ensureExists(checkInRepository.existsById(id), "El check-in solicitado no existe.");
        checkInRepository.deleteById(id);
    }

    public Incidente guardar(Incidente entity) { return incidenteRepository.save(entity); }

    public void eliminarIncidente(Long id) {
        ensureExists(incidenteRepository.existsById(id), "El incidente solicitado no existe.");
        incidenteRepository.deleteById(id);
    }

    public Reasignacion guardar(Reasignacion entity) {
        validateReasignacion(entity);
        return reasignacionRepository.save(entity);
    }

    public void eliminarReasignacion(Long id) {
        ensureExists(reasignacionRepository.existsById(id), "La reasignación solicitada no existe.");
        reasignacionRepository.deleteById(id);
    }

    public RegistroLimpieza guardar(RegistroLimpieza entity) {
        validateLimpieza(entity);
        return registroLimpiezaRepository.save(entity);
    }

    public void eliminarLimpieza(Long id) {
        ensureExists(registroLimpiezaRepository.existsById(id), "El registro de limpieza solicitado no existe.");
        registroLimpiezaRepository.deleteById(id);
    }

    public Notificacion guardar(Notificacion entity) { return notificacionRepository.save(entity); }

    public void eliminarNotificacion(Long id) {
        ensureExists(notificacionRepository.existsById(id), "La notificación solicitada no existe.");
        notificacionRepository.deleteById(id);
    }

    private void validateZona(Zona entity) {
        boolean exists = entity.getId() == null
                ? zonaRepository.existsByNombreIgnoreCase(entity.getNombre())
                : zonaRepository.existsByNombreIgnoreCaseAndIdNot(entity.getNombre(), entity.getId());
        if (exists) {
            throw new RecursoDuplicadoException("La zona ya existe.");
        }
    }

    private void validateCheckIn(CheckIn entity) {
        Long turnoId = entity.getTurno() != null ? entity.getTurno().getId() : null;
        if (turnoId == null) {
            return;
        }
        boolean exists = entity.getId() == null
                ? checkInRepository.existsByTurnoId(turnoId)
                : checkInRepository.existsByTurnoIdAndIdNot(turnoId, entity.getId());
        if (exists) {
            throw new RecursoDuplicadoException("Ya existe un check-in para el turno seleccionado.");
        }
    }

    private void validateReasignacion(Reasignacion entity) {
        Long turnoId = entity.getTurno() != null ? entity.getTurno().getId() : null;
        if (turnoId == null) {
            return;
        }
        boolean exists = entity.getId() == null
                ? reasignacionRepository.existsByTurnoId(turnoId)
                : reasignacionRepository.existsByTurnoIdAndIdNot(turnoId, entity.getId());
        if (exists) {
            throw new RecursoDuplicadoException("Ya existe una reasignación para el turno seleccionado.");
        }
    }

    private void validateLimpieza(RegistroLimpieza entity) {
        Long turnoId = entity.getTurno() != null ? entity.getTurno().getId() : null;
        if (turnoId == null) {
            return;
        }
        boolean exists = entity.getId() == null
                ? registroLimpiezaRepository.existsByTurnoId(turnoId)
                : registroLimpiezaRepository.existsByTurnoIdAndIdNot(turnoId, entity.getId());
        if (exists) {
            throw new RecursoDuplicadoException("Ya existe un registro de limpieza para el turno seleccionado.");
        }
    }

    private void ensureExists(boolean exists, String message) {
        if (!exists) {
            throw new RecursoNoEncontradoException(message);
        }
    }
}
