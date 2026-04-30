/* Archivo documentado: Formulario modal de la SPA. Orquesta campos, carga de opciones y acciones de creación o edición para una entidad concreta. */
import { EntityForm } from './EntityForm';
import { createNotificacion, updateNotificacion } from '../../api/notificacion.api';
import { getTurnos } from '../../api/turno.api';
import { getUsuarios } from '../../api/usuario.api';
import { notificacionTipos } from './options';

export function NotificacionForm({ notificacion, onSave, onCancel }) {
  const fields = [
    { name: 'turnoId', label: 'Turno', type: 'select', loader: 'turnos', optionLabel: (item) => item.franja, allowEmpty: true, placeholder: 'Sin turno', full: true },
    { name: 'destinatarioId', label: 'Destinatario', type: 'select', required: true, loader: 'usuarios', optionLabel: (item) => `${item.nombre} · ${item.rol}`, full: true },
    { name: 'tipo', label: 'Tipo', type: 'select', required: true, options: notificacionTipos },
    { name: 'titulo', label: 'Título', type: 'text', required: true, full: true },
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
      loaders={{ turnos: { fetcher: getTurnos }, usuarios: { fetcher: getUsuarios } }}
      createAction={createNotificacion}
      updateAction={updateNotificacion}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
