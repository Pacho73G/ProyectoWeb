import { CrudPage } from './CrudPage';
import { getLimpiezas, deleteLimpieza } from '../api/limpieza.api';
import { LimpiezaForm } from './forms/LimpiezaForm';

export function LimpiezasPage() {
  const columns = [
    { key: 'turnoFranja', label: 'Turno' },
    { key: 'escala', label: 'Escala' },
    { key: 'registradoEn', label: 'Registrado en' },
  ];

  return (
    <CrudPage
      title="Registros de limpieza"
      toolbarNote="Cierre de turnos con limpieza."
      createLabel="Nuevo registro"
      fetchFn={getLimpiezas}
      deleteFn={deleteLimpieza}
      columns={columns}
      FormComponent={LimpiezaForm}
      formPropName="limpieza"
      deleteMessage="¿Eliminar este registro de limpieza? Esta acción no se puede deshacer."
      resource="limpiezas"
    />
  );
}
