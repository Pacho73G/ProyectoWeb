/* Archivo documentado: Pantalla principal de la SPA. Consume la API y presenta una vista funcional del módulo correspondiente. */
import { Badge } from '../components/Badge';
import { CrudPage } from './CrudPage';
import { getTurnos, deleteTurno } from '../api/turno.api';
import { TurnoForm } from './forms/TurnoForm';
import { getRole } from '../roleConfig';

export function TurnosPage() {
  const role = getRole();
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
    { key: 'estado', label: 'Estado', render: (row) => <Badge value={row.estado} /> },
  ];

  return (
    <CrudPage
      title="Turnos"
      subtitle={
        role === 'administrador'
          ? 'Crea franjas personalizadas, asigna docente y zona, y deja visible el estado operativo de cada cobertura.'
          : 'Consulta la franja, la zona asignada y el estado actual de cada turno en una sola vista.'
      }
      introTitle={role === 'administrador' ? 'Programación de turnos' : 'Calendario operativo de turnos'}
      toolbarNote={
        role === 'administrador'
          ? 'Asignación y carga de turnos. Puedes crear nuevas franjas personalizadas.'
          : 'Calendario de turnos asignados y franjas programadas.'
      }
      createLabel="Nuevo turno"
      fetchFn={getTurnos}
      deleteFn={deleteTurno}
      columns={columns}
      FormComponent={TurnoForm}
      formPropName="turno"
      deleteMessage="¿Eliminar este turno? Esta acción no se puede deshacer."
      resource="turnos"
    />
  );
}
