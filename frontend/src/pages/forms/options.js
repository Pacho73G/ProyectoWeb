/* Archivo documentado: Opciones estáticas compartidas por varios formularios de la SPA. Evita repetir catálogos simples dentro de cada componente. */
export const turnoEstados = [
  { value: 'PENDIENTE', label: 'PENDIENTE' },
  { value: 'EN_CURSO', label: 'EN_CURSO' },
  { value: 'CERRADO', label: 'CERRADO' },
  { value: 'SIN_COBERTURA', label: 'SIN_COBERTURA' },
];

export const incidenteTipos = [
  { value: 'SEGURIDAD_FISICA', label: 'SEGURIDAD_FISICA' },
  { value: 'CONVIVENCIA', label: 'CONVIVENCIA' },
  { value: 'USO_ESPACIO', label: 'USO_ESPACIO' },
  { value: 'OBSERVACION_SOCIAL', label: 'OBSERVACION_SOCIAL' },
];

export const incidenteSeveridades = [
  { value: 'S1_LEVE', label: 'S1_LEVE' },
  { value: 'S2_SEGUIMIENTO', label: 'S2_SEGUIMIENTO' },
  { value: 'S3_ATENCION_INMEDIATA', label: 'S3_ATENCION_INMEDIATA' },
];

export const checkinMetodos = [
  { value: 'QR', label: 'QR' },
  { value: 'PIN', label: 'PIN' },
  { value: 'NFC', label: 'NFC' },
];

export const reasignacionEstados = [
  { value: 'PROPUESTA', label: 'PROPUESTA' },
  { value: 'ACEPTADA', label: 'ACEPTADA' },
  { value: 'RECHAZADA', label: 'RECHAZADA' },
  { value: 'EXPIRADA', label: 'EXPIRADA' },
];

export const limpiezaEscalas = [
  { value: 1, label: '1' },
  { value: 2, label: '2' },
  { value: 3, label: '3' },
  { value: 4, label: '4' },
];

export const notificacionTipos = [
  { value: 'RECORDATORIO_10MIN', label: 'RECORDATORIO_10MIN' },
  { value: 'RECORDATORIO_5MIN', label: 'RECORDATORIO_5MIN' },
  { value: 'ALERTA_AUSENCIA', label: 'ALERTA_AUSENCIA' },
  { value: 'PROPUESTA_REEMPLAZO', label: 'PROPUESTA_REEMPLAZO' },
  { value: 'CONFIRMACION_CHECKIN', label: 'CONFIRMACION_CHECKIN' },
];

export const rolesUsuario = [
  { value: 'DOCENTE', label: 'DOCENTE' },
  { value: 'COORDINADOR', label: 'COORDINADOR' },
  { value: 'ADMINISTRADOR', label: 'ADMINISTRADOR' },
];

export const reconocimientoTipos = [
  { value: 'PUNTUALIDAD', label: 'PUNTUALIDAD' },
  { value: 'RECORRIDOS', label: 'RECORRIDOS' },
  { value: 'CALIDAD_REGISTRO', label: 'CALIDAD_REGISTRO' },
  { value: 'CONTRIBUCION_PREVENTIVA', label: 'CONTRIBUCION_PREVENTIVA' },
];

export const recorridoEstados = [
  { value: 'EN_PROGRESO', label: 'EN_PROGRESO' },
  { value: 'COMPLETADO', label: 'COMPLETADO' },
  { value: 'INCOMPLETO', label: 'INCOMPLETO' },
];
