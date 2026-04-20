/* Archivo documentado: Formulario modal de la SPA. Orquesta campos, carga de opciones y acciones de creación o edición para una entidad concreta. */
import { EntityForm } from './EntityForm';
import { createMetrica, updateMetrica } from '../../api/metrica.api';
import { getDocentes } from '../../api/usuario.api';

export function MetricaForm({ metrica, onSave, onCancel }) {
  const fields = [
    { name: 'docenteId', label: 'Docente', type: 'select', required: true, loader: 'docentes', optionLabel: (item) => item.nombre, full: true },
    { name: 'puntualidad', label: 'Puntualidad', type: 'number', required: true, min: 0 },
    { name: 'cobertura', label: 'Cobertura', type: 'number', required: true, min: 0 },
    { name: 'retrasos', label: 'Retrasos', type: 'number', required: true, min: 0 },
    { name: 'recorridosCompletados', label: 'Recorridos completados', type: 'number', required: true, min: 0 },
    { name: 'incidentesRegistrados', label: 'Incidentes registrados', type: 'number', required: true, min: 0 },
    { name: 'reasignacionesAceptadas', label: 'Reasignaciones aceptadas', type: 'number', required: true, min: 0 },
    { name: 'puntajeTotal', label: 'Puntaje total', type: 'number', required: true, min: 0 },
    { name: 'periodo', label: 'Periodo', type: 'text', required: true, full: true },
  ];

  return (
    <EntityForm
      entity={metrica}
      title={metrica ? 'Editar métrica' : 'Nueva métrica'}
      description="Consolida gamificación y desempeño docente por periodo."
      fields={fields}
      loaders={{ docentes: { fetcher: getDocentes } }}
      createAction={createMetrica}
      updateAction={updateMetrica}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
