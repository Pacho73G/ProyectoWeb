/* Archivo documentado: Pantalla principal de la SPA. Consume la API y presenta una vista funcional del módulo correspondiente. */
import { CrudPage } from './CrudPage';
import { getCheckpoints, deleteCheckpoint } from '../api/checkpoint.api';
import { CheckpointForm } from './forms/CheckpointForm';

export function CheckpointsPage() {
  const columns = [
    { key: 'zonaNombre', label: 'Zona' },
    { key: 'recorridoId', label: 'Recorrido' },
    { key: 'codigoQR', label: 'Código QR' },
    { key: 'orden', label: 'Orden' },
  ];

  return (
    <CrudPage
      title="Checkpoints de recorrido"
      toolbarNote="Puntos de recorrido validados por QR."
      createLabel="Nuevo checkpoint"
      fetchFn={getCheckpoints}
      deleteFn={deleteCheckpoint}
      columns={columns}
      FormComponent={CheckpointForm}
      formPropName="checkpoint"
      deleteMessage="¿Eliminar este checkpoint? Esta acción no se puede deshacer."
      resource="checkpoints"
    />
  );
}
