import { EntityForm } from './EntityForm';
import { createIncidente, updateIncidente } from '../../api/incidente.api';
import { getTurnos } from '../../api/turno.api';
import { getDocentes } from '../../api/usuario.api';
import { getZonas } from '../../api/zona.api';
import { incidenteSeveridades, incidenteTipos } from './options';
import {
  getRole,
  getDocenteId,
  getUserNombre,
} from '../../roleConfig';
import { pushNotification } from '../../api/notificacion.api';

export function IncidenteForm({ incidente, onSave, onCancel }) {
  const role = getRole();
  const docenteId = getDocenteId();
  const userNombre = getUserNombre();

  const fields = [
    {
      name: 'turnoId',
      label: 'Turno',
      type: 'select',
      required: true,
      loader: 'turnos',
      optionLabel: (item) => item.franja,
    },

    ...(role !== 'docente'
      ? [
          {
            name: 'docenteId',
            label: 'Docente',
            type: 'select',
            required: true,
            loader: 'docentes',
            optionLabel: (item) => item.nombre,
          },
        ]
      : []),

    {
      name: 'zonaId',
      label: 'Zona',
      type: 'select',
      required: true,
      loader: 'zonas',
      optionLabel: (item) => item.nombre,
    },
    {
      name: 'tipo',
      label: 'Tipo',
      type: 'select',
      required: true,
      options: incidenteTipos,
    },
    {
      name: 'severidad',
      label: 'Severidad',
      type: 'select',
      required: true,
      options: incidenteSeveridades,
    },
    {
      name: 'registradoEn',
      label: 'Registrado en',
      type: 'datetime-local',
      required: true,
    },
    {
      name: 'descripcion',
      label: 'Descripción',
      type: 'textarea',
      required: true,
      full: true,
    },
    {
      name: 'observacionSocial',
      label: 'Observación social',
      type: 'textarea',
      full: true,
    },
    {
      name: 'requiereSeguimiento',
      label: 'Requiere seguimiento',
      type: 'checkbox',
      full: true,
    },
  ];

  const loaders = {
    turnos: {
      fetcher: async () => {
        const data = await getTurnos();
        return role === 'docente'
          ? data.filter((t) => t.docenteId === docenteId)
          : data;
      },
    },
    docentes: {
      fetcher: getDocentes,
    },
    zonas: {
      fetcher: getZonas,
    },
  };

  const preparePayload = (payload) => {
    if (role === 'docente') {
      payload.docenteId = docenteId;
    }
    return payload;
  };

  const handleSave = () => {
    pushNotification({
      title: 'Nuevo incidente',
      message: `${userNombre} registró un incidente.`,
      role: 'coordinador',
    });

    pushNotification({
      title: 'Nuevo incidente',
      message: `${userNombre} registró un incidente.`,
      role: 'administrador',
    });

    onSave();
  };

  return (
    <EntityForm
      entity={incidente}
      title={incidente ? 'Editar incidente' : 'Nuevo incidente'}
      description="Documenta lo ocurrido con severidad, zona y contexto en una sola vista."
      fields={fields}
      loaders={loaders}
      createAction={(data) => createIncidente(preparePayload(data))}
      updateAction={(id, data) =>
        updateIncidente(id, preparePayload(data))
      }
      onSave={handleSave}
      onCancel={onCancel}
    />
  );
}