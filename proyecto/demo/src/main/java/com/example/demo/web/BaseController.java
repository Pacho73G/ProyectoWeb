package com.example.demo.web;

import org.springframework.ui.Model;

import com.example.demo.model.EstadoReasignacion;
import com.example.demo.model.EstadoRecorrido;
import com.example.demo.model.EstadoTurno;
import com.example.demo.model.MetodoCheckIn;
import com.example.demo.model.RolUsuario;
import com.example.demo.model.SeveridadIncidente;
import com.example.demo.model.TipoIncidente;
import com.example.demo.model.TipoNotificacion;
import com.example.demo.model.TipoReconocimiento;
import com.example.demo.service.SistemaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseController {

    protected final SistemaService sistemaService;

    protected void addShared(Model model) {
        model.addAttribute("usuarios", sistemaService.usuarios());
        model.addAttribute("docentes", sistemaService.docentes());
        model.addAttribute("coordinadores", sistemaService.coordinadores());
        model.addAttribute("administradores", sistemaService.administradores());
        model.addAttribute("configuraciones", sistemaService.configuraciones());
        model.addAttribute("zonas", sistemaService.zonas());
        model.addAttribute("turnos", sistemaService.turnos());
        model.addAttribute("checkins", sistemaService.checkIns());
        model.addAttribute("incidentes", sistemaService.incidentes());
        model.addAttribute("reasignaciones", sistemaService.reasignaciones());
        model.addAttribute("limpiezas", sistemaService.limpiezas());
        model.addAttribute("notificaciones", sistemaService.notificaciones());
        model.addAttribute("recorridos", sistemaService.recorridos());
        model.addAttribute("checkpoints", sistemaService.checkpoints());
        model.addAttribute("mapasCalor", sistemaService.mapasCalor());
        model.addAttribute("metricas", sistemaService.metricas());
        model.addAttribute("reconocimientos", sistemaService.reconocimientos());
        model.addAttribute("rolesUsuario", RolUsuario.values());
        model.addAttribute("estadosTurno", EstadoTurno.values());
        model.addAttribute("tiposIncidente", TipoIncidente.values());
        model.addAttribute("severidadesIncidente", SeveridadIncidente.values());
        model.addAttribute("metodosCheckIn", MetodoCheckIn.values());
        model.addAttribute("estadosReasignacion", EstadoReasignacion.values());
        model.addAttribute("tiposNotificacion", TipoNotificacion.values());
        model.addAttribute("estadosRecorrido", EstadoRecorrido.values());
        model.addAttribute("tiposReconocimiento", TipoReconocimiento.values());
        model.addAttribute("totalDocentes", sistemaService.totalDocentes());
        model.addAttribute("totalTurnos", sistemaService.totalTurnos());
        model.addAttribute("totalIncidentes", sistemaService.totalIncidentes());
        model.addAttribute("totalReasignaciones", sistemaService.totalReasignaciones());
        model.addAttribute("totalRecorridos", sistemaService.totalRecorridos());
        model.addAttribute("totalReconocimientos", sistemaService.totalReconocimientos());
    }
}
