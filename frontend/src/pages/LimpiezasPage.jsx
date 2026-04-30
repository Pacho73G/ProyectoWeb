
import { useCallback } from 'react';
import { Badge } from '../components/Badge';
import { CrudPage } from './CrudPage';
import { getLimpiezas, deleteLimpieza } from '../api/limpieza.api';
import { LimpiezaForm } from './forms/LimpiezaForm';
import { getRole, getDocenteId } from '../roleConfig';

export function LimpiezasPage() {
  const role = getRole();
  const docenteId = getDocenteId();

  const columns = [
    {
      key: 'docenteNombre',
      label: 'Docente',
      render: (row) => (
        <div className="table-main">
          <strong>{row.docenteNombre}</strong>
          <small>{row.zonaNombre}</small>
        </div>
      ),
    },
    {
      key: 'turnoFranja',
      label: 'Contexto',
      render: (row) => row.turnoFranja || 'Sin turno',
    },
    {
      key: 'escala',
      label: 'Escala de limpieza',
      render: (row) => {
        const labels = {
          1: '1 – Limpio',
          2: '2 – Algo de basura',
          3: '3 – Mucha basura',
          4: '4 – Crítico',
        };

        const colors = {
          1: 'success',
          2: 'info',
          3: 'warning',
          4: 'danger',
        };

        return (
          <span className={`badge ${colors[row.escala] ?? 'info'}`}>
            {labels[row.escala] ?? row.escala}
          </span>
        );
      },
    },
    {
      key: 'completada',
      label: 'Estado',
      render: (row) => <Badge value={row.completada ? 'COMPLETADA' : 'PENDIENTE'} />,
    },
    { key: 'observaciones', label: 'Observaciones' },
    { key: 'asignadaEn', label: 'Asignada en' },
    { key: 'registradoEn', label: 'Registrado en' },
  ];

  const fetchDocente = useCallback(async () => {
    const limpiezas = await getLimpiezas();
    // El docente solo trabaja sobre tareas asignadas a su propio usuario.
    return limpiezas.filter((l) => Number(l.docenteId) === Number(docenteId));
  }, [docenteId]);

  return (
    <CrudPage
      title="Registros de limpieza"
      subtitle={
        role === 'docente'
          ? 'Consulta y completa las limpiezas que te asignaron.'
          : 'Asigna limpiezas por zona, con o sin turno.'
      }
      toolbarNote={
        role === 'docente'
          ? 'Solo verás limpiezas asignadas a tu usuario.'
          : 'Puedes asignar limpiezas independientes del turno.'
      }
      createLabel="Nuevo registro"
      fetchFn={role === 'docente' ? fetchDocente : getLimpiezas}
      deleteFn={deleteLimpieza}
      columns={columns}
      FormComponent={LimpiezaForm}
      formPropName="limpieza"
      deleteMessage="¿Eliminar este registro?"
      resource="limpiezas"
      editLabel={role === 'docente' ? 'Completar' : 'Editar'}
      // Una limpieza ya completada deja de ser editable para docente.
      canEditRow={(row) => role !== 'docente' || !row.completada}
    />
  );
}
