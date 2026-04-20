/* Archivo documentado: Formulario modal de la SPA. Orquesta campos, carga de opciones y acciones de creación o edición para una entidad concreta. */
import { EntityForm } from './EntityForm';
import { createCheckpoint, updateCheckpoint } from '../../api/checkpoint.api';
import { getZonas } from '../../api/zona.api';
import { getRecorridos } from '../../api/recorrido.api';

export function CheckpointForm({ checkpoint, onSave, onCancel }) {
  const fields = [
    { name: 'zonaId', label: 'Zona', type: 'select', required: true, loader: 'zonas', optionLabel: (item) => item.nombre },
    { name: 'recorridoId', label: 'Recorrido', type: 'select', required: true, loader: 'recorridos', optionLabel: (item) => `${item.id} · ${item.docenteNombre}` },
    { name: 'codigoQR', label: 'Código QR', type: 'text', required: true },
    { name: 'descripcion', label: 'Descripción', type: 'textarea', required: true, full: true },
    { name: 'orden', label: 'Orden', type: 'number', required: true, min: 0 },
    { name: 'escaneadoEn', label: 'Escaneado en', type: 'datetime-local', required: true },
  ];

  return (
    <EntityForm
      entity={checkpoint}
      title={checkpoint ? 'Editar checkpoint' : 'Nuevo checkpoint'}
      description="Registra puntos de recorrido validados por QR."
      fields={fields}
      loaders={{
        zonas: { fetcher: getZonas },
        recorridos: { fetcher: getRecorridos },
      }}
      createAction={createCheckpoint}
      updateAction={updateCheckpoint}
      onSave={onSave}
      onCancel={onCancel}
    />
  );
}
