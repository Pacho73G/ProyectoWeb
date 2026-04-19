import { CrudPage } from './CrudPage';
import { getReconocimientos, deleteReconocimiento } from '../api/reconocimiento.api';
import { ReconocimientoForm } from './forms/ReconocimientoForm';

export function ReconocimientosPage() {
  const columns = [
    { key: 'titulo', label: 'Título' },
    { key: 'docenteNombre', label: 'Docente' },
    { key: 'tipo', label: 'Tipo' },
    { key: 'trimestre', label: 'Trimestre' },
  ];

  return (
    <CrudPage
      title="Reconocimientos"
      toolbarNote="Reconocimientos generados desde métricas."
      createLabel="Nuevo reconocimiento"
      fetchFn={getReconocimientos}
      deleteFn={deleteReconocimiento}
      columns={columns}
      FormComponent={ReconocimientoForm}
      formPropName="reconocimiento"
      deleteMessage="¿Eliminar este reconocimiento? Esta acción no se puede deshacer."
      resource="reconocimientos"
    />
  );
}
