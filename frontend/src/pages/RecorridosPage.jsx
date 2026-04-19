import { CrudPage } from './CrudPage';
import { getRecorridos, deleteRecorrido } from '../api/recorrido.api';
import { RecorridoForm } from './forms/RecorridoForm';

export function RecorridosPage() {
  const columns = [
    { key: 'docenteNombre', label: 'Docente' },
    { key: 'turnoFranja', label: 'Turno' },
    { key: 'estado', label: 'Estado' },
    { key: 'duracionMinutos', label: 'Duración' },
  ];

  return (
    <CrudPage
      title="Recorridos"
      toolbarNote="Vigilancia activa y evidencias de recorrido."
      createLabel="Nuevo recorrido"
      fetchFn={getRecorridos}
      deleteFn={deleteRecorrido}
      columns={columns}
      FormComponent={RecorridoForm}
      formPropName="recorrido"
      deleteMessage="¿Eliminar este recorrido? Esta acción no se puede deshacer."
      resource="recorridos"
    />
  );
}
