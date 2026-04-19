import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useApi } from '../hooks/useApi';
import { Tabla } from '../components/Tabla';
import { Modal } from '../components/Modal';
import { Spinner } from '../components/Spinner';
import { allows, getRole, showsActions, ROLE_VIEW_LABELS } from '../roleConfig';

export function CrudPage({
  title,
  subtitle,
  introTitle,
  toolbarNote,
  createLabel,
  fetchFn,
  deleteFn,
  columns,
  FormComponent,
  formPropName,
  deleteMessage,
  resource,
}) {
  const role = getRole();
  const canCreate = allows(role, resource, 'create');
  const canEdit = allows(role, resource, 'edit');
  const canDelete = allows(role, resource, 'delete');
  const showRowActions = showsActions(role, resource);

  const { data, loading, error, reload } = useApi(fetchFn);
  const [eliminando, setEliminando] = useState(null);
  const [editando, setEditando] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [actionError, setActionError] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);

  const handleEliminar = async () => {
    if (!eliminando) return;
    try {
      await deleteFn(eliminando);
      setEliminando(null);
      setActionError(null);
      setSuccessMessage('Registro eliminado correctamente.');
      reload();
    } catch (e) {
      setActionError(e.message);
      setEliminando(null);
    }
  };

  const formProps = {
    [formPropName]: editando,
    onSave: () => {
      setShowForm(false);
      setEditando(null);
      setSuccessMessage(editando ? 'Registro actualizado correctamente.' : 'Registro creado correctamente.');
      reload();
    },
    onCancel: () => {
      setShowForm(false);
      setEditando(null);
    },
  };

  return (
    <div>
      <div className="topbar">
        <div>
          <p className="eyebrow">Colegio San José · Supervisión escolar</p>
          <h1>{title}</h1>
          <p className="muted role-label">{ROLE_VIEW_LABELS[role]}</p>
        </div>
        <div className="topbar-actions">
          <Link className="ghost-button" to="/">Volver al acceso</Link>
          {canCreate && !toolbarNote && (
            <button
              className="primary-button"
              onClick={() => {
                setEditando(null);
                setShowForm(true);
              }}
            >
              {createLabel}
            </button>
          )}
        </div>
      </div>

      {(error || actionError) && <div className="alert error">{error ?? actionError}</div>}
      {successMessage && <div className="alert success">{successMessage}</div>}

      {loading ? (
        <Spinner />
      ) : (
        <div className="panel">
          {(introTitle || subtitle) && (
            <div className="panel-intro">
              {introTitle && <h3>{introTitle}</h3>}
              {subtitle && <p>{subtitle}</p>}
            </div>
          )}
          {toolbarNote && (
            <div className="table-toolbar">
              <p className="muted">{toolbarNote}</p>
              {canCreate && (
                <button
                  className="primary-button"
                  onClick={() => {
                    setEditando(null);
                    setShowForm(true);
                  }}
                >
                  {createLabel}
                </button>
              )}
            </div>
          )}
          <Tabla
            columns={columns}
            data={data}
            actions={
              showRowActions
                ? (row) => (
                    <>
                      {canEdit && (
                        <button
                          className="secondary-button"
                          onClick={() => {
                            setEditando(row);
                            setShowForm(true);
                          }}
                        >
                          Editar
                        </button>
                      )}
                      {canDelete && (
                        <button className="danger-button" onClick={() => setEliminando(row.id)}>
                          Eliminar
                        </button>
                      )}
                    </>
                  )
                : undefined
            }
          />
        </div>
      )}

      {eliminando && (
        <Modal mensaje={deleteMessage} onConfirm={handleEliminar} onCancel={() => setEliminando(null)} />
      )}

      {showForm && <FormComponent {...formProps} />}
    </div>
  );
}
