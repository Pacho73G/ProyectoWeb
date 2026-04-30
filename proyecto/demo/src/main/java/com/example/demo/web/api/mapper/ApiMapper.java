/* Archivo documentado: Mapper central de la API. Convierte entre entidades, DTOs y payloads entrantes para mantener separado el modelo persistente del contrato HTTP. */
package com.example.demo.web.api.mapper;

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
import com.example.demo.web.api.dto.ApiDtos.CheckInDto;
import com.example.demo.web.api.dto.ApiDtos.CheckpointDto;
import com.example.demo.web.api.dto.ApiDtos.ConfiguracionDto;
import com.example.demo.web.api.dto.ApiDtos.IncidenteDto;
import com.example.demo.web.api.dto.ApiDtos.LimpiezaDto;
import com.example.demo.web.api.dto.ApiDtos.MapaCalorDto;
import com.example.demo.web.api.dto.ApiDtos.MetricaDto;
import com.example.demo.web.api.dto.ApiDtos.NotificacionDto;
import com.example.demo.web.api.dto.ApiDtos.ReasignacionDto;
import com.example.demo.web.api.dto.ApiDtos.ReconocimientoDto;
import com.example.demo.web.api.dto.ApiDtos.RecorridoDto;
import com.example.demo.web.api.dto.ApiDtos.TurnoDto;
import com.example.demo.web.api.dto.ApiDtos.UsuarioDto;
import com.example.demo.web.api.dto.ApiDtos.ZonaDto;
import com.example.demo.web.api.request.ApiRequests.CheckInRequest;
import com.example.demo.web.api.request.ApiRequests.CheckpointRequest;
import com.example.demo.web.api.request.ApiRequests.ConfiguracionRequest;
import com.example.demo.web.api.request.ApiRequests.DocenteRequest;
import com.example.demo.web.api.request.ApiRequests.IncidenteRequest;
import com.example.demo.web.api.request.ApiRequests.LimpiezaRequest;
import com.example.demo.web.api.request.ApiRequests.MapaCalorRequest;
import com.example.demo.web.api.request.ApiRequests.MetricaRequest;
import com.example.demo.web.api.request.ApiRequests.NotificacionRequest;
import com.example.demo.web.api.request.ApiRequests.ReasignacionRequest;
import com.example.demo.web.api.request.ApiRequests.ReconocimientoRequest;
import com.example.demo.web.api.request.ApiRequests.RecorridoRequest;
import com.example.demo.web.api.request.ApiRequests.TurnoRequest;
import com.example.demo.web.api.request.ApiRequests.UsuarioRequest;
import com.example.demo.web.api.request.ApiRequests.ZonaRequest;

public final class ApiMapper {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME = DateTimeFormatter.ISO_LOCAL_TIME;
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private ApiMapper() {}

    public static ZonaDto toDto(Zona entity) {
        return new ZonaDto(entity.getId(), entity.getNombre(), entity.getDescripcion(), entity.getUbicacion(),
                entity.getCapacidadMaxima(), entity.getActiva());
    }

