/* Reconocimientos filtrados por docente */

import { CrudPage } from './CrudPage';
import { getReconocimientos, deleteReconocimiento } from '../api/reconocimiento.api';
import { ReconocimientoForm } from './forms/ReconocimientoForm';
import { getRole, getDocenteId } from '../roleConfig';

export function ReconocimientosPage() {
  const role = getRole();
  const docenteId = getDocenteId();

  const columns = [
    { key: 'titulo', label: 'Título' },
    { key: 'docenteNombre', label: 'Docente' },
    { key: 'tipo', label: 'Tipo' },
    { key: 'trimestre', label: 'Trimestre' },
  ];

  // 🔥 FILTRO CLAVE
  const filterFn = (data) =>
    role === 'docente'
      ? data.filter((r) => String(r.docenteId) === String(docenteId))
      : data;

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
      filterFn={filterFn}
    />
  );
}