/* Archivo documentado: Formulario modal de la SPA. Orquesta campos, carga de opciones y acciones de creación o edición para una entidad concreta. */
import { EntityForm } from './EntityForm';
import { createCheckIn, updateCheckIn } from '../../api/checkin.api';
import { getTurnos } from '../../api/turno.api';
import { getDocentes } from '../../api/usuario.api';
import { getZonas } from '../../api/zona.api';
import { checkinMetodos } from './options';

export function CheckInForm({ checkin, onSave, onCancel }) {
  const fields = [
    { name: 'turnoId', label: 'Turno', type: 'select', required: true, loader: 'turnos', optionLabel: (item) => `${item.franja} · ${item.fecha}` },
    { name: 'docenteId', label: 'Docente', type: 'select', required: true, loader: 'docentes', optionLabel: (item) => item.nombre },
    { name: 'zonaId', label: 'Zona', type: 'select', required: true, loader: 'zonas', optionLabel: (item) => item.nombre },
    { name: 'metodo', label: 'Método', type: 'select', required: true, options: checkinMetodos, help: 'Usa QR o PIN según el acceso disponible en la zona.' },
    { name: 'evidencia', label: 'Evidencia', type: 'text', required: true, full: true },
    { name: 'timestamp', label: 'Fecha y hora', type: 'datetime-local', required: true },
    { name: 'valido', label: 'Check-in válido', type: 'checkbox', full: true, defaultValue: true },
  ];

  return (
    <EntityForm
      entity={checkin}
      title={checkin ? 'Editar check-in' : 'Nuevo check-in'}
      description="Valida el ingreso por turno mediante QR, PIN o NFC."
      fields={fields}
      loaders={{
        turnos: { fetcher: getTurnos },
        docentes: { fetcher: getDocentes },
        zonas: { fetcher: getZonas },
      }}
      createAction={createCheckIn}
      updateAction={updateCheckIn}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
