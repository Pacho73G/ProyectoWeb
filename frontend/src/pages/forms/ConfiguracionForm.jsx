/* Archivo documentado: Formulario modal de la SPA. Orquesta campos, carga de opciones y acciones de creación o edición para una entidad concreta. */
import { EntityForm } from './EntityForm';
import { createConfiguracion, updateConfiguracion } from '../../api/configuracion.api';
import { getUsuarios } from '../../api/usuario.api';

const getAdministradores = async () => {
  const usuarios = await getUsuarios();
  return usuarios.filter((item) => item.rol === 'ADMINISTRADOR');
};

export function ConfiguracionForm({ configuracion, onSave, onCancel }) {
  const fields = [
    { name: 'administradorId', label: 'Administrador', type: 'select', required: true, loader: 'administradores', optionLabel: (item) => item.nombre, full: true },
    { name: 'minutosAlertaAusencia', label: 'Minutos alerta ausencia', type: 'number', required: true, min: 0 },
    { name: 'segundosVentanaReasignacion', label: 'Segundos ventana reasignación', type: 'number', required: true, min: 0 },
    { name: 'minutosInactividad', label: 'Minutos inactividad', type: 'number', required: true, min: 0 },
    { name: 'umbralIngreso', label: 'Umbral ingreso', type: 'number', required: true, min: 0 },
    { name: 'minutosRecordatorio1', label: 'Recordatorio 1', type: 'number', required: true, min: 0 },
    { name: 'minutosRecordatorio2', label: 'Recordatorio 2', type: 'number', required: true, min: 0 },
  ];

  return (
    <EntityForm
      entity={configuracion}
      title={configuracion ? 'Editar configuración' : 'Nueva configuración'}
      description="Define parámetros operativos globales para alertas, inactividad y recordatorios."
      fields={fields}
      loaders={{ administradores: { fetcher: getAdministradores } }}
      createAction={createConfiguracion}
      updateAction={updateConfiguracion}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
