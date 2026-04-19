import { EntityForm } from './EntityForm';
import { createZona, updateZona } from '../../api/zona.api';

export function ZonaForm({ zona, onSave, onCancel }) {
  const fields = [
    { name: 'nombre', label: 'Nombre', type: 'text', required: true },
    { name: 'descripcion', label: 'Descripción', type: 'text', required: true },
    { name: 'ubicacion', label: 'Ubicación', type: 'text', required: true },
    { name: 'capacidadMaxima', label: 'Capacidad máxima', type: 'number', required: true, min: 0 },
    { name: 'activa', label: 'Zona activa', type: 'checkbox', defaultValue: true, full: true },
  ];

  return (
    <EntityForm
      entity={zona}
      title={zona ? 'Editar zona' : 'Nueva zona'}
      description="Registra espacios del colegio con su ubicación, capacidad y estado operativo."
      fields={fields}
      createAction={createZona}
      updateAction={updateZona}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
