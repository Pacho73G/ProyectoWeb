package com.example.demo.web.api;

record ZonaRequest(
        String nombre,
        String descripcion,
        String ubicacion,
        Integer capacidadMaxima,
        Boolean activa
) {}

record TurnoRequest(
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

record IncidenteRequest(
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

record CheckInRequest(
        Long turnoId,
        Long docenteId,
        Long zonaId,
        String timestamp,
        String metodo,
        String evidencia,
        Boolean valido
) {}

record ReasignacionRequest(
        Long turnoId,
        Long docenteSolicitanteId,
        Long docenteReemplazoId,
        String motivo,
        String estado,
        String propuestaEn,
        String respondidaEn,
        Integer segundosVentana
) {}

record LimpiezaRequest(
        Long turnoId,
        Integer escala,
        String observaciones,
        String registradoEn
) {}

record NotificacionRequest(
        Long turnoId,
        String tipo,
        String mensaje,
        String enviadaEn,
        Boolean leida,
        Integer minutosAnticipacion
) {}

record MapaCalorRequest(
        Long zonaId,
        String franja,
        String tipoIncidente,
        Integer totalIncidentes,
        Double porcentaje,
        String periodoInicio,
        String periodoFin
) {}

record MetricaRequest(
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

record ReconocimientoRequest(
        Long metricaDocenteId,
        String titulo,
        String descripcion,
        String tipo,
        String otorgadoEn,
        String trimestre
) {}

record RecorridoRequest(
        Long docenteId,
        Long turnoId,
        String iniciadoEn,
        String finalizadoEn,
        String estado,
        Integer duracionMinutos
) {}

record CheckpointRequest(
        Long zonaId,
        Long recorridoId,
        String codigoQR,
        String descripcion,
        Integer orden,
        String escaneadoEn
) {}

record UsuarioRequest(
        String nombre,
        String email,
        String passwordHash,
        Boolean activo,
        String rol,
        String descriptor,
        Integer cargaActual,
        Integer puntajeGamificacion
) {}

record DocenteRequest(
        String nombre,
        String email,
        String passwordHash,
        Boolean activo,
        String materias,
        Integer cargaActual,
        Integer puntajeGamificacion
) {}

record ConfiguracionRequest(
        Long administradorId,
        Integer minutosAlertaAusencia,
        Integer segundosVentanaReasignacion,
        Integer minutosInactividad,
        Integer umbralIngreso,
        Integer minutosRecordatorio1,
        Integer minutosRecordatorio2
) {}
