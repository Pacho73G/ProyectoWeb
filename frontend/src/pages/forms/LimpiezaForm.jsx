import { EntityForm } from './EntityForm';
import { createLimpieza, updateLimpieza } from '../../api/limpieza.api';
import { getTurnos } from '../../api/turno.api';
import { limpiezaEscalas } from './options';
import {
  getRole,
  getDocenteId,
  getUserNombre,
} from '../../roleConfig';
import { pushNotification } from '../../api/notificacion.api';

export function LimpiezaForm({ limpieza, onSave, onCancel }) {
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
      optionLabel: (item) => `${item.franja} · ${item.fecha}`,
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
  ];

  const loaders = {
    turnos: {
      fetcher: async () => {
        const data = await getTurnos();

        if (role === 'docente') {
          return data.filter((t) => t.docenteId === docenteId);
        }

        return data;
      },
    },
  };

  const handleSave = () => {
    pushNotification({
      title: 'Nuevo registro de limpieza',
      message: `${userNombre} registró una limpieza.`,
      role: 'coordinador',
    });

    pushNotification({
      title: 'Nuevo registro de limpieza',
      message: `${userNombre} registró una limpieza.`,
      role: 'administrador',
    });

    onSave();
  };

  return (
    <EntityForm
      entity={limpieza}
      title={limpieza ? 'Editar limpieza' : 'Nueva limpieza'}
      description="Registro de limpieza."
      fields={fields}
      loaders={loaders}
      createAction={createLimpieza}
      updateAction={updateLimpieza}
      onSave={handleSave}
      onCancel={onCancel}
    />
  );
}