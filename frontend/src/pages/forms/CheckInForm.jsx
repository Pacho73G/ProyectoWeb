import { useMemo } from 'react';
import { EntityForm } from './EntityForm';
import { createCheckIn, updateCheckIn } from '../../api/checkin.api';
import { getTurnos } from '../../api/turno.api';
import { getDocentes } from '../../api/usuario.api';
import { getZonas } from '../../api/zona.api';
import { checkinMetodos } from './options';
import { getRole, getDocenteId } from '../../roleConfig';

export function CheckInForm({ checkin, onSave, onCancel }) {
  const role = getRole();
  const docenteId = getDocenteId();

  const fields = useMemo(() => {
    if (role === 'docente') {
      return [
        {
          name: 'turnoId',
          label: 'Turno',
          type: 'select',
          required: true,
          loader: 'turnos',
          optionLabel: (item) => `${item.franja} · ${item.fecha}`,
        },
        {
          name: 'zonaId',
          label: 'Zona',
          type: 'select',
          required: true,
          loader: 'zonas',
          optionLabel: (item) => item.nombre,
        },
        {
          name: 'metodo',
          label: 'Método',
          type: 'select',
          required: true,
          options: checkinMetodos,
        },
        {
          name: 'evidencia',
          label: 'Evidencia',
          type: 'text',
          required: true,
          full: true,
        },
        {
        name: 'timestamp',
        label: 'Fecha y hora',
        type: 'datetime-local',
        required: true,
        min: new Date().toISOString().slice(0,16)
        },
        {
          name: 'valido',
          label: 'Check-in válido',
          type: 'checkbox',
          full: true,
          defaultValue: true,
        },
      ];
    }

    return [
      {
        name: 'turnoId',
        label: 'Turno',
        type: 'select',
        required: true,
        loader: 'turnos',
        optionLabel: (item) => `${item.franja} · ${item.fecha}`,
      },
      {
        name: 'docenteId',
        label: 'Docente',
        type: 'select',
        required: true,
        loader: 'docentes',
        optionLabel: (item) => item.nombre,
      },
      {
        name: 'zonaId',
        label: 'Zona',
        type: 'select',
        required: true,
        loader: 'zonas',
        optionLabel: (item) => item.nombre,
      },
      {
        name: 'metodo',
        label: 'Método',
        type: 'select',
        required: true,
        options: checkinMetodos,
      },
      {
        name: 'evidencia',
        label: 'Evidencia',
        type: 'text',
        required: true,
        full: true,
      },
      {
        name: 'timestamp',
        label: 'Fecha y hora',
        type: 'datetime-local',
        required: true,
      },
      {
        name: 'valido',
        label: 'Check-in válido',
        type: 'checkbox',
        full: true,
        defaultValue: true,
      },
    ];
  }, [role, docenteId]);

  return (
    <EntityForm
      entity={checkin}
      title={checkin ? 'Editar check-in' : 'Nuevo check-in'}
      description="Registro manual de ingreso."
      fields={fields}
      loaders={{
        turnos: {
          fetcher: async () => {
            const data = await getTurnos();
            return role === 'docente'
              ? data.filter((t) => t.docenteId === docenteId)
              : data;
          },
        },
        docentes: {
          fetcher: async () => {
            const data = await getDocentes();
            return role === 'docente'
              ? data.filter((d) => d.id === docenteId)
              : data;
          },
        },
        zonas: { fetcher: getZonas },
      }}
      createAction={createCheckIn}
      updateAction={updateCheckIn}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}