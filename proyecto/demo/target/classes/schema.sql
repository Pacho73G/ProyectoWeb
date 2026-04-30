CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(32) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS docentes (
    id BIGINT PRIMARY KEY,
    materias VARCHAR(255) NOT NULL,
    carga_actual INTEGER NOT NULL DEFAULT 0,
    puntaje_gamificacion INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_docentes_usuario FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS coordinadores (
    id BIGINT PRIMARY KEY,
    nivel VARCHAR(255) NOT NULL,
    CONSTRAINT fk_coordinadores_usuario FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS administradores (
    id BIGINT PRIMARY KEY,
    cargo VARCHAR(255) NOT NULL,
    CONSTRAINT fk_administradores_usuario FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS zonas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL,
    ubicacion VARCHAR(255) NOT NULL,
    capacidad_maxima INTEGER NOT NULL,
    activa BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS configuraciones_sistema (
    id BIGSERIAL PRIMARY KEY,
    administrador_id BIGINT NOT NULL UNIQUE,
    minutos_alerta_ausencia INTEGER NOT NULL,
    segundos_ventana_reasignacion INTEGER NOT NULL,
    minutos_inactividad INTEGER NOT NULL,
    umbral_ingreso INTEGER NOT NULL,
    minutos_recordatorio_1 INTEGER NOT NULL,
    minutos_recordatorio_2 INTEGER NOT NULL,
    CONSTRAINT fk_configuraciones_administrador FOREIGN KEY (administrador_id) REFERENCES administradores(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS turnos (
    id BIGSERIAL PRIMARY KEY,
    docente_id BIGINT NOT NULL,
    zona_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    franja VARCHAR(255) NOT NULL,
    estado VARCHAR(32) NOT NULL,
    abierto_en TIMESTAMP NULL,
    cerrado_en TIMESTAMP NULL,
    CONSTRAINT fk_turnos_docente FOREIGN KEY (docente_id) REFERENCES docentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_turnos_zona FOREIGN KEY (zona_id) REFERENCES zonas(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS checkins (
    id BIGSERIAL PRIMARY KEY,
    turno_id BIGINT NOT NULL UNIQUE,
    docente_id BIGINT NOT NULL,
    zona_id BIGINT NOT NULL,
    timestamp_registro TIMESTAMP NOT NULL,
    metodo VARCHAR(32) NOT NULL,
    evidencia VARCHAR(255) NOT NULL,
    valido BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_checkins_turno FOREIGN KEY (turno_id) REFERENCES turnos(id) ON DELETE CASCADE,
    CONSTRAINT fk_checkins_docente FOREIGN KEY (docente_id) REFERENCES docentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_checkins_zona FOREIGN KEY (zona_id) REFERENCES zonas(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS incidentes (
    id BIGSERIAL PRIMARY KEY,
    turno_id BIGINT NULL,
    docente_id BIGINT NOT NULL,
    zona_id BIGINT NOT NULL,
    tipo VARCHAR(64) NOT NULL,
    severidad VARCHAR(64) NOT NULL,
    descripcion TEXT NOT NULL,
    observacion_social TEXT NULL,
    registrado_en TIMESTAMP NOT NULL,
    requiere_seguimiento BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_incidentes_turno FOREIGN KEY (turno_id) REFERENCES turnos(id) ON DELETE CASCADE,
    CONSTRAINT fk_incidentes_docente FOREIGN KEY (docente_id) REFERENCES docentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_incidentes_zona FOREIGN KEY (zona_id) REFERENCES zonas(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reasignaciones (
    id BIGSERIAL PRIMARY KEY,
    turno_id BIGINT NOT NULL UNIQUE,
    docente_solicitante_id BIGINT NOT NULL,
    docente_reemplazo_id BIGINT NULL,
    motivo TEXT NOT NULL,
    estado VARCHAR(32) NOT NULL,
    propuesta_en TIMESTAMP NOT NULL,
    respondida_en TIMESTAMP NULL,
    segundos_ventana INTEGER NOT NULL,
    CONSTRAINT fk_reasignaciones_turno FOREIGN KEY (turno_id) REFERENCES turnos(id) ON DELETE CASCADE,
    CONSTRAINT fk_reasignaciones_solicitante FOREIGN KEY (docente_solicitante_id) REFERENCES docentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_reasignaciones_reemplazo FOREIGN KEY (docente_reemplazo_id) REFERENCES docentes(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS registros_limpieza (
    id BIGSERIAL PRIMARY KEY,
    turno_id BIGINT NULL UNIQUE,
    docente_id BIGINT NOT NULL,
    zona_id BIGINT NOT NULL,
    escala INTEGER NOT NULL,
    observaciones TEXT NOT NULL,
    asignada_en TIMESTAMP NOT NULL,
    registrado_en TIMESTAMP NOT NULL,
    completada BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_registros_limpieza_turno FOREIGN KEY (turno_id) REFERENCES turnos(id) ON DELETE CASCADE,
    CONSTRAINT fk_registros_limpieza_docente FOREIGN KEY (docente_id) REFERENCES docentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_registros_limpieza_zona FOREIGN KEY (zona_id) REFERENCES zonas(id) ON DELETE CASCADE
);

ALTER TABLE incidentes ALTER COLUMN turno_id DROP NOT NULL;

CREATE TABLE IF NOT EXISTS recorridos (
    id BIGSERIAL PRIMARY KEY,
    docente_id BIGINT NOT NULL,
    turno_id BIGINT NOT NULL,
    iniciado_en TIMESTAMP NOT NULL,
    finalizado_en TIMESTAMP NULL,
    estado VARCHAR(32) NOT NULL,
    duracion_minutos INTEGER NULL,
    CONSTRAINT fk_recorridos_docente FOREIGN KEY (docente_id) REFERENCES docentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_recorridos_turno FOREIGN KEY (turno_id) REFERENCES turnos(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS checkpoints_recorrido (
    id BIGSERIAL PRIMARY KEY,
    zona_id BIGINT NOT NULL,
    recorrido_id BIGINT NOT NULL,
    codigo_qr VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    orden_checkpoint INTEGER NOT NULL,
    escaneado_en TIMESTAMP NULL,
    CONSTRAINT fk_checkpoints_zona FOREIGN KEY (zona_id) REFERENCES zonas(id) ON DELETE CASCADE,
    CONSTRAINT fk_checkpoints_recorrido FOREIGN KEY (recorrido_id) REFERENCES recorridos(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS notificaciones (
    id BIGSERIAL PRIMARY KEY,
    turno_id BIGINT NULL,
    destinatario_id BIGINT NULL,
    tipo VARCHAR(64) NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    mensaje TEXT NOT NULL,
    enviada_en TIMESTAMP NOT NULL,
    leida BOOLEAN NOT NULL DEFAULT FALSE,
    minutos_anticipacion INTEGER NOT NULL,
    CONSTRAINT fk_notificaciones_turno FOREIGN KEY (turno_id) REFERENCES turnos(id) ON DELETE CASCADE,
    CONSTRAINT fk_notificaciones_destinatario FOREIGN KEY (destinatario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);
ALTER TABLE notificaciones ALTER COLUMN turno_id DROP NOT NULL;

ALTER TABLE notificaciones ADD COLUMN IF NOT EXISTS destinatario_id BIGINT NULL;
ALTER TABLE notificaciones ADD COLUMN IF NOT EXISTS titulo VARCHAR(255);
UPDATE notificaciones SET titulo = 'Notificación' WHERE titulo IS NULL;
ALTER TABLE notificaciones ALTER COLUMN titulo SET NOT NULL;
ALTER TABLE registros_limpieza ALTER COLUMN turno_id DROP NOT NULL;
ALTER TABLE registros_limpieza ADD COLUMN IF NOT EXISTS docente_id BIGINT;
ALTER TABLE registros_limpieza ADD COLUMN IF NOT EXISTS zona_id BIGINT;
ALTER TABLE registros_limpieza ADD COLUMN IF NOT EXISTS asignada_en TIMESTAMP;
ALTER TABLE registros_limpieza ADD COLUMN IF NOT EXISTS completada BOOLEAN NOT NULL DEFAULT FALSE;
UPDATE registros_limpieza rl
SET docente_id = t.docente_id,
    zona_id = t.zona_id,
    asignada_en = COALESCE(rl.registrado_en, CURRENT_TIMESTAMP)
FROM turnos t
WHERE rl.turno_id = t.id
  AND (rl.docente_id IS NULL OR rl.zona_id IS NULL OR rl.asignada_en IS NULL);
ALTER TABLE registros_limpieza ALTER COLUMN docente_id SET NOT NULL;
ALTER TABLE registros_limpieza ALTER COLUMN zona_id SET NOT NULL;
UPDATE registros_limpieza SET asignada_en = CURRENT_TIMESTAMP WHERE asignada_en IS NULL;
ALTER TABLE registros_limpieza ALTER COLUMN asignada_en SET NOT NULL;

CREATE TABLE IF NOT EXISTS metricas_docente (
    id BIGSERIAL PRIMARY KEY,
    docente_id BIGINT NOT NULL,
    puntualidad INTEGER NOT NULL,
    cobertura INTEGER NOT NULL,
    retrasos INTEGER NOT NULL,
    recorridos_completados INTEGER NOT NULL,
    incidentes_registrados INTEGER NOT NULL,
    reasignaciones_aceptadas INTEGER NOT NULL,
    puntaje_total INTEGER NOT NULL,
    periodo VARCHAR(255) NOT NULL,
    CONSTRAINT fk_metricas_docente_docente FOREIGN KEY (docente_id) REFERENCES docentes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reconocimientos (
    id BIGSERIAL PRIMARY KEY,
    metrica_docente_id BIGINT NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    tipo VARCHAR(64) NOT NULL,
    otorgado_en DATE NOT NULL,
    trimestre VARCHAR(255) NOT NULL,
    CONSTRAINT fk_reconocimientos_metrica FOREIGN KEY (metrica_docente_id) REFERENCES metricas_docente(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS mapas_calor (
    id BIGSERIAL PRIMARY KEY,
    zona_id BIGINT NOT NULL,
    franja VARCHAR(255) NOT NULL,
    tipo_incidente VARCHAR(64) NOT NULL,
    total_incidentes INTEGER NOT NULL,
    porcentaje REAL NOT NULL,
    periodo_inicio DATE NOT NULL,
    periodo_fin DATE NOT NULL,
    CONSTRAINT fk_mapas_calor_zona FOREIGN KEY (zona_id) REFERENCES zonas(id) ON DELETE CASCADE
);
