/* Archivo documentado: Contenedor de requests de entrada de la API REST. Agrupa los payloads JSON aceptados por los endpoints de creación y edición. */
package com.example.demo.web.api.request;

public final class ApiRequests {

    private ApiRequests() {}

    public record ZonaRequest(
            String nombre,
            String descripcion,
            String ubicacion,
            Integer capacidadMaxima,
            Boolean activa
    ) {}

    public record TurnoRequest(
            Long docenteId,
            Long zonaId,
            String fecha,
            String horaInicio,
            String horaFin,
            String franja,
            String estado,
            String abiertoEn,
            String cerradoEn
    ) {}

    public record IncidenteRequest(
            Long turnoId,
            Long docenteId,
            Long zonaId,
            String tipo,
            String severidad,
            String descripcion,
            String observacionSocial,
            String registradoEn,
            Boolean requiereSeguimiento
    ) {}

    public record CheckInRequest(
            Long turnoId,
            Long docenteId,
            Long zonaId,
            String timestamp,
            String metodo,
            String evidencia,
            Boolean valido
    ) {}

    public record ReasignacionRequest(
            Long turnoId,
            Long docenteSolicitanteId,
            Long docenteReemplazoId,
            String motivo,
            String estado,
            String propuestaEn,
            String respondidaEn,
            Integer segundosVentana
    ) {}

    public record LimpiezaRequest(
            Long turnoId,
            Integer escala,
            String observaciones,
            String registradoEn
    ) {}

    public record NotificacionRequest(
            Long turnoId,
            String tipo,
            String mensaje,
            String enviadaEn,
            Boolean leida,
            Integer minutosAnticipacion
    ) {}

    public record MapaCalorRequest(
            Long zonaId,
            String franja,
            String tipoIncidente,
            Integer totalIncidentes,
            Double porcentaje,
            String periodoInicio,
            String periodoFin
    ) {}

    public record MetricaRequest(
            Long docenteId,
            Integer puntualidad,
            Integer cobertura,
            Integer retrasos,
            Integer recorridosCompletados,
            Integer incidentesRegistrados,
            Integer reasignacionesAceptadas,
            Integer puntajeTotal,
            String periodo
    ) {}

    public record ReconocimientoRequest(
            Long metricaDocenteId,
            String titulo,
            String descripcion,
            String tipo,
            String otorgadoEn,
            String trimestre
    ) {}

    public record RecorridoRequest(
            Long docenteId,
            Long turnoId,
            String iniciadoEn,
            String finalizadoEn,
            String estado,
            Integer duracionMinutos
    ) {}

    public record CheckpointRequest(
            Long zonaId,
            Long recorridoId,
            String codigoQR,
            String descripcion,
            Integer orden,
            String escaneadoEn
    ) {}

    public record UsuarioRequest(
            String nombre,
            String email,
            String passwordHash,
            Boolean activo,
            String rol,
            String descriptor,
            Integer cargaActual,
            Integer puntajeGamificacion
    ) {}

    public record DocenteRequest(
            String nombre,
            String email,
            String passwordHash,
            Boolean activo,
            String materias,
            Integer cargaActual,
            Integer puntajeGamificacion
    ) {}

    public record ConfiguracionRequest(
            Long administradorId,
            Integer minutosAlertaAusencia,
            Integer segundosVentanaReasignacion,
            Integer minutosInactividad,
            Integer umbralIngreso,
            Integer minutosRecordatorio1,
            Integer minutosRecordatorio2
    ) {}
}
