import { EntityForm } from './EntityForm';
import { createLimpieza, updateLimpieza } from '../../api/limpieza.api';
import { getTurnos } from '../../api/turno.api';
import { limpiezaEscalas } from './options';

export function LimpiezaForm({ limpieza, onSave, onCancel }) {
  const fields = [
    { name: 'turnoId', label: 'Turno', type: 'select', required: true, loader: 'turnos', optionLabel: (item) => item.franja, full: true },
    { name: 'escala', label: 'Escala', type: 'select', required: true, options: limpiezaEscalas },
    { name: 'registradoEn', label: 'Registrado en', type: 'datetime-local', required: true },
    { name: 'observaciones', label: 'Observaciones', type: 'textarea', required: true, full: true },
  ];

  return (
    <EntityForm
      entity={limpieza}
      title={limpieza ? 'Editar limpieza' : 'Nueva limpieza'}
      description="Cierra turnos con una valoración básica del estado de limpieza."
      fields={fields}
      loaders={{ turnos: { fetcher: getTurnos } }}
      createAction={createLimpieza}
      updateAction={updateLimpieza}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
