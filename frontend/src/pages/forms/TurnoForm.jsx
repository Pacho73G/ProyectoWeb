import { EntityForm } from './EntityForm';
import { createTurno, updateTurno } from '../../api/turno.api';
import { getDocentes } from '../../api/usuario.api';
import { getZonas } from '../../api/zona.api';
import { turnoEstados } from './options';

export function TurnoForm({ turno, onSave, onCancel }) {
  const fields = [
    { name: 'docenteId', label: 'Docente', type: 'select', required: true, loader: 'docentes', optionLabel: (item) => item.nombre },
    { name: 'zonaId', label: 'Zona', type: 'select', required: true, loader: 'zonas', optionLabel: (item) => item.nombre },
    {name: 'fecha',label: 'Fecha',type: 'date',required: true,min: new Date().toISOString().split('T')[0]},
    { name: 'franja', label: 'Franja o nombre del turno', type: 'text', required: true },
    { name: 'horaInicio', label: 'Hora inicio', type: 'time', required: true },
    { name: 'horaFin', label: 'Hora fin', type: 'time', required: true },
    { name: 'estado', label: 'Estado', type: 'select', required: true, options: turnoEstados },
    { name: 'abiertoEn', label: 'Abierto en', type: 'datetime-local' },
    { name: 'cerradoEn', label: 'Cerrado en', type: 'datetime-local' },
  ];

  return (
    <EntityForm
      entity={turno}
      title={turno ? 'Editar turno' : 'Nuevo turno'}
      description="Gestión de turnos."
      fields={fields}
      loaders={{
        docentes: { fetcher: getDocentes },
        zonas: { fetcher: getZonas },
      }}
      createAction={createTurno}
      updateAction={updateTurno}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
