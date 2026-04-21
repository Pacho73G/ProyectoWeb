/* Archivo documentado: Pantalla de check-ins. Para el rol docente filtra únicamente sus propios registros. */
import { useCallback } from 'react';
import { CrudPage } from './CrudPage';
import { getCheckIns, deleteCheckIn } from '../api/checkin.api';
import { CheckInForm } from './forms/CheckInForm';
import { getRole, getDocenteId } from '../roleConfig';

export function CheckInsPage() {
  const role = getRole();
  const docenteId = getDocenteId();

  const columns = [
    {
      key: 'turnoFranja',
      label: 'Turno',
      render: (row) => (
        <div className="table-main">
          <strong>{row.turnoFranja}</strong>
          <small>{row.zonaNombre}</small>
        </div>
      ),
    },
    { key: 'docenteNombre', label: 'Docente' },
    {
      key: 'metodo',
      label: 'Método',
      render: (row) => <span className="badge en_curso">{row.metodo}</span>
    },
    { key: 'timestamp', label: 'Fecha/Hora' },
    { key: 'valido', label: 'Válido', render: (row) => String(row.valido) },
  ];

  const filterFn = useCallback(
    (data) => (docenteId ? data.filter((c) => c.docenteId === docenteId) : data),
    [docenteId],
  );

  return (
    <CrudPage
      title="Check-ins"
      subtitle={
        role === 'docente'
          ? 'Historial de tus registros de entrada y salida en cada turno.'
          : 'Validación del ingreso por turno mediante QR, PIN o NFC.'
      }
      toolbarNote={
        role === 'docente'
          ? 'Para iniciar o finalizar un turno, ve a "Mis turnos" y usa los botones de acción.'
          : 'Validación del ingreso por turno mediante QR, PIN o NFC.'
      }
      createLabel="Nuevo check-in"
      fetchFn={getCheckIns}
      deleteFn={deleteCheckIn}
      filterFn={role === 'docente' ? filterFn : undefined}
      columns={columns}
      FormComponent={CheckInForm}
      formPropName="checkin"
      deleteMessage="¿Eliminar este check-in? Esta acción no se puede deshacer."
      resource="checkins"
    />
  );
}