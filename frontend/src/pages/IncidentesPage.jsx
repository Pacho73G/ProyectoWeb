import { Badge } from '../components/Badge';
import { CrudPage } from './CrudPage';
import { getIncidentes, deleteIncidente } from '../api/incidente.api';
import { IncidenteForm } from './forms/IncidenteForm';
import { getRole } from '../roleConfig';

export function IncidentesPage() {
  const role = getRole();
  const columns = [
    {
      key: 'tipo',
      label: 'Tipo',
      render: (row) => (
        <div className="table-main">
          <strong>{row.tipo}</strong>
          <small>{row.descripcion}</small>
        </div>
      ),
    },
    { key: 'severidad', label: 'Severidad', render: (row) => <Badge value={row.severidad} /> },
    {
      key: 'docenteNombre',
      label: 'Docente',
      render: (row) => (
        <div className="table-main">
          <strong>{row.docenteNombre}</strong>
          <small>{row.registradoEn}</small>
        </div>
      ),
    },
    {
      key: 'zonaNombre',
      label: 'Zona',
      render: (row) => (
        <div className="table-main">
          <strong>{row.zonaNombre}</strong>
          <small>{row.requiereSeguimiento ? 'Requiere seguimiento' : 'Sin seguimiento'}</small>
        </div>
      ),
    },
  ];

  return (
    <CrudPage
      title="Incidentes"
      subtitle={
        role === 'docente'
          ? 'Documenta lo ocurrido con severidad, zona y contexto en una sola vista.'
          : 'Consulta eventos reportados y detecta zonas o situaciones que requieren seguimiento.'
      }
      introTitle={role === 'docente' ? 'Registro rápido de incidentes' : 'Historial operativo de incidentes'}
      toolbarNote={role === 'docente' ? 'Registro ágil de situaciones durante el turno.' : 'Historial de incidentes reportados.'}
      createLabel="Nuevo incidente"
      fetchFn={getIncidentes}
      deleteFn={deleteIncidente}
      columns={columns}
      FormComponent={IncidenteForm}
      formPropName="incidente"
      deleteMessage="¿Eliminar este incidente? Esta acción no se puede deshacer."
      resource="incidentes"
    />
  );
}
