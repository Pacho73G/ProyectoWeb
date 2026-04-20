/* Archivo documentado: Formulario modal de la SPA. Orquesta campos, carga de opciones y acciones de creación o edición para una entidad concreta. */
import { EntityForm } from './EntityForm';
import { createReconocimiento, updateReconocimiento } from '../../api/reconocimiento.api';
import { getMetricas } from '../../api/metrica.api';
import { reconocimientoTipos } from './options';

export function ReconocimientoForm({ reconocimiento, onSave, onCancel }) {
  const fields = [
    { name: 'metricaDocenteId', label: 'Métrica docente', type: 'select', required: true, loader: 'metricas', optionLabel: (item) => `${item.docenteNombre} · ${item.periodo}` },
    { name: 'titulo', label: 'Título', type: 'text', required: true },
    { name: 'tipo', label: 'Tipo', type: 'select', required: true, options: reconocimientoTipos },
    { name: 'otorgadoEn', label: 'Otorgado en', type: 'date', required: true },
    { name: 'trimestre', label: 'Trimestre', type: 'text', required: true },
    { name: 'descripcion', label: 'Descripción', type: 'textarea', required: true, full: true },
  ];

  return (
    <EntityForm
      entity={reconocimiento}
      title={reconocimiento ? 'Editar reconocimiento' : 'Nuevo reconocimiento'}
      description="Genera reconocimientos institucionales a partir de métricas positivas."
      fields={fields}
      loaders={{ metricas: { fetcher: getMetricas } }}
      createAction={createReconocimiento}
      updateAction={updateReconocimiento}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
