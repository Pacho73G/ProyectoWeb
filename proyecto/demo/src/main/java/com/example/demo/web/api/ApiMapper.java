package com.example.demo.web.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.example.demo.model.Administrador;
import com.example.demo.model.CheckIn;
import com.example.demo.model.CheckpointRecorrido;
import com.example.demo.model.ConfiguracionSistema;
import com.example.demo.model.Coordinador;
import com.example.demo.model.Docente;
import com.example.demo.model.EstadoReasignacion;
import com.example.demo.model.EstadoRecorrido;
import com.example.demo.model.EstadoTurno;
import com.example.demo.model.Incidente;
import com.example.demo.model.MapaCalor;
import com.example.demo.model.MetodoCheckIn;
import com.example.demo.model.MetricaDocente;
import com.example.demo.model.Notificacion;
import com.example.demo.model.Reasignacion;
import com.example.demo.model.Reconocimiento;
import com.example.demo.model.Recorrido;
import com.example.demo.model.RegistroLimpieza;
import com.example.demo.model.RolUsuario;
import com.example.demo.model.SeveridadIncidente;
import com.example.demo.model.TipoIncidente;
import com.example.demo.model.TipoNotificacion;
import com.example.demo.model.TipoReconocimiento;
import com.example.demo.model.Turno;
import com.example.demo.model.Usuario;
import com.example.demo.model.Zona;
import com.example.demo.service.CatalogQueryService;

final class ApiMapper {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME = DateTimeFormatter.ISO_LOCAL_TIME;
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private ApiMapper() {}

