import { CrudPage } from './CrudPage';
import { getCheckIns, deleteCheckIn } from '../api/checkin.api';
import { CheckInForm } from './forms/CheckInForm';

export function CheckInsPage() {
  const columns = [
    {
      key: 'turnoFranja',
      label: 'Turno',
      render: (row) => row.turnoFranja,
    },
    { key: 'docenteNombre', label: 'Docente' },
    { key: 'metodo', label: 'Método' },
    { key: 'valido', label: 'Válido', render: (row) => String(row.valido) },
  ];

  return (
    <CrudPage
      title="Check-ins"
      toolbarNote="Validación del ingreso por turno mediante QR, PIN o NFC."
      createLabel="Nuevo check-in"
      fetchFn={getCheckIns}
      deleteFn={deleteCheckIn}
      columns={columns}
      FormComponent={CheckInForm}
      formPropName="checkin"
      deleteMessage="¿Eliminar este check-in? Esta acción no se puede deshacer."
      resource="checkins"
    />
  );
}
