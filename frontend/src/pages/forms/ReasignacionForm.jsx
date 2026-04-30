import { EntityForm } from './EntityForm';
import { createReasignacion, updateReasignacion } from '../../api/reasignacion.api';
import { getTurnos } from '../../api/turno.api';
import { getDocentes } from '../../api/usuario.api';
import { reasignacionEstados } from './options';
import {
  getRole,
  getDocenteId,
} from '../../roleConfig';

export function ReasignacionForm({ reasignacion, onSave, onCancel }) {
  const role = getRole();
  const docenteId = getDocenteId();
  const esDocente = role === 'docente';

  const fields = [
    {
      name: 'turnoId',
      label: 'Turno',
      type: 'select',
      required: true,
      loader: 'turnos',
      optionLabel: (item) => `${item.franja} · ${item.fecha}`,
    },

    ...(!esDocente
      ? [{
          name: 'docenteSolicitanteId',
          label: 'Docente solicitante',
          type: 'select',
          required: true,
          loader: 'docentes',
          optionLabel: (item) => item.nombre,
        }]
      : []),

    {
      name: 'docenteReemplazoId',
      label: 'Docente reemplazo',
      type: 'select',
      loader: 'docentes',
      optionLabel: (item) => item.nombre,
      allowEmpty: true,
      placeholder: 'Pendiente',
    },

    ...(!esDocente
      ? [
          {
            name: 'estado',
            label: 'Estado',
            type: 'select',
            required: true,
            options: reasignacionEstados,
          },
          {
            name: 'respondidaEn',
            label: 'Respondida en',
            type: 'datetime-local',
          },
        ]
      : []),

    {
      name: 'propuestaEn',
      label: 'Propuesta en',
      type: 'datetime-local',
      required: true,
    },
    {
      name: 'segundosVentana',
      label: 'Ventana en segundos',
      type: 'number',
      required: true,
      min: 0,
    },
    {
      name: 'motivo',
      label: 'Motivo',
      type: 'textarea',
      required: true,
      full: true,
    },
  ];

  const loaders = {
    turnos: {
      fetcher: async () => {
        const data = await getTurnos();
        return esDocente
          ? data.filter((t) => t.docenteId === docenteId)
          : data;
      },
    },
    docentes: {
      fetcher: async () => {
        const data = await getDocentes();

        if (esDocente) {
          return data.filter((d) => d.id !== docenteId);
        }

        return data;
      },
    },
  };

  const entityData = esDocente
    ? {
        ...reasignacion,
        docenteSolicitanteId: docenteId,
        estado: 'PENDIENTE',
        respondidaEn: null,
      }
    : reasignacion;

  return (
    <EntityForm
      entity={entityData}
      title={reasignacion ? 'Editar reasignación' : 'Nueva reasignación'}
      description="Solicitud de reemplazo."
      fields={fields}
      loaders={loaders}
      createAction={createReasignacion}
      updateAction={updateReasignacion}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