    public static TurnoDto toDto(Turno entity) {
        return new TurnoDto(
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

    public static IncidenteDto toDto(Incidente entity) {
        return new IncidenteDto(
                entity.getId(),
                entity.getTurno() != null ? entity.getTurno().getId() : null,
                entity.getTurno() != null ? entity.getTurno().getFranja() : null,
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

    public static CheckInDto toDto(CheckIn entity) {
        return new CheckInDto(
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

    public static ReasignacionDto toDto(Reasignacion entity) {
        return new ReasignacionDto(
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

    public static LimpiezaDto toDto(RegistroLimpieza entity) {
        return new LimpiezaDto(
                entity.getId(),
                entity.getTurno() != null ? entity.getTurno().getId() : null,
                entity.getTurno() != null ? entity.getTurno().getFranja() : null,
                entity.getDocente() != null ? entity.getDocente().getId() : null,
                entity.getDocente() != null ? entity.getDocente().getNombre() : null,
                entity.getZona() != null ? entity.getZona().getId() : null,
                entity.getZona() != null ? entity.getZona().getNombre() : null,
                entity.getEscala(),
                entity.getObservaciones(),
                format(entity.getAsignadaEn()),
                format(entity.getRegistradoEn()),
                entity.getCompletada()
        );
    }

    public static NotificacionDto toDto(Notificacion entity) {
        return new NotificacionDto(
                entity.getId(),
                entity.getTurno() != null ? entity.getTurno().getId() : null,
                entity.getTurno() != null ? entity.getTurno().getFranja() : null,
                entity.getDestinatario() != null ? entity.getDestinatario().getId() : null,
                entity.getDestinatario() != null ? entity.getDestinatario().getNombre() : null,
                enumName(entity.getTipo()),
                entity.getTitulo(),
                entity.getMensaje(),
                format(entity.getEnviadaEn()),
                entity.getLeida(),
                entity.getMinutosAnticipacion()
        );
    }

    public static MapaCalorDto toDto(MapaCalor entity) {
        return new MapaCalorDto(entity.getId(), entity.getZona() != null ? entity.getZona().getId() : null,
                entity.getZona() != null ? entity.getZona().getNombre() : null, entity.getFranja(),
                enumName(entity.getTipoIncidente()), entity.getTotalIncidentes(),
                entity.getPorcentaje() != null ? entity.getPorcentaje().doubleValue() : null,
                format(entity.getPeriodoInicio()), format(entity.getPeriodoFin()));
    }

    public static MetricaDto toDto(MetricaDocente entity) {
        return new MetricaDto(entity.getId(), entity.getDocente() != null ? entity.getDocente().getId() : null,
                entity.getDocente() != null ? entity.getDocente().getNombre() : null,
                entity.getPuntualidad() != null ? entity.getPuntualidad().doubleValue() : null,
                entity.getCobertura() != null ? entity.getCobertura().doubleValue() : null, entity.getRetrasos(),
                entity.getRecorridosCompletados(), entity.getIncidentesRegistrados(),
                entity.getReasignacionesAceptadas(),
                entity.getPuntajeTotal() != null ? entity.getPuntajeTotal().doubleValue() : null, entity.getPeriodo());
    }

    public static ReconocimientoDto toDto(Reconocimiento entity) {
        return new ReconocimientoDto(entity.getId(),
                entity.getMetricaDocente() != null ? entity.getMetricaDocente().getId() : null,
                entity.getMetricaDocente() != null && entity.getMetricaDocente().getDocente() != null
                        ? entity.getMetricaDocente().getDocente().getNombre() : null,
                entity.getTitulo(), entity.getDescripcion(), enumName(entity.getTipo()),
                format(entity.getOtorgadoEn()), entity.getTrimestre());
    }

    public static RecorridoDto toDto(Recorrido entity) {
        return new RecorridoDto(entity.getId(), entity.getDocente() != null ? entity.getDocente().getId() : null,
                entity.getDocente() != null ? entity.getDocente().getNombre() : null,
                entity.getTurno() != null ? entity.getTurno().getId() : null,
                entity.getTurno() != null ? entity.getTurno().getFranja() : null,
                format(entity.getIniciadoEn()), format(entity.getFinalizadoEn()), enumName(entity.getEstado()),
                entity.getDuracionMinutos());
    }

    public static CheckpointDto toDto(CheckpointRecorrido entity) {
        return new CheckpointDto(entity.getId(), entity.getZona() != null ? entity.getZona().getId() : null,
                entity.getZona() != null ? entity.getZona().getNombre() : null,
                entity.getRecorrido() != null ? entity.getRecorrido().getId() : null, entity.getCodigoQR(),
                entity.getDescripcion(), entity.getOrden(), format(entity.getEscaneadoEn()));
    }

    public static UsuarioDto toDto(Usuario entity) {
        return new UsuarioDto(entity.getId(), entity.getNombre(), entity.getEmail(), entity.getActivo(),
                enumName(entity.getRol()), descriptor(entity),
                entity instanceof Docente docente ? docente.getCargaActual() : null,
                entity instanceof Docente docente ? docente.getPuntajeGamificacion() : null);
    }

    public static ConfiguracionDto toDto(ConfiguracionSistema entity) {
        return new ConfiguracionDto(entity.getId(),
                entity.getAdministrador() != null ? entity.getAdministrador().getId() : null,
                entity.getAdministrador() != null ? entity.getAdministrador().getNombre() : null,
                entity.getMinutosAlertaAusencia(), entity.getSegundosVentanaReasignacion(),
                entity.getMinutosInactividad(), entity.getUmbralIngreso(), entity.getMinutosRecordatorio1(),
                entity.getMinutosRecordatorio2());
    }

    public static Zona apply(ZonaRequest request, Zona entity) {
        entity.setNombre(request.nombre());
        entity.setDescripcion(defaultString(request.descripcion()));
        entity.setUbicacion(request.ubicacion());
        entity.setCapacidadMaxima(request.capacidadMaxima());
        entity.setActiva(request.activa() != null ? request.activa() : Boolean.TRUE);
        return entity;
    }

    public static Turno apply(TurnoRequest request, Turno entity, CatalogQueryService queryService) {
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

    public static Incidente apply(IncidenteRequest request, Incidente entity, CatalogQueryService queryService) {
        // El incidente puede registrarse fuera de turno; por eso la relación es opcional.
        entity.setTurno(request.turnoId() != null ? queryService.turno(request.turnoId()) : null);
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

    public static CheckIn apply(CheckInRequest request, CheckIn entity, CatalogQueryService queryService) {
        entity.setTurno(queryService.turno(request.turnoId()));
        entity.setDocente(queryService.docente(request.docenteId()));
        entity.setZona(queryService.zona(request.zonaId()));
        entity.setTimestamp(resolveDateTimeOrNow(request.timestamp(), entity.getTimestamp()));
        entity.setMetodo(parseMetodoCheckIn(request.metodo()));
        entity.setEvidencia(request.evidencia());
        entity.setValido(request.valido() != null ? request.valido() : Boolean.TRUE);
        return entity;
    }

    public static Reasignacion apply(ReasignacionRequest request, Reasignacion entity, CatalogQueryService queryService) {
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

    public static RegistroLimpieza apply(LimpiezaRequest request, RegistroLimpieza entity, CatalogQueryService queryService) {
        // La limpieza puede existir con turno o sin turno, pero siempre con docente y zona asignados.
        entity.setTurno(request.turnoId() != null ? queryService.turno(request.turnoId()) : null);
        entity.setDocente(queryService.docente(request.docenteId()));
        entity.setZona(queryService.zona(request.zonaId()));
        entity.setEscala(request.escala());
        entity.setObservaciones(defaultString(request.observaciones()));
        // "asignadaEn" representa cuándo se entregó la tarea, distinto de cuándo fue registrada.
        entity.setAsignadaEn(resolveDateTimeOrNow(request.asignadaEn(), entity.getAsignadaEn()));
        entity.setRegistradoEn(resolveDateTimeOrNow(request.registradoEn(), entity.getRegistradoEn()));
        entity.setCompletada(request.completada() != null ? request.completada() : entity.getCompletada() != null ? entity.getCompletada() : Boolean.FALSE);
        return entity;
    }

    public static Notificacion apply(NotificacionRequest request, Notificacion entity, CatalogQueryService queryService) {
        // Algunas notificaciones nacen de turno y otras de eventos sin turno; por eso es opcional.
        entity.setTurno(request.turnoId() != null ? queryService.turno(request.turnoId()) : null);
        entity.setDestinatario(request.destinatarioId() != null ? queryService.usuario(request.destinatarioId()) : null);
        entity.setTipo(parseTipoNotificacion(request.tipo()));
        entity.setTitulo(defaultString(request.titulo()));
        entity.setMensaje(defaultString(request.mensaje()));
        entity.setEnviadaEn(resolveDateTimeOrNow(request.enviadaEn(), entity.getEnviadaEn()));
        entity.setLeida(request.leida() != null ? request.leida() : Boolean.FALSE);
        entity.setMinutosAnticipacion(request.minutosAnticipacion());
        return entity;
    }

    public static MapaCalor apply(MapaCalorRequest request, MapaCalor entity, CatalogQueryService queryService) {
        entity.setZona(queryService.zona(request.zonaId()));
        entity.setFranja(request.franja());
        entity.setTipoIncidente(parseTipoIncidente(request.tipoIncidente()));
        entity.setTotalIncidentes(request.totalIncidentes());
        entity.setPorcentaje(request.porcentaje() != null ? request.porcentaje().floatValue() : null);
        entity.setPeriodoInicio(parseDate(request.periodoInicio()));
        entity.setPeriodoFin(parseDate(request.periodoFin()));
        return entity;
    }

    public static MetricaDocente apply(MetricaRequest request, MetricaDocente entity, CatalogQueryService queryService) {
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

    public static Reconocimiento apply(ReconocimientoRequest request, Reconocimiento entity, CatalogQueryService queryService) {
        entity.setMetricaDocente(queryService.metrica(request.metricaDocenteId()));
        entity.setTitulo(request.titulo());
        entity.setDescripcion(defaultString(request.descripcion()));
        entity.setTipo(parseTipoReconocimiento(request.tipo()));
        entity.setOtorgadoEn(parseDate(request.otorgadoEn()));
        entity.setTrimestre(request.trimestre());
        return entity;
    }

    public static Recorrido apply(RecorridoRequest request, Recorrido entity, CatalogQueryService queryService) {
        entity.setDocente(queryService.docente(request.docenteId()));
        entity.setTurno(queryService.turno(request.turnoId()));
        entity.setIniciadoEn(parseDateTimeOrNow(request.iniciadoEn()));
        entity.setFinalizadoEn(parseDateTimeOrNull(request.finalizadoEn()));
        entity.setEstado(parseEstadoRecorrido(request.estado()));
        entity.setDuracionMinutos(request.duracionMinutos());
        return entity;
    }

    public static CheckpointRecorrido apply(CheckpointRequest request, CheckpointRecorrido entity, CatalogQueryService queryService) {
        entity.setZona(queryService.zona(request.zonaId()));
        entity.setRecorrido(queryService.recorrido(request.recorridoId()));
        entity.setCodigoQR(request.codigoQR());
        entity.setDescripcion(defaultString(request.descripcion()));
        entity.setOrden(request.orden());
        entity.setEscaneadoEn(parseDateTimeOrNull(request.escaneadoEn()));
        return entity;
    }

    public static Usuario apply(UsuarioRequest request, Usuario existing) {
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

    public static Docente apply(DocenteRequest request, Docente entity) {
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

    public static ConfiguracionSistema apply(ConfiguracionRequest request, ConfiguracionSistema entity, CatalogQueryService queryService) {
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
        if (entity instanceof Docente docente) return docente.getMaterias();
        if (entity instanceof Coordinador coordinador) return coordinador.getNivel();
        if (entity instanceof Administrador administrador) return administrador.getCargo();
        return null;
    }

    private static String enumName(Enum<?> value) { return value != null ? value.name() : null; }
    private static String format(LocalDate value) { return value != null ? value.format(DATE) : null; }
    private static String format(LocalTime value) { return value != null ? value.format(TIME) : null; }
    private static String format(LocalDateTime value) { return value != null ? value.format(DATE_TIME) : null; }
    private static LocalDate parseDate(String value) { return LocalDate.parse(value, DATE); }
    private static LocalTime parseTime(String value) { return LocalTime.parse(value, TIME); }
    private static LocalDateTime parseDateTimeOrNow(String value) { return isBlank(value) ? LocalDateTime.now() : LocalDateTime.parse(value, DATE_TIME); }
    private static LocalDateTime parseDateTimeOrNull(String value) { return isBlank(value) ? null : LocalDateTime.parse(value, DATE_TIME); }
    private static LocalDateTime resolveDateTimeOrNow(String value, LocalDateTime currentValue) { return isBlank(value) ? (currentValue != null ? currentValue : LocalDateTime.now()) : LocalDateTime.parse(value, DATE_TIME); }
    private static LocalDateTime resolveDateTime(String value, LocalDateTime currentValue) { return isBlank(value) ? currentValue : LocalDateTime.parse(value, DATE_TIME); }
    private static EstadoTurno parseEstadoTurno(String value) { return EstadoTurno.valueOf(value); }
    private static TipoIncidente parseTipoIncidente(String value) { return TipoIncidente.valueOf(value); }
    private static MetodoCheckIn parseMetodoCheckIn(String value) { return MetodoCheckIn.valueOf(value); }

    private static EstadoReasignacion parseEstadoReasignacion(String value) {
        String normalized = normalizeEnum(value);
        if ("PENDIENTE".equals(normalized)) normalized = "PROPUESTA";
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

    private static TipoNotificacion parseTipoNotificacion(String value) { return TipoNotificacion.valueOf(value); }
    private static TipoReconocimiento parseTipoReconocimiento(String value) { return TipoReconocimiento.valueOf(value); }

    private static EstadoRecorrido parseEstadoRecorrido(String value) {
        String normalized = normalizeEnum(value);
        if ("EN_CURSO".equals(normalized)) normalized = "EN_PROGRESO";
        return EstadoRecorrido.valueOf(normalized);
    }

    private static RolUsuario parseRol(String value) { return RolUsuario.valueOf(normalizeEnum(value)); }
    private static String normalizeEnum(String value) { return value != null ? value.trim().toUpperCase() : null; }
    private static String defaultString(String value) { return value != null ? value : ""; }
    private static boolean isBlank(String value) { return value == null || value.isBlank(); }
}
