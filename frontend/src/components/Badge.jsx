/* Archivo documentado: Componente reutilizable de la interfaz. Aísla una pieza visual o de interacción compartida por varias pantallas. */
const variantMap = {
  PENDIENTE: 'warning',
  EN_CURSO: 'success',
  CERRADO: 'info',
  SIN_COBERTURA: 'danger',
  PROPUESTA: 'warning',
  ACEPTADA: 'success',
  RECHAZADA: 'danger',
  EXPIRADA: 'warning',
  EN_PROGRESO: 'success',
  COMPLETADO: 'success',
  INCOMPLETO: 'danger',
  S1: 'info',
  S2: 'warning',
  S3: 'danger',
  S1_LEVE: 'info',
  S2_SEGUIMIENTO: 'warning',
  S3_ATENCION_INMEDIATA: 'danger',
  true: 'success',
  false: 'danger',
};

export function Badge({ value }) {
  return <span className={`badge ${variantMap[String(value)] ?? 'info'}`}>{String(value)}</span>;
}
