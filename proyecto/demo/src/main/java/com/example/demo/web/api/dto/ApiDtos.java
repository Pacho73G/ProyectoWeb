/* Archivo documentado: Contenedor de DTOs de salida de la API REST. Define la forma exacta en que el backend entrega información a la SPA. */
package com.example.demo.web.api.dto;

public final class ApiDtos {

    private ApiDtos() {}

    public record ZonaDto(
            Long id,
            String nombre,
            String descripcion,
            String ubicacion,
            Integer capacidadMaxima,
            Boolean activa
    ) {}

    public record TurnoDto(
            Long id,
            Long docenteId,
            String docenteNombre,
            String docenteEmail,
            Long zonaId,
            String zonaNombre,
            String zonaUbicacion,
            String fecha,
            String horaInicio,
            String horaFin,
            String franja,
            String estado
    ) {}

    public record IncidenteDto(
            Long id,
            Long turnoId,
            String turnoFranja,
            Long docenteId,
            String docenteNombre,
            Long zonaId,
            String zonaNombre,
            String tipo,
            String severidad,
            String descripcion,
            String observacionSocial,
            String registradoEn,
            Boolean requiereSeguimiento
    ) {}

    public record CheckInDto(
            Long id,
            Long turnoId,
            String turnoFranja,
            Long docenteId,
            String docenteNombre,
            Long zonaId,
            String zonaNombre,
            String timestamp,
            String metodo,
            String evidencia,
            Boolean valido
    ) {}

    public record ReasignacionDto(
            Long id,
            Long turnoId,
            String turnoFranja,
            String zonaNombre,
            Long docenteSolicitanteId,
            String docenteSolicitanteNombre,
            Long docenteReemplazoId,
            String docenteReemplazoNombre,
            String motivo,
            String estado,
            String propuestaEn,
            String respondidaEn,
            Integer segundosVentana
    ) {}

    public record LimpiezaDto(
            Long id,
            Long turnoId,
            String turnoFranja,
            Long docenteId,
            String docenteNombre,
            Long zonaId,
            String zonaNombre,
            Integer escala,
            String observaciones,
            String asignadaEn,
            String registradoEn,
            Boolean completada
    ) {}

    public record NotificacionDto(
            Long id,
            Long turnoId,
            String turnoFranja,
            Long destinatarioId,
            String destinatarioNombre,
            String tipo,
            String titulo,
            String mensaje,
            String enviadaEn,
            Boolean leida,
            Integer minutosAnticipacion
    ) {}

    public record MapaCalorDto(
            Long id,
            Long zonaId,
            String zonaNombre,
            String franja,
            String tipoIncidente,
            Integer totalIncidentes,
            Double porcentaje,
            String periodoInicio,
            String periodoFin
    ) {}

    public record MetricaDto(
            Long id,
            Long docenteId,
            String docenteNombre,
            Double puntualidad,
            Double cobertura,
            Integer retrasos,
            Integer recorridosCompletados,
            Integer incidentesRegistrados,
            Integer reasignacionesAceptadas,
            Double puntajeTotal,
            String periodo
    ) {}

    public record ReconocimientoDto(
            Long id,
            Long metricaDocenteId,
            String docenteNombre,
            String titulo,
            String descripcion,
            String tipo,
            String otorgadoEn,
            String trimestre
    ) {}

    public record RecorridoDto(
            Long id,
            Long docenteId,
            String docenteNombre,
            Long turnoId,
            String turnoFranja,
            String iniciadoEn,
            String finalizadoEn,
            String estado,
            Integer duracionMinutos
    ) {}

    public record CheckpointDto(
            Long id,
            Long zonaId,
            String zonaNombre,
            Long recorridoId,
            String codigoQR,
            String descripcion,
            Integer orden,
            String escaneadoEn
    ) {}

    public record UsuarioDto(
            Long id,
            String nombre,
            String email,
            Boolean activo,
            String rol,
            String descriptor,
            Integer cargaActual,
            Integer puntajeGamificacion
    ) {}

    public record ConfiguracionDto(
            Long id,
            Long administradorId,
            String administradorNombre,
            Integer minutosAlertaAusencia,
            Integer segundosVentanaReasignacion,
            Integer minutosInactividad,
            Integer umbralIngreso,
            Integer minutosRecordatorio1,
            Integer minutosRecordatorio2
    ) {}

    public record ReporteResumenDto(
            Long totalDocentes,
            Long totalTurnos,
            Long totalIncidentes,
            Long totalReasignaciones,
            Long totalRecorridos,
            Long totalReconocimientos
    ) {}

    public record ApiErrorDto(String message) {}
}
