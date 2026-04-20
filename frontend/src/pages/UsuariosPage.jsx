/* Archivo documentado: Pantalla principal de la SPA. Consume la API y presenta una vista funcional del módulo correspondiente. */
import { CrudPage } from './CrudPage';
import { getUsuarios, deleteUsuario } from '../api/usuario.api';
import { UsuarioForm } from './forms/UsuarioForm';

export function UsuariosPage() {
  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'nombre', label: 'Nombre' },
    { key: 'email', label: 'Email' },
    { key: 'rol', label: 'Rol' },
    { key: 'activo', label: 'Activo', render: (row) => String(row.activo) },
  ];

  return (
    <CrudPage
      title="Usuarios"
      toolbarNote="Gestión centralizada de usuarios del sistema."
      createLabel="Nuevo usuario"
      fetchFn={getUsuarios}
      deleteFn={deleteUsuario}
      columns={columns}
      FormComponent={UsuarioForm}
      formPropName="usuario"
      deleteMessage="¿Eliminar este usuario? Esta acción no se puede deshacer."
      resource="usuarios"
    />
  );
}
