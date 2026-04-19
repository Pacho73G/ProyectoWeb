import { EntityForm } from './EntityForm';
import { createNotificacion, updateNotificacion } from '../../api/notificacion.api';
import { getTurnos } from '../../api/turno.api';
import { notificacionTipos } from './options';

export function NotificacionForm({ notificacion, onSave, onCancel }) {
  const fields = [
    { name: 'turnoId', label: 'Turno', type: 'select', required: true, loader: 'turnos', optionLabel: (item) => item.franja, full: true },
    { name: 'tipo', label: 'Tipo', type: 'select', required: true, options: notificacionTipos },
    { name: 'enviadaEn', label: 'Enviada en', type: 'datetime-local', required: true },
    { name: 'minutosAnticipacion', label: 'Minutos anticipación', type: 'number', required: true, min: 0 },
    { name: 'leida', label: 'Marcada como leída', type: 'checkbox', full: true },
    { name: 'mensaje', label: 'Mensaje', type: 'textarea', required: true, full: true },
  ];

  return (
    <EntityForm
      entity={notificacion}
      title={notificacion ? 'Editar notificación' : 'Nueva notificación'}
      description="Gestiona mensajes y recordatorios asociados a los turnos."
      fields={fields}
      loaders={{ turnos: { fetcher: getTurnos } }}
      createAction={createNotificacion}
      updateAction={updateNotificacion}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
