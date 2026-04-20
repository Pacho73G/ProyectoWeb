/* Archivo documentado: Pantalla principal de la SPA. Consume la API y presenta una vista funcional del módulo correspondiente. */
import { CrudPage } from './CrudPage';
import { getConfiguraciones, deleteConfiguracion } from '../api/configuracion.api';
import { ConfiguracionForm } from './forms/ConfiguracionForm';

export function ConfiguracionesPage() {
  const columns = [
    { key: 'administradorNombre', label: 'Administrador' },
    { key: 'minutosAlertaAusencia', label: 'Alerta ausencia' },
    { key: 'segundosVentanaReasignacion', label: 'Ventana' },
    { key: 'minutosInactividad', label: 'Inactividad' },
  ];

  return (
    <CrudPage
      title="Configuración del sistema"
      toolbarNote="Parámetros globales administrados por el sistema."
      createLabel="Nueva configuración"
      fetchFn={getConfiguraciones}
      deleteFn={deleteConfiguracion}
      columns={columns}
      FormComponent={ConfiguracionForm}
      formPropName="configuracion"
      deleteMessage="¿Eliminar esta configuración? Esta acción no se puede deshacer."
      resource="configuraciones"
    />
  );
}
