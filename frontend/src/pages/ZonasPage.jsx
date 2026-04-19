import { CrudPage } from './CrudPage';
import { getZonas, deleteZona } from '../api/zona.api';
import { ZonaForm } from './forms/ZonaForm';
import { getRole } from '../roleConfig';

export function ZonasPage() {
  const role = getRole();
  const columns = [
    { key: 'nombre', label: 'Nombre' },
    { key: 'ubicacion', label: 'Ubicación' },
    { key: 'capacidadMaxima', label: 'Capacidad' },
    { key: 'activa', label: 'Activa', render: (row) => String(row.activa) },
  ];

  return (
    <CrudPage
      title="Zonas"
      toolbarNote={role === 'administrador' ? 'Gestión de zonas del sistema.' : 'Cobertura y consulta de zonas.'}
      createLabel="Nueva zona"
      fetchFn={getZonas}
      deleteFn={deleteZona}
      columns={columns}
      FormComponent={ZonaForm}
      formPropName="zona"
      deleteMessage="¿Eliminar esta zona? Esta acción no se puede deshacer."
      resource="zonas"
    />
  );
}
