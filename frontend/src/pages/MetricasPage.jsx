import { CrudPage } from './CrudPage';
import { getMetricas, deleteMetrica } from '../api/metrica.api';
import { MetricaForm } from './forms/MetricaForm';

export function MetricasPage() {
  const columns = [
    { key: 'docenteNombre', label: 'Docente' },
    { key: 'periodo', label: 'Periodo' },
    { key: 'puntualidad', label: 'Puntualidad' },
    { key: 'puntajeTotal', label: 'Puntaje total' },
  ];

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
    />
  );
}
