/* Archivo documentado: Componente reutilizable de la interfaz. Aísla una pieza visual o de interacción compartida por varias pantallas. */
export function Modal({ mensaje, onConfirm, onCancel }) {
  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <p>{mensaje}</p>
        <div className="modal-actions">
          <button className="secondary-button" onClick={onCancel}>
            Cancelar
          </button>
          <button className="danger-button" onClick={onConfirm}>
            Eliminar
          </button>
        </div>
      </div>
    </div>
  );
}
