import { EntityForm } from './EntityForm';
import { createIncidente, updateIncidente } from '../../api/incidente.api';
import { getTurnos } from '../../api/turno.api';
import { getDocentes } from '../../api/usuario.api';
import { getZonas } from '../../api/zona.api';
import { incidenteSeveridades, incidenteTipos } from './options';

export function IncidenteForm({ incidente, onSave, onCancel }) {
  const fields = [
    { name: 'turnoId', label: 'Turno', type: 'select', required: true, loader: 'turnos', optionLabel: (item) => item.franja },
    { name: 'docenteId', label: 'Docente', type: 'select', required: true, loader: 'docentes', optionLabel: (item) => item.nombre },
    { name: 'zonaId', label: 'Zona', type: 'select', required: true, loader: 'zonas', optionLabel: (item) => item.nombre },
    { name: 'tipo', label: 'Tipo', type: 'select', required: true, options: incidenteTipos },
    { name: 'severidad', label: 'Severidad', type: 'select', required: true, options: incidenteSeveridades },
    { name: 'registradoEn', label: 'Registrado en', type: 'datetime-local', required: true },
    { name: 'descripcion', label: 'Descripción', type: 'textarea', required: true, full: true },
    { name: 'observacionSocial', label: 'Observación social', type: 'textarea', full: true },
    { name: 'requiereSeguimiento', label: 'Requiere seguimiento', type: 'checkbox', full: true },
  ];

  return (
    <EntityForm
      entity={incidente}
      title={incidente ? 'Editar incidente' : 'Nuevo incidente'}
      description="Documenta lo ocurrido con severidad, zona y contexto en una sola vista."
      fields={fields}
      loaders={{
        turnos: { fetcher: getTurnos },
        docentes: { fetcher: getDocentes },
        zonas: { fetcher: getZonas },
      }}
      createAction={createIncidente}
      updateAction={updateIncidente}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
