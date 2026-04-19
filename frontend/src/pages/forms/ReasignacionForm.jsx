import { EntityForm } from './EntityForm';
import { createReasignacion, updateReasignacion } from '../../api/reasignacion.api';
import { getTurnos } from '../../api/turno.api';
import { getDocentes } from '../../api/usuario.api';
import { reasignacionEstados } from './options';

export function ReasignacionForm({ reasignacion, onSave, onCancel }) {
  const fields = [
    { name: 'turnoId', label: 'Turno', type: 'select', required: true, loader: 'turnos', optionLabel: (item) => item.franja },
    { name: 'docenteSolicitanteId', label: 'Docente solicitante', type: 'select', required: true, loader: 'docentes', optionLabel: (item) => item.nombre },
    { name: 'docenteReemplazoId', label: 'Docente reemplazo', type: 'select', loader: 'docentes', optionLabel: (item) => item.nombre, allowEmpty: true, placeholder: 'Pendiente' },
    { name: 'estado', label: 'Estado', type: 'select', required: true, options: reasignacionEstados },
    { name: 'propuestaEn', label: 'Propuesta en', type: 'datetime-local', required: true },
    { name: 'respondidaEn', label: 'Respondida en', type: 'datetime-local' },
    { name: 'segundosVentana', label: 'Ventana en segundos', type: 'number', required: true, min: 0 },
    { name: 'motivo', label: 'Motivo', type: 'textarea', required: true, full: true },
  ];

  return (
    <EntityForm
      entity={reasignacion}
      title={reasignacion ? 'Editar reasignación' : 'Nueva reasignación'}
      description="Visualiza quién pidió apoyo, el reemplazo propuesto y el estado de respuesta del turno."
      fields={fields}
      loaders={{
        turnos: { fetcher: getTurnos },
        docentes: { fetcher: getDocentes },
      }}
      createAction={createReasignacion}
      updateAction={updateReasignacion}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
