/* Archivo documentado: Pantalla principal de la SPA. Consume la API y presenta una vista funcional del módulo correspondiente. */
import { CrudPage } from './CrudPage';
import { getMapasCalor, deleteMapaCalor } from '../api/mapaCalor.api';
import { MapaCalorForm } from './forms/MapaCalorForm';

export function MapasCalorPage() {
  const columns = [
    { key: 'zonaNombre', label: 'Zona' },
    { key: 'franja', label: 'Franja' },
    { key: 'tipoIncidente', label: 'Tipo' },
    { key: 'totalIncidentes', label: 'Total' },
    { key: 'porcentaje', label: 'Porcentaje' },
  ];

  return (
    <CrudPage
      title="Mapas de calor"
      toolbarNote="Analítica de incidentes por zona y franja."
      createLabel="Nuevo mapa"
      fetchFn={getMapasCalor}
      deleteFn={deleteMapaCalor}
      columns={columns}
      FormComponent={MapaCalorForm}
      formPropName="mapaCalor"
      deleteMessage="¿Eliminar este mapa de calor? Esta acción no se puede deshacer."
      resource="mapas-calor"
    />
  );
}