    static ZonaDTO toDto(Zona entity) {
        return new ZonaDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getUbicacion(),
                entity.getCapacidadMaxima(),
                entity.getActiva()
        );
    }

    static TurnoDTO toDto(Turno entity) {
        return new TurnoDTO(
                entity.getId(),
                entity.getDocente() != null ? entity.getDocente().getId() : null,
                entity.getDocente() != null ? entity.getDocente().getNombre() : null,
                entity.getDocente() != null ? entity.getDocente().getEmail() : null,
                entity.getZona() != null ? entity.getZona().getId() : null,
                entity.getZona() != null ? entity.getZona().getNombre() : null,
                entity.getZona() != null ? entity.getZona().getUbicacion() : null,
                format(entity.getFecha()),
                format(entity.getHoraInicio()),
                format(entity.getHoraFin()),
                entity.getFranja(),
                enumName(entity.getEstado())
        );
    }

    static IncidenteDTO toDto(Incidente entity) {
        return new IncidenteDTO(
                entity.getId(),
                entity.getTurno() != null ? entity.getTurno().getId() : null,
                entity.getDocente() != null ? entity.getDocente().getId() : null,
                entity.getDocente() != null ? entity.getDocente().getNombre() : null,
                entity.getZona() != null ? entity.getZona().getId() : null,
                entity.getZona() != null ? entity.getZona().getNombre() : null,
                enumName(entity.getTipo()),
                enumName(entity.getSeveridad()),
                entity.getDescripcion(),
                entity.getObservacionSocial(),
                format(entity.getRegistradoEn()),
                entity.getRequiereSeguimiento()
        );
    }

    static CheckInDTO toDto(CheckIn entity) {
        return new CheckInDTO(
                entity.getId(),
                entity.getTurno() != null ? entity.getTurno().getId() : null,
                entity.getTurno() != null ? entity.getTurno().getFranja() : null,
                entity.getDocente() != null ? entity.getDocente().getId() : null,
                entity.getDocente() != null ? entity.getDocente().getNombre() : null,
                entity.getZona() != null ? entity.getZona().getId() : null,
                entity.getZona() != null ? entity.getZona().getNombre() : null,
                format(entity.getTimestamp()),
                enumName(entity.getMetodo()),
                entity.getEvidencia(),
                entity.getValido()
        );
    }

    static ReasignacionDTO toDto(Reasignacion entity) {
        return new ReasignacionDTO(
                entity.getId(),
                entity.getTurno() != null ? entity.getTurno().getId() : null,
                entity.getTurno() != null ? entity.getTurno().getFranja() : null,
                entity.getTurno() != null && entity.getTurno().getZona() != null ? entity.getTurno().getZona().getNombre() : null,
                entity.getDocenteSolicitante() != null ? entity.getDocenteSolicitante().getId() : null,
                entity.getDocenteSolicitante() != null ? entity.getDocenteSolicitante().getNombre() : null,
                entity.getDocenteReemplazo() != null ? entity.getDocenteReemplazo().getId() : null,
                entity.getDocenteReemplazo() != null ? entity.getDocenteReemplazo().getNombre() : null,
                entity.getMotivo(),
                enumName(entity.getEstado()),
                format(entity.getPropuestaEn()),
                format(entity.getRespondidaEn()),
                entity.getSegundosVentana()
        );
    }

    static LimpiezaDTO toDto(RegistroLimpieza entity) {
        return new LimpiezaDTO(
                entity.getId(),
                entity.getTurno() != null ? entity.getTurno().getId() : null,
                entity.getTurno() != null ? entity.getTurno().getFranja() : null,
                entity.getEscala(),
                entity.getObservaciones(),
                format(entity.getRegistradoEn())
        );
    }

    static NotificacionDTO toDto(Notificacion entity) {
        return new NotificacionDTO(
                entity.getId(),
                entity.getTurno() != null ? entity.getTurno().getId() : null,
                entity.getTurno() != null ? entity.getTurno().getFranja() : null,
                enumName(entity.getTipo()),
                entity.getMensaje(),
                format(entity.getEnviadaEn()),
                entity.getLeida(),
                entity.getMinutosAnticipacion()
        );
    }

    static MapaCalorDTO toDto(MapaCalor entity) {
        return new MapaCalorDTO(
                entity.getId(),
                entity.getZona() != null ? entity.getZona().getId() : null,
                entity.getZona() != null ? entity.getZona().getNombre() : null,
                entity.getFranja(),
                enumName(entity.getTipoIncidente()),
                entity.getTotalIncidentes(),
                entity.getPorcentaje() != null ? entity.getPorcentaje().doubleValue() : null,
                format(entity.getPeriodoInicio()),
                format(entity.getPeriodoFin())
        );
    }

    static MetricaDTO toDto(MetricaDocente entity) {
        return new MetricaDTO(
                entity.getId(),
                entity.getDocente() != null ? entity.getDocente().getId() : null,
                entity.getDocente() != null ? entity.getDocente().getNombre() : null,
                entity.getPuntualidad() != null ? entity.getPuntualidad().doubleValue() : null,
                entity.getCobertura() != null ? entity.getCobertura().doubleValue() : null,
                entity.getRetrasos(),
                entity.getRecorridosCompletados(),
                entity.getIncidentesRegistrados(),
                entity.getReasignacionesAceptadas(),
                entity.getPuntajeTotal() != null ? entity.getPuntajeTotal().doubleValue() : null,
                entity.getPeriodo()
        );
    }

    static ReconocimientoDTO toDto(Reconocimiento entity) {
        return new ReconocimientoDTO(
                entity.getId(),
                entity.getMetricaDocente() != null ? entity.getMetricaDocente().getId() : null,
                entity.getMetricaDocente() != null && entity.getMetricaDocente().getDocente() != null ? entity.getMetricaDocente().getDocente().getNombre() : null,
                entity.getTitulo(),
                entity.getDescripcion(),
                enumName(entity.getTipo()),
                format(entity.getOtorgadoEn()),
                entity.getTrimestre()
        );
    }

    static RecorridoDTO toDto(Recorrido entity) {
        return new RecorridoDTO(
                entity.getId(),
                entity.getDocente() != null ? entity.getDocente().getId() : null,
                entity.getDocente() != null ? entity.getDocente().getNombre() : null,
                entity.getTurno() != null ? entity.getTurno().getId() : null,
                entity.getTurno() != null ? entity.getTurno().getFranja() : null,
                format(entity.getIniciadoEn()),
                format(entity.getFinalizadoEn()),
                enumName(entity.getEstado()),
                entity.getDuracionMinutos()
        );
    }

    static CheckpointDTO toDto(CheckpointRecorrido entity) {
        return new CheckpointDTO(
                entity.getId(),
                entity.getZona() != null ? entity.getZona().getId() : null,
                entity.getZona() != null ? entity.getZona().getNombre() : null,
                entity.getRecorrido() != null ? entity.getRecorrido().getId() : null,
                entity.getCodigoQR(),
                entity.getDescripcion(),
                entity.getOrden(),
                format(entity.getEscaneadoEn())
        );
    }

    static UsuarioDTO toDto(Usuario entity) {
        return new UsuarioDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getEmail(),
                entity.getActivo(),
                enumName(entity.getRol()),
                descriptor(entity),
                entity instanceof Docente docente ? docente.getCargaActual() : null,
                entity instanceof Docente docente ? docente.getPuntajeGamificacion() : null
        );
    }

    static ConfiguracionDTO toDto(ConfiguracionSistema entity) {
        return new ConfiguracionDTO(
                entity.getId(),
                entity.getAdministrador() != null ? entity.getAdministrador().getId() : null,
                entity.getAdministrador() != null ? entity.getAdministrador().getNombre() : null,
                entity.getMinutosAlertaAusencia(),
                entity.getSegundosVentanaReasignacion(),
                entity.getMinutosInactividad(),
                entity.getUmbralIngreso(),
                entity.getMinutosRecordatorio1(),
                entity.getMinutosRecordatorio2()
        );
    }

    static Zona apply(ZonaRequest request, Zona entity) {
        entity.setNombre(request.nombre());
        entity.setDescripcion(defaultString(request.descripcion()));
        entity.setUbicacion(request.ubicacion());
        entity.setCapacidadMaxima(request.capacidadMaxima());
        entity.setActiva(request.activa() != null ? request.activa() : Boolean.TRUE);
        return entity;
    }

    static Turno apply(TurnoRequest request, Turno entity, CatalogQueryService queryService) {
        entity.setDocente(queryService.docente(request.docenteId()));
        entity.setZona(queryService.zona(request.zonaId()));
        entity.setFecha(parseDate(request.fecha()));
        entity.setHoraInicio(parseTime(request.horaInicio()));
        entity.setHoraFin(parseTime(request.horaFin()));
        entity.setFranja(request.franja());
        entity.setEstado(parseEstadoTurno(request.estado()));
        entity.setAbiertoEn(resolveDateTime(request.abiertoEn(), entity.getAbiertoEn()));
        entity.setCerradoEn(resolveDateTime(request.cerradoEn(), entity.getCerradoEn()));
        return entity;
    }

    static Incidente apply(IncidenteRequest request, Incidente entity, CatalogQueryService queryService) {
        entity.setTurno(queryService.turno(request.turnoId()));
        entity.setDocente(queryService.docente(request.docenteId()));
        entity.setZona(queryService.zona(request.zonaId()));
        entity.setTipo(parseTipoIncidente(request.tipo()));
        entity.setSeveridad(parseSeveridad(request.severidad()));
        entity.setDescripcion(defaultString(request.descripcion()));
        entity.setObservacionSocial(request.observacionSocial());
        entity.setRegistradoEn(resolveDateTimeOrNow(request.registradoEn(), entity.getRegistradoEn()));
        entity.setRequiereSeguimiento(request.requiereSeguimiento() != null ? request.requiereSeguimiento() : Boolean.FALSE);
        return entity;
    }

    static CheckIn apply(CheckInRequest request, CheckIn entity, CatalogQueryService queryService) {
        entity.setTurno(queryService.turno(request.turnoId()));
        entity.setDocente(queryService.docente(request.docenteId()));
        entity.setZona(queryService.zona(request.zonaId()));
        entity.setTimestamp(resolveDateTimeOrNow(request.timestamp(), entity.getTimestamp()));
        entity.setMetodo(parseMetodoCheckIn(request.metodo()));
        entity.setEvidencia(request.evidencia());
        entity.setValido(request.valido() != null ? request.valido() : Boolean.TRUE);
        return entity;
    }

    static Reasignacion apply(ReasignacionRequest request, Reasignacion entity, CatalogQueryService queryService) {
        entity.setTurno(queryService.turno(request.turnoId()));
        entity.setDocenteSolicitante(queryService.docente(request.docenteSolicitanteId()));
        entity.setDocenteReemplazo(request.docenteReemplazoId() != null ? queryService.docente(request.docenteReemplazoId()) : null);
        entity.setMotivo(defaultString(request.motivo()));
        entity.setEstado(parseEstadoReasignacion(request.estado()));
        entity.setPropuestaEn(resolveDateTimeOrNow(request.propuestaEn(), entity.getPropuestaEn()));
        entity.setRespondidaEn(resolveDateTime(request.respondidaEn(), entity.getRespondidaEn()));
        entity.setSegundosVentana(request.segundosVentana());
        return entity;
    }

    static RegistroLimpieza apply(LimpiezaRequest request, RegistroLimpieza entity, CatalogQueryService queryService) {
        entity.setTurno(queryService.turno(request.turnoId()));
        entity.setEscala(request.escala());
        entity.setObservaciones(defaultString(request.observaciones()));
        entity.setRegistradoEn(resolveDateTimeOrNow(request.registradoEn(), entity.getRegistradoEn()));
        return entity;
    }

    static Notificacion apply(NotificacionRequest request, Notificacion entity, CatalogQueryService queryService) {
        entity.setTurno(queryService.turno(request.turnoId()));
        entity.setTipo(parseTipoNotificacion(request.tipo()));
        entity.setMensaje(defaultString(request.mensaje()));
        entity.setEnviadaEn(resolveDateTimeOrNow(request.enviadaEn(), entity.getEnviadaEn()));
        entity.setLeida(request.leida() != null ? request.leida() : Boolean.FALSE);
        entity.setMinutosAnticipacion(request.minutosAnticipacion());
        return entity;
    }

    static MapaCalor apply(MapaCalorRequest request, MapaCalor entity, CatalogQueryService queryService) {
        entity.setZona(queryService.zona(request.zonaId()));
        entity.setFranja(request.franja());
        entity.setTipoIncidente(parseTipoIncidente(request.tipoIncidente()));
        entity.setTotalIncidentes(request.totalIncidentes());
        entity.setPorcentaje(request.porcentaje() != null ? request.porcentaje().floatValue() : null);
        entity.setPeriodoInicio(parseDate(request.periodoInicio()));
        entity.setPeriodoFin(parseDate(request.periodoFin()));
        return entity;
    }

    static MetricaDocente apply(MetricaRequest request, MetricaDocente entity, CatalogQueryService queryService) {
        entity.setDocente(queryService.docente(request.docenteId()));
        entity.setPuntualidad(request.puntualidad());
        entity.setCobertura(request.cobertura());
        entity.setRetrasos(request.retrasos());
        entity.setRecorridosCompletados(request.recorridosCompletados());
        entity.setIncidentesRegistrados(request.incidentesRegistrados());
        entity.setReasignacionesAceptadas(request.reasignacionesAceptadas());
        entity.setPuntajeTotal(request.puntajeTotal());
        entity.setPeriodo(request.periodo());
        return entity;
    }

    static Reconocimiento apply(ReconocimientoRequest request, Reconocimiento entity, CatalogQueryService queryService) {
        entity.setMetricaDocente(queryService.metrica(request.metricaDocenteId()));
        entity.setTitulo(request.titulo());
        entity.setDescripcion(defaultString(request.descripcion()));
        entity.setTipo(parseTipoReconocimiento(request.tipo()));
        entity.setOtorgadoEn(parseDate(request.otorgadoEn()));
        entity.setTrimestre(request.trimestre());
        return entity;
    }

    static Recorrido apply(RecorridoRequest request, Recorrido entity, CatalogQueryService queryService) {
        entity.setDocente(queryService.docente(request.docenteId()));
        entity.setTurno(queryService.turno(request.turnoId()));
        entity.setIniciadoEn(parseDateTimeOrNow(request.iniciadoEn()));
        entity.setFinalizadoEn(parseDateTimeOrNull(request.finalizadoEn()));
        entity.setEstado(parseEstadoRecorrido(request.estado()));
        entity.setDuracionMinutos(request.duracionMinutos());
        return entity;
    }

    static CheckpointRecorrido apply(CheckpointRequest request, CheckpointRecorrido entity, CatalogQueryService queryService) {
        entity.setZona(queryService.zona(request.zonaId()));
        entity.setRecorrido(queryService.recorrido(request.recorridoId()));
        entity.setCodigoQR(request.codigoQR());
        entity.setDescripcion(defaultString(request.descripcion()));
        entity.setOrden(request.orden());
        entity.setEscaneadoEn(parseDateTimeOrNull(request.escaneadoEn()));
        return entity;
    }

    static Usuario apply(UsuarioRequest request, Usuario existing) {
        Usuario entity = existing != null ? existing : newUsuario(request.rol());
        entity.setNombre(request.nombre());
        entity.setEmail(request.email());
        entity.setPasswordHash(isBlank(request.passwordHash()) ? entity.getPasswordHash() : request.passwordHash());
        entity.setActivo(request.activo() != null ? request.activo() : entity.getActivo() != null ? entity.getActivo() : Boolean.TRUE);
        entity.setRol(existing != null ? existing.getRol() : parseRol(request.rol()));

        if (entity instanceof Docente docente) {
            docente.setMaterias(request.descriptor());
            docente.setCargaActual(request.cargaActual() != null ? request.cargaActual() : docente.getCargaActual() != null ? docente.getCargaActual() : 0);
            docente.setPuntajeGamificacion(request.puntajeGamificacion() != null ? request.puntajeGamificacion() : docente.getPuntajeGamificacion() != null ? docente.getPuntajeGamificacion() : 0);
        } else if (entity instanceof Coordinador coordinador) {
            coordinador.setNivel(request.descriptor());
        } else if (entity instanceof Administrador administrador) {
            administrador.setCargo(request.descriptor());
        }
        return entity;
    }

    static Docente apply(DocenteRequest request, Docente entity) {
        entity.setNombre(request.nombre());
        entity.setEmail(request.email());
        entity.setPasswordHash(isBlank(request.passwordHash()) ? entity.getPasswordHash() : request.passwordHash());
        entity.setActivo(request.activo() != null ? request.activo() : entity.getActivo() != null ? entity.getActivo() : Boolean.TRUE);
        entity.setRol(RolUsuario.DOCENTE);
        entity.setMaterias(request.materias());
        entity.setCargaActual(request.cargaActual() != null ? request.cargaActual() : entity.getCargaActual() != null ? entity.getCargaActual() : 0);
        entity.setPuntajeGamificacion(request.puntajeGamificacion() != null ? request.puntajeGamificacion() : entity.getPuntajeGamificacion() != null ? entity.getPuntajeGamificacion() : 0);
        return entity;
    }

    static ConfiguracionSistema apply(ConfiguracionRequest request, ConfiguracionSistema entity, CatalogQueryService queryService) {
        entity.setAdministrador(queryService.administrador(request.administradorId()));
        entity.setMinutosAlertaAusencia(request.minutosAlertaAusencia());
        entity.setSegundosVentanaReasignacion(request.segundosVentanaReasignacion());
        entity.setMinutosInactividad(request.minutosInactividad());
        entity.setUmbralIngreso(request.umbralIngreso());
        entity.setMinutosRecordatorio1(request.minutosRecordatorio1());
        entity.setMinutosRecordatorio2(request.minutosRecordatorio2());
        return entity;
    }

    private static Usuario newUsuario(String rol) {
        return switch (parseRol(rol)) {
            case DOCENTE -> new Docente();
            case COORDINADOR -> new Coordinador();
            case ADMINISTRADOR -> new Administrador();
        };
    }

    private static String descriptor(Usuario entity) {
        if (entity instanceof Docente docente) {
            return docente.getMaterias();
        }
        if (entity instanceof Coordinador coordinador) {
            return coordinador.getNivel();
        }
        if (entity instanceof Administrador administrador) {
            return administrador.getCargo();
        }
        return null;
    }

    private static String enumName(Enum<?> value) {
        return value != null ? value.name() : null;
    }

    private static String format(LocalDate value) {
        return value != null ? value.format(DATE) : null;
    }

    private static String format(LocalTime value) {
        return value != null ? value.format(TIME) : null;
    }

    private static String format(LocalDateTime value) {
        return value != null ? value.format(DATE_TIME) : null;
    }

    private static LocalDate parseDate(String value) {
        return LocalDate.parse(value, DATE);
    }

    private static LocalTime parseTime(String value) {
        return LocalTime.parse(value, TIME);
    }

    private static LocalDateTime parseDateTimeOrNow(String value) {
        return isBlank(value) ? LocalDateTime.now() : LocalDateTime.parse(value, DATE_TIME);
    }

    private static LocalDateTime parseDateTimeOrNull(String value) {
        return isBlank(value) ? null : LocalDateTime.parse(value, DATE_TIME);
    }

    private static LocalDateTime resolveDateTimeOrNow(String value, LocalDateTime currentValue) {
        return isBlank(value) ? (currentValue != null ? currentValue : LocalDateTime.now()) : LocalDateTime.parse(value, DATE_TIME);
    }

    private static LocalDateTime resolveDateTime(String value, LocalDateTime currentValue) {
        return isBlank(value) ? currentValue : LocalDateTime.parse(value, DATE_TIME);
    }

    private static EstadoTurno parseEstadoTurno(String value) {
        return EstadoTurno.valueOf(value);
    }

    private static TipoIncidente parseTipoIncidente(String value) {
        return TipoIncidente.valueOf(value);
    }

    private static MetodoCheckIn parseMetodoCheckIn(String value) {
        return MetodoCheckIn.valueOf(value);
    }

    private static EstadoReasignacion parseEstadoReasignacion(String value) {
        String normalized = normalizeEnum(value);
        if ("PENDIENTE".equals(normalized)) {
            normalized = "PROPUESTA";
        }
        return EstadoReasignacion.valueOf(normalized);
    }

    private static SeveridadIncidente parseSeveridad(String value) {
        String normalized = normalizeEnum(value);
        return switch (normalized) {
            case "S1" -> SeveridadIncidente.S1_LEVE;
            case "S2" -> SeveridadIncidente.S2_SEGUIMIENTO;
            case "S3" -> SeveridadIncidente.S3_ATENCION_INMEDIATA;
            default -> SeveridadIncidente.valueOf(normalized);
        };
    }

    private static TipoNotificacion parseTipoNotificacion(String value) {
        return TipoNotificacion.valueOf(value);
    }

    private static TipoReconocimiento parseTipoReconocimiento(String value) {
        return TipoReconocimiento.valueOf(value);
    }

    private static EstadoRecorrido parseEstadoRecorrido(String value) {
        String normalized = normalizeEnum(value);
        if ("EN_CURSO".equals(normalized)) {
            normalized = "EN_PROGRESO";
        }
        return EstadoRecorrido.valueOf(normalized);
    }

    private static RolUsuario parseRol(String value) {
        return RolUsuario.valueOf(normalizeEnum(value));
    }

    private static String normalizeEnum(String value) {
        return value != null ? value.trim().toUpperCase() : null;
    }

    private static String defaultString(String value) {
        return value != null ? value : "";
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
