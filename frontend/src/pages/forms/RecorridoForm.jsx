import { EntityForm } from './EntityForm';
import { createRecorrido, updateRecorrido } from '../../api/recorrido.api';
import { getTurnos } from '../../api/turno.api';
import { getDocentes } from '../../api/usuario.api';
import { recorridoEstados } from './options';

export function RecorridoForm({ recorrido, onSave, onCancel }) {
  const fields = [
    { name: 'docenteId', label: 'Docente', type: 'select', required: true, loader: 'docentes', optionLabel: (item) => item.nombre },
    { name: 'turnoId', label: 'Turno', type: 'select', required: true, loader: 'turnos', optionLabel: (item) => item.franja },
    { name: 'iniciadoEn', label: 'Iniciado en', type: 'datetime-local', required: true },
    { name: 'finalizadoEn', label: 'Finalizado en', type: 'datetime-local' },
    { name: 'estado', label: 'Estado', type: 'select', required: true, options: recorridoEstados },
    { name: 'duracionMinutos', label: 'Duración', type: 'number', required: true, min: 0 },
  ];

  return (
    <EntityForm
      entity={recorrido}
      title={recorrido ? 'Editar recorrido' : 'Nuevo recorrido'}
      description="Registra evidencia de vigilancia activa dentro de un turno."
      fields={fields}
      loaders={{
        docentes: { fetcher: getDocentes },
        turnos: { fetcher: getTurnos },
      }}
      createAction={createRecorrido}
      updateAction={updateRecorrido}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
