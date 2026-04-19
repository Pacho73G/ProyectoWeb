import { EntityForm } from './EntityForm';
import { createMapaCalor, updateMapaCalor } from '../../api/mapaCalor.api';
import { getZonas } from '../../api/zona.api';
import { incidenteTipos } from './options';

export function MapaCalorForm({ mapaCalor, onSave, onCancel }) {
  const fields = [
    { name: 'zonaId', label: 'Zona', type: 'select', required: true, loader: 'zonas', optionLabel: (item) => item.nombre },
    { name: 'franja', label: 'Franja', type: 'text', required: true },
    { name: 'tipoIncidente', label: 'Tipo incidente', type: 'select', required: true, options: incidenteTipos },
    { name: 'totalIncidentes', label: 'Total incidentes', type: 'number', required: true, min: 0 },
    { name: 'porcentaje', label: 'Porcentaje', type: 'number', required: true, min: 0, step: '0.01' },
    { name: 'periodoInicio', label: 'Periodo inicio', type: 'date', required: true },
    { name: 'periodoFin', label: 'Periodo fin', type: 'date', required: true },
  ];

  return (
    <EntityForm
      entity={mapaCalor}
      title={mapaCalor ? 'Editar mapa de calor' : 'Nuevo mapa de calor'}
      description="Registra analítica de incidentes por zona, franja y periodo."
      fields={fields}
      loaders={{ zonas: { fetcher: getZonas } }}
      createAction={createMapaCalor}
      updateAction={updateMapaCalor}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
