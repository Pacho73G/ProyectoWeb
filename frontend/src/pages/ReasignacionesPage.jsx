/* Archivo documentado: Pantalla principal de la SPA. Consume la API y presenta una vista funcional del módulo correspondiente. */
import { Badge } from '../components/Badge';
import { CrudPage } from './CrudPage';
import { getReasignaciones, deleteReasignacion } from '../api/reasignacion.api';
import { ReasignacionForm } from './forms/ReasignacionForm';
import { getRole } from '../roleConfig';

export function ReasignacionesPage() {
  const role = getRole();
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
          <small>{row.docenteReemplazoNombre ? 'Candidato asignado' : 'Esperando respuesta'}</small>
        </div>
      ),
    },
    { key: 'estado', label: 'Estado', render: (row) => <Badge value={row.estado} /> },
  ];

  return (
    <CrudPage
      title="Reasignaciones"
      subtitle={
        role === 'coordinador'
          ? 'Visualiza quién pidió apoyo, el reemplazo propuesto y el estado de respuesta del turno.'
          : 'Consulta tus solicitudes y el avance de cada propuesta de reemplazo.'
      }
      introTitle={role === 'coordinador' ? 'Gestión de cobertura y reemplazos' : 'Solicitudes de reasignación'}
      toolbarNote={role === 'coordinador' ? 'Gestión operativa de reasignaciones.' : 'Solicitudes de reasignación por impedimento.'}
      createLabel="Nueva reasignación"
      fetchFn={getReasignaciones}
      deleteFn={deleteReasignacion}
      columns={columns}
      FormComponent={ReasignacionForm}
      formPropName="reasignacion"
      deleteMessage="¿Eliminar esta reasignación? Esta acción no se puede deshacer."
      resource="reasignaciones"
    />
  );
}
