
import { useCallback } from 'react';
import { Badge } from '../components/Badge';
import { CrudPage } from './CrudPage';
import { getTurnos, deleteTurno, updateTurno } from '../api/turno.api';
import { createCheckIn } from '../api/checkin.api';
import { TurnoForm } from './forms/TurnoForm';
import { getRole, getDocenteId } from '../roleConfig';

export function TurnosPage() {
  const role = getRole();
  const docenteId = getDocenteId();

  /* ======================
     COLUMNAS
  ====================== */

  const columns = [
    {
      key: 'docenteNombre',
      label: 'Docente',
      render: (row) => (
        <div className="table-main">
          <strong>{row.docenteNombre}</strong>
          <small>{row.docenteEmail}</small>
        </div>
      ),
    },
    {
      key: 'zonaNombre',
      label: 'Zona',
      render: (row) => (
        <div className="table-main">
          <strong>{row.zonaNombre}</strong>
          <small>{row.zonaUbicacion}</small>
        </div>
      ),
    },
    { key: 'fecha', label: 'Fecha' },
    {
      key: 'franja',
      label: 'Franja',
      render: (row) => (
        <div className="table-main">
          <strong>{row.franja}</strong>
          <small>
            {row.horaInicio} - {row.horaFin}
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

  /* ======================
     FILTRO DOCENTE
  ====================== */

  const filterFn = useCallback(
    (data) =>
      docenteId ? data.filter((t) => t.docenteId === docenteId) : data,
    [docenteId]
  );

  /* ======================
     CHECK-IN
  ====================== */

  const handleCheckIn = useCallback(async (turno, reload) => {
    const now = new Date().toISOString().slice(0, 16);

    try {
      await createCheckIn({
        turnoId: turno.id,
        docenteId: turno.docenteId,
        zonaId: turno.zonaId,
        timestamp: now,
        metodo: 'PIN',
        evidencia: 'CHECK_IN_DOCENTE',
        valido: true,
      });

      await updateTurno(turno.id, {
        docenteId: turno.docenteId,
        zonaId: turno.zonaId,
        fecha: turno.fecha,
        horaInicio: turno.horaInicio,
        horaFin: turno.horaFin,
        franja: turno.franja,
        estado: 'EN_CURSO',
        abiertoEn: now,
        cerradoEn: null,
      });

      reload();
    } catch (e) {
      alert('Error al registrar check-in: ' + e.message);
    }
  }, []);

  /* ======================
     CHECK-OUT CORREGIDO
  ====================== */

  const handleCheckOut = useCallback(async (turno, reload) => {
    const now = new Date().toISOString().slice(0, 16);

    try {
      // IMPORTANTE:
      // Ya no se crea otro check-in.
      // Solo se cierra el turno.

      await updateTurno(turno.id, {
        docenteId: turno.docenteId,
        zonaId: turno.zonaId,
        fecha: turno.fecha,
        horaInicio: turno.horaInicio,
        horaFin: turno.horaFin,
        franja: turno.franja,
        estado: 'CERRADO',
        abiertoEn: turno.abiertoEn || null,
        cerradoEn: now,
      });

      reload();
    } catch (e) {
      alert('Error al finalizar turno: ' + e.message);
    }
  }, []);

  /* ======================
     ACCIONES EXTRA
  ====================== */

  const extraRowActions =
    role === 'docente'
      ? (row, reload) => (
          <div style={{ display: 'flex', gap: '8px' }}>
            {row.estado === 'PENDIENTE' && (
              <button
                className="action-button"
                onClick={() => handleCheckIn(row, reload)}
              >
                Iniciar turno
              </button>
            )}

            {row.estado === 'EN_CURSO' && (
              <button
                className="action-button danger"
                onClick={() => handleCheckOut(row, reload)}
              >
                Finalizar turno
              </button>
            )}
          </div>
        )
      : undefined;

  /* ======================
     RENDER
  ====================== */

  return (
    <CrudPage
      title="Turnos"
      subtitle={
        role === 'administrador'
          ? 'Crea franjas personalizadas, asigna docente y zona, y deja visible el estado operativo.'
          : role === 'docente'
          ? 'Tus turnos asignados. Inicia y finaliza desde aquí.'
          : 'Consulta la franja, zona y estado de cada turno.'
      }
      introTitle={
        role === 'administrador'
          ? 'Programación de turnos'
          : role === 'docente'
          ? 'Mis turnos'
          : 'Calendario operativo'
      }
      toolbarNote={
        role === 'administrador'
          ? 'Asignación de turnos y cobertura.'
          : role === 'docente'
          ? 'Usa los botones para iniciar o finalizar tu turno.'
          : 'Consulta de turnos.'
      }
      createLabel="Nuevo turno"
      fetchFn={getTurnos}
      deleteFn={deleteTurno}
      filterFn={role === 'docente' ? filterFn : undefined}
      columns={columns}
      FormComponent={TurnoForm}
      formPropName="turno"
      deleteMessage="¿Eliminar este turno? Esta acción no se puede deshacer."
      resource="turnos"
      extraRowActions={extraRowActions}
    />
  );
}