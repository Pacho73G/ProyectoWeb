import { useCallback } from 'react';
import { Badge } from '../components/Badge';
import { CrudPage } from './CrudPage';
import { getTurnos, getTurnosHoy, deleteTurno, updateTurno } from '../api/turno.api';
import { TurnoForm } from './forms/TurnoForm';
import { getRole, getDocenteId } from '../roleConfig';

export function TurnosPage() {
  const role = getRole();
  const docenteId = getDocenteId();

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

  // El docente usa GET /turnos/hoy y filtra por su ID para ver solo sus turnos de hoy.
  // El admin y coordinador usan GET /turnos para ver toda la programación.
  const fetchFn = role === 'docente' ? getTurnosHoy : getTurnos;

  const filterFn = useCallback(
      (data) => {
        if (!docenteId) return data;
        const now = new Date();
        return data.filter((t) => {
          if (t.docenteId !== docenteId) return false;
          // Ocultar si aún no llegó la hora de apertura definida por el admin.
          if (t.abiertoEn && new Date(t.abiertoEn) > now) return false;
          // Ocultar si ya pasó la hora de cierre o el turno ya está cerrado.
          if (t.estado === 'CERRADO') return false;
          if (t.cerradoEn && new Date(t.cerradoEn) <= now) return false;
          return true;
        });
      },
      [docenteId]
  );

  /* ======================
     INICIAR TURNO (PENDIENTE → EN_CURSO)
     Solo cambia el estado; el check-in se registra por separado desde el menú Check-ins.
  ====================== */

  const handleIniciarTurno = useCallback(async (turno, reload) => {
    try {
      await updateTurno(turno.id, {
        docenteId: turno.docenteId,
        zonaId: turno.zonaId,
        fecha: turno.fecha,
        horaInicio: turno.horaInicio,
        horaFin: turno.horaFin,
        franja: turno.franja,
        estado: 'EN_CURSO',
        // Se preservan las fechas que puso el admin; no se sobreescriben al iniciar.
        abiertoEn: turno.abiertoEn ?? null,
        cerradoEn: turno.cerradoEn ?? null,
      });
      reload();
    } catch (e) {
      alert('Error al iniciar turno: ' + e.message);
    }
  }, []);

  /* ======================
     ACCIONES EXTRA
  ====================== */

  const extraRowActions =
    role === 'docente'
      ? (row, reload) => (
          <div style={{ display: 'flex', gap: '8px' }}>
            {(row.estado === 'PENDIENTE' || row.estado === 'SIN_COBERTURA') && (
              <button
                className="action-button"
                onClick={() => handleIniciarTurno(row, reload)}
              >
                Iniciar turno
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
          ? 'Tus turnos asignados para hoy. Inicia y finaliza desde aquí.'
          : 'Consulta la franja, zona y estado de cada turno.'
      }
      introTitle={
        role === 'administrador'
          ? 'Programación de turnos'
          : role === 'docente'
          ? 'Mis turnos de hoy'
          : 'Calendario operativo'
      }
      toolbarNote={
        role === 'administrador'
          ? 'Asignación de turnos y cobertura.'
          : role === 'docente'
          ? 'Usa los botones para iniciar tu turno. Registra el check-in desde el menú Check-ins.'
          : 'Consulta de turnos.'
      }
      createLabel="Nuevo turno"
      fetchFn={fetchFn}
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