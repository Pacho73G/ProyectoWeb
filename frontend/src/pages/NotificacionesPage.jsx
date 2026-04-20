/* Archivo documentado: Pantalla principal de la SPA. Consume la API y presenta una vista funcional del módulo correspondiente. */
import { CrudPage } from './CrudPage';
import { getNotificaciones, deleteNotificacion } from '../api/notificacion.api';
import { NotificacionForm } from './forms/NotificacionForm';

export function NotificacionesPage() {
  const columns = [
    { key: 'turnoFranja', label: 'Turno' },
    { key: 'tipo', label: 'Tipo' },
    { key: 'leida', label: 'Leída', render: (row) => String(row.leida) },
    { key: 'minutosAnticipacion', label: 'Anticipación' },
  ];

  return (
    <CrudPage
      title="Notificaciones"
      toolbarNote="Mensajes y recordatorios asociados a los turnos."
      createLabel="Nueva notificación"
      fetchFn={getNotificaciones}
      deleteFn={deleteNotificacion}
      columns={columns}
      FormComponent={NotificacionForm}
      formPropName="notificacion"
      deleteMessage="¿Eliminar esta notificación? Esta acción no se puede deshacer."
      resource="notificaciones"
    />
  );
}
