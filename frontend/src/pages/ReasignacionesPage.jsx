
import { useCallback } from 'react';
import { Badge } from '../components/Badge';
import { CrudPage } from './CrudPage';
import {
  getReasignaciones,
  deleteReasignacion,
  updateReasignacion,
} from '../api/reasignacion.api';
import { ReasignacionForm } from './forms/ReasignacionForm';
import {
  getRole,
  getDocenteId,
  getUserId,
  getUserNombre,
} from '../roleConfig';
import { pushNotification } from '../api/notificacion.api';

export function ReasignacionesPage() {
  const role = getRole();
  const docenteId = getDocenteId();
  const userId = getUserId();
  const userNombre = getUserNombre();

  const columns = [
    {
      key: 'turnoFranja',
      label: 'Turno',
      render: (row) => (
        <div className="table-main">
          <strong>{row.turnoFranja}</strong>
          <small>{row.zonaNombre}</small>
        </div>
      ),
    },
    {
      key: 'docenteSolicitanteNombre',
      label: 'Solicitante',
      render: (row) => (
        <div className="table-main">
          <strong>{row.docenteSolicitanteNombre}</strong>
          <small>{row.propuestaEn}</small>
        </div>
      ),
    },
    {
      key: 'docenteReemplazoNombre',
      label: 'Reemplazo',
      render: (row) => (
        <div className="table-main">
          <strong>{row.docenteReemplazoNombre || 'Pendiente'}</strong>
          <small>
            {row.docenteReemplazoNombre
              ? 'Candidato asignado'
              : 'Esperando respuesta'}
          </small>
        </div>
      ),
    },
    {
      key: 'estado',
      label: 'Estado',
      render: (row) => <Badge value={row.estado} />,
    },
  ];

  const filterFn = useCallback(
    (data) =>
      docenteId
        ? data.filter(
            (r) =>
              Number(r.docenteSolicitanteId) === Number(docenteId) ||
              Number(r.docenteReemplazoId) === Number(docenteId)
          )
        : data,
    [docenteId]
  );

  const responder = async (row, estado, reload) => {
    const now = new Date().toISOString().slice(0, 16);

    await updateReasignacion(row.id, {
      ...row,
      estado,
      respondidaEn: now,
    });

    pushNotification({
      title: 'Reasignación respondida',
      message: `${userNombre} ${estado === 'ACEPTADA' ? 'aceptó' : 'rechazó'} la solicitud`,
      role: 'docente',
      userId: row.docenteSolicitanteId,
    });

    reload();
  };

  const extraRowActions =
    role === 'docente'
      ? (row, reload) =>
          Number(row.docenteReemplazoId) === Number(userId) &&
          row.estado === 'PENDIENTE' ? (
            <div style={{ display: 'flex', gap: '8px' }}>
              <button
                className="action-button"
                onClick={() => responder(row, 'ACEPTADA', reload)}
              >
                Aceptar
              </button>

              <button
                className="action-button danger"
                onClick={() => responder(row, 'RECHAZADA', reload)}
              >
                Rechazar
              </button>
            </div>
          ) : null
      : undefined;

  return (
    <CrudPage
      title="Reasignaciones"
      subtitle={
        role === 'coordinador'
          ? 'Visualiza quién pidió apoyo, el reemplazo propuesto y el estado de respuesta del turno.'
          : 'Consulta tus solicitudes y el avance de cada propuesta de reemplazo.'
      }
      introTitle={
        role === 'coordinador'
          ? 'Gestión de cobertura y reemplazos'
          : 'Solicitudes de reasignación'
      }
      toolbarNote={
        role === 'coordinador'
          ? 'Gestión operativa de reasignaciones.'
          : 'Solicitudes de reasignación por impedimento.'
      }
      createLabel="Nueva reasignación"
      fetchFn={getReasignaciones}
      deleteFn={deleteReasignacion}
      filterFn={role === 'docente' ? filterFn : undefined}
      columns={columns}
      FormComponent={ReasignacionForm}
      formPropName="reasignacion"
      deleteMessage="¿Eliminar esta reasignación? Esta acción no se puede deshacer."
      resource="reasignaciones"
      extraRowActions={extraRowActions}
    />
  );
}