/* Archivo documentado: Pantalla de registros de limpieza. Para el rol docente filtra únicamente los registros de sus turnos. */
import { useCallback } from 'react';
import { CrudPage } from './CrudPage';
import { getLimpiezas, deleteLimpieza } from '../api/limpieza.api';
import { getTurnos } from '../api/turno.api';
import { LimpiezaForm } from './forms/LimpiezaForm';
import { getRole, getDocenteId } from '../roleConfig';

export function LimpiezasPage() {
  const role = getRole();
  const docenteId = getDocenteId();

  const columns = [
    { key: 'turnoFranja', label: 'Turno' },
    {
      key: 'escala',
      label: 'Escala de limpieza',
      render: (row) => {
        const labels = { 1: '1 – Limpio', 2: '2 – Algo de basura', 3: '3 – Mucha basura', 4: '4 – Crítico' };
        const colors = { 1: 'success', 2: 'info', 3: 'warning', 4: 'danger' };
        return <span className={`badge ${colors[row.escala] ?? 'info'}`}>{labels[row.escala] ?? row.escala}</span>;
      },
    },
    { key: 'observaciones', label: 'Observaciones' },
    { key: 'registradoEn', label: 'Registrado en' },
  ];

  // For docente: first load their turno IDs, then filter limpiezas
  const fetchFn = useCallback(async () => {
    const [limpiezas, turnos] = await Promise.all([getLimpiezas(), getTurnos()]);
    if (!docenteId) return limpiezas;
    const misTurnoIds = new Set(
      turnos.filter((t) => t.docenteId === docenteId).map((t) => t.id),
    );
    return limpiezas.filter((l) => misTurnoIds.has(l.turnoId));
  }, [docenteId]);

  return (
    <CrudPage
      title="Registros de limpieza"
      subtitle={
        role === 'docente'
          ? 'Registra el estado de limpieza al cerrar tu turno.'
          : 'Cierre de turnos con estado de limpieza.'
      }
      toolbarNote={
        role === 'docente'
          ? 'Cierre de turno con escala de limpieza (1=Limpio, 4=Crítico).'
          : 'Cierre de turnos con limpieza.'
      }
      createLabel="Nuevo registro"
      fetchFn={role === 'docente' ? fetchFn : getLimpiezas}
      deleteFn={deleteLimpieza}
      columns={columns}
      FormComponent={LimpiezaForm}
      formPropName="limpieza"
      deleteMessage="¿Eliminar este registro de limpieza? Esta acción no se puede deshacer."
      resource="limpiezas"
    />
  );
}