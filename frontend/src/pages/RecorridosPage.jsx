import { useCallback } from 'react';
import { CrudPage } from './CrudPage';
import { getRecorridos, deleteRecorrido } from '../api/recorrido.api';
import { RecorridoForm } from './forms/RecorridoForm';
import { getRole, getDocenteId } from '../roleConfig';

export function RecorridosPage() {
  const role = getRole();
  const docenteId = getDocenteId();

  const columns = [
    { key: 'docenteNombre', label: 'Docente' },
    { key: 'turnoFranja', label: 'Turno' },
    { key: 'estado', label: 'Estado' },
    { key: 'duracionMinutos', label: 'Duración (min)' },
    { key: 'iniciadoEn', label: 'Inicio' },
  ];

  const filterFn = useCallback(
    (data) =>
      docenteId
        ? data.filter((r) => r.docenteId === docenteId)
        : data,
    [docenteId]
  );

  return (
    <CrudPage
      title="Recorridos"
      subtitle={
        role === 'docente'
          ? 'Tus recorridos registrados durante los turnos.'
          : 'Vigilancia activa y evidencias de recorrido.'
      }
      toolbarNote={
        role === 'docente'
          ? 'Registro de recorridos activos en tu zona asignada.'
          : 'Vigilancia activa y evidencias de recorrido.'
      }
      createLabel="Nuevo recorrido"
      fetchFn={getRecorridos}
      deleteFn={deleteRecorrido}
      filterFn={role === 'docente' ? filterFn : undefined}
      columns={columns}
      FormComponent={RecorridoForm}
      formPropName="recorrido"
      deleteMessage="¿Eliminar este recorrido? Esta acción no se puede deshacer."
      resource="recorridos"
    />
  );
}