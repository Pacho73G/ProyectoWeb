import { EntityForm } from './EntityForm';
import { createLimpieza, updateLimpieza } from '../../api/limpieza.api';
import { getTurnos } from '../../api/turno.api';
import { getDocentes } from '../../api/usuario.api';
import { getZonas } from '../../api/zona.api';
import { limpiezaEscalas } from './options';
import {
  getRole,
  getDocenteId,
} from '../../roleConfig';

export function LimpiezaForm({ limpieza, onSave, onCancel }) {
  const role = getRole();
  const docenteId = getDocenteId();

  const fields = [
    ...(role !== 'docente'
      ? [
          {
            name: 'docenteId',
            label: 'Docente asignado',
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
      full: true,
    },
    {
      name: 'turnoId',
      label: 'Turno',
      type: 'select',
      loader: 'turnos',
      optionLabel: (item) => `${item.franja} · ${item.fecha}`,
      allowEmpty: true,
      placeholder: 'Sin turno',
      full: true,
    },
    {
      name: 'escala',
      label: 'Escala',
      type: 'select',
      required: true,
      options: limpiezaEscalas,
    },
    {
      name: 'asignadaEn',
      label: 'Asignada en',
      type: 'datetime-local',
      required: true,
    },
    {
      name: 'registradoEn',
      label: 'Registrado en',
      type: 'datetime-local',
      required: true,
    },
    {
      name: 'observaciones',
      label: 'Observaciones',
      type: 'textarea',
      required: true,
      full: true,
    },
    {
      name: 'completada',
      label: 'Completada',
      type: 'checkbox',
      full: true,
    },
  ];

  const loaders = {
    turnos: {
      fetcher: async () => {
        const data = await getTurnos();

        if (role === 'docente') {
          // Si el docente completa una limpieza con turno, solo puede escoger entre sus turnos.
          return data.filter((t) => t.docenteId === docenteId);
        }

        return data;
      },
    },
    docentes: {
      fetcher: getDocentes,
    },
    zonas: {
      fetcher: getZonas,
    },
  };

  const entityData = role === 'docente'
    ? { docenteId, completada: false, ...limpieza }
    : limpieza;

  const preparePayload = (payload) => {
    if (role === 'docente') {
      return {
        ...payload,
        // El docente nunca decide a quién pertenece la limpieza; eso llega fijado por sesión.
        docenteId,
      };
    }

    return payload;
  };

  return (
    <EntityForm
      entity={entityData}
      title={limpieza ? 'Editar limpieza' : 'Nueva limpieza'}
      description="Asignación o registro de limpieza por zona, con o sin turno."
      fields={fields}
      loaders={loaders}
      createAction={(data) => createLimpieza(preparePayload(data))}
      updateAction={(id, data) => updateLimpieza(id, preparePayload(data))}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
