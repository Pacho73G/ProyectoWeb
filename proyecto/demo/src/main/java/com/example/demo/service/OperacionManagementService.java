package com.example.demo.service;

import org.springframework.stereotype.Service;

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

    public Zona guardar(Zona entity) { return zonaRepository.save(entity); }
    public void eliminarZona(Long id) { zonaRepository.deleteById(id); }
    public Turno guardar(Turno entity) { return turnoRepository.save(entity); }
    public void eliminarTurno(Long id) { turnoRepository.deleteById(id); }
    public CheckIn guardar(CheckIn entity) { return checkInRepository.save(entity); }
    public void eliminarCheckIn(Long id) { checkInRepository.deleteById(id); }
    public Incidente guardar(Incidente entity) { return incidenteRepository.save(entity); }
    public void eliminarIncidente(Long id) { incidenteRepository.deleteById(id); }
    public Reasignacion guardar(Reasignacion entity) { return reasignacionRepository.save(entity); }
    public void eliminarReasignacion(Long id) { reasignacionRepository.deleteById(id); }
    public RegistroLimpieza guardar(RegistroLimpieza entity) { return registroLimpiezaRepository.save(entity); }
    public void eliminarLimpieza(Long id) { registroLimpiezaRepository.deleteById(id); }
    public Notificacion guardar(Notificacion entity) { return notificacionRepository.save(entity); }
    public void eliminarNotificacion(Long id) { notificacionRepository.deleteById(id); }
}
