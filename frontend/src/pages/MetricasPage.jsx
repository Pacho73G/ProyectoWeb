/* Métricas filtradas por docente */

import { CrudPage } from './CrudPage';
import { getMetricas, deleteMetrica } from '../api/metrica.api';
import { MetricaForm } from './forms/MetricaForm';
import { getRole, getDocenteId } from '../roleConfig';

export function MetricasPage() {
  const role = getRole();
  const docenteId = getDocenteId();

  const columns = [
    { key: 'docenteNombre', label: 'Docente' },
    { key: 'periodo', label: 'Periodo' },
    { key: 'puntualidad', label: 'Puntualidad' },
    { key: 'puntajeTotal', label: 'Puntaje total' },
  ];

  // 🔥 FILTRO CLAVE
  const filterFn = (data) =>
    role === 'docente'
      ? data.filter((m) => String(m.docenteId) === String(docenteId))
      : data;

  return (
    <CrudPage
      title="Métricas docentes"
      toolbarNote="Gamificación y desempeño docente."
      createLabel="Nueva métrica"
      fetchFn={getMetricas}
      deleteFn={deleteMetrica}
      columns={columns}
      FormComponent={MetricaForm}
      formPropName="metrica"
      deleteMessage="¿Eliminar esta métrica? Esta acción no se puede deshacer."
      resource="metricas"
      filterFn={filterFn}
    />
  );
}