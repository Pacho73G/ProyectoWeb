import { EntityForm } from './EntityForm';
import { createRecorrido, updateRecorrido } from '../../api/recorrido.api';
import { getTurnos } from '../../api/turno.api';
import { getDocentes } from '../../api/usuario.api';
import { getRole, getDocenteId, getUserNombre } from '../../roleConfig';
import { recorridoEstados } from './options';
import { pushNotification } from '../../api/notificacion.api';

export function RecorridoForm({ recorrido, onSave, onCancel }) {
  const role = getRole();
  const docenteId = getDocenteId();
  const userNombre = getUserNombre();

  const fields =
    role === 'docente'
      ? [
          {
            name: 'turnoId',
            label: 'Turno',
            type: 'select',
            required: true,
            loader: 'turnos',
            optionLabel: (item) => item.franja,
          },
          {
            name: 'iniciadoEn',
            label: 'Iniciado en',
            type: 'datetime-local',
            required: true,
          },
          {
            name: 'finalizadoEn',
            label: 'Finalizado en',
            type: 'datetime-local',
          },
          {
            name: 'estado',
            label: 'Estado',
            type: 'select',
            required: true,
            options: recorridoEstados,
          },
          {
            name: 'duracionMinutos',
            label: 'Duración',
            type: 'number',
            required: true,
            min: 0,
          },
        ]
      : [
          {
            name: 'docenteId',
            label: 'Docente',
            type: 'select',
            required: true,
            loader: 'docentes',
            optionLabel: (item) => item.nombre,
          },
          {
            name: 'turnoId',
            label: 'Turno',
            type: 'select',
            required: true,
            loader: 'turnos',
            optionLabel: (item) => item.franja,
          },
          {
            name: 'iniciadoEn',
            label: 'Iniciado en',
            type: 'datetime-local',
            required: true,
          },
          {
            name: 'finalizadoEn',
            label: 'Finalizado en',
            type: 'datetime-local',
          },
          {
            name: 'estado',
            label: 'Estado',
            type: 'select',
            required: true,
            options: recorridoEstados,
          },
          {
            name: 'duracionMinutos',
            label: 'Duración',
            type: 'number',
            required: true,
            min: 0,
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
  };

  const handleSave = () => {
    pushNotification({
      title: 'Nuevo recorrido',
      message: `${userNombre} registró un recorrido.`,
      role: 'coordinador',
    });

    pushNotification({
      title: 'Nuevo recorrido',
      message: `${userNombre} registró un recorrido.`,
      role: 'administrador',
    });

    onSave();
  };

  return (
    <EntityForm
      entity={role === 'docente' ? { docenteId, ...recorrido } : recorrido}
      title={recorrido ? 'Editar recorrido' : 'Nuevo recorrido'}
      description="Registra evidencia de vigilancia activa dentro de un turno."
      fields={fields}
      loaders={loaders}
      createAction={createRecorrido}
      updateAction={updateRecorrido}
      onSave={handleSave}
      onCancel={onCancel}
    />
  );
}