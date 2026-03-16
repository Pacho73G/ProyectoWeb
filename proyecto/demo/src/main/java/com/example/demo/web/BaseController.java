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
import com.example.demo.service.CatalogQueryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseController {

    protected final CatalogQueryService catalogQueryService;

    protected void addShared(Model model) {
        model.addAttribute("usuarios", catalogQueryService.usuarios());
        model.addAttribute("docentes", catalogQueryService.docentes());
        model.addAttribute("coordinadores", catalogQueryService.coordinadores());
        model.addAttribute("administradores", catalogQueryService.administradores());
        model.addAttribute("configuraciones", catalogQueryService.configuraciones());
        model.addAttribute("zonas", catalogQueryService.zonas());
        model.addAttribute("turnos", catalogQueryService.turnos());
        model.addAttribute("checkins", catalogQueryService.checkIns());
        model.addAttribute("incidentes", catalogQueryService.incidentes());
        model.addAttribute("reasignaciones", catalogQueryService.reasignaciones());
        model.addAttribute("limpiezas", catalogQueryService.limpiezas());
        model.addAttribute("notificaciones", catalogQueryService.notificaciones());
        model.addAttribute("recorridos", catalogQueryService.recorridos());
        model.addAttribute("checkpoints", catalogQueryService.checkpoints());
        model.addAttribute("mapasCalor", catalogQueryService.mapasCalor());
        model.addAttribute("metricas", catalogQueryService.metricas());
        model.addAttribute("reconocimientos", catalogQueryService.reconocimientos());
        model.addAttribute("rolesUsuario", RolUsuario.values());
        model.addAttribute("estadosTurno", EstadoTurno.values());
        model.addAttribute("tiposIncidente", TipoIncidente.values());
        model.addAttribute("severidadesIncidente", SeveridadIncidente.values());
        model.addAttribute("metodosCheckIn", MetodoCheckIn.values());
        model.addAttribute("estadosReasignacion", EstadoReasignacion.values());
        model.addAttribute("tiposNotificacion", TipoNotificacion.values());
        model.addAttribute("estadosRecorrido", EstadoRecorrido.values());
        model.addAttribute("tiposReconocimiento", TipoReconocimiento.values());
        model.addAttribute("totalDocentes", catalogQueryService.totalDocentes());
        model.addAttribute("totalTurnos", catalogQueryService.totalTurnos());
        model.addAttribute("totalIncidentes", catalogQueryService.totalIncidentes());
        model.addAttribute("totalReasignaciones", catalogQueryService.totalReasignaciones());
        model.addAttribute("totalRecorridos", catalogQueryService.totalRecorridos());
        model.addAttribute("totalReconocimientos", catalogQueryService.totalReconocimientos());
    }

    protected String prepareForm(Model model, String template, Object form, String titulo, String accion) {
        addShared(model);
        model.addAttribute("form", form);
        model.addAttribute("titulo", titulo);
        model.addAttribute("accion", accion);
        return template;
    }
}
