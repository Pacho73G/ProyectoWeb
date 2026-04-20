/* Archivo documentado: Formulario modal de la SPA. Orquesta campos, carga de opciones y acciones de creación o edición para una entidad concreta. */
import { useMemo, useState } from 'react';
import { FormField } from '../../components/FormField';
import { createUsuario, updateUsuario } from '../../api/usuario.api';
import { rolesUsuario } from './options';

const descriptorLabels = {
  DOCENTE: 'Materias',
  COORDINADOR: 'Nivel',
  ADMINISTRADOR: 'Cargo',
};

function buildValues(usuario) {
  return {
    nombre: usuario?.nombre ?? '',
    email: usuario?.email ?? '',
    passwordHash: '',
    activo: usuario?.activo ?? true,
    rol: usuario?.rol ?? 'DOCENTE',
    descriptor: usuario?.descriptor ?? '',
    cargaActual: usuario?.cargaActual ?? 0,
    puntajeGamificacion: usuario?.puntajeGamificacion ?? 0,
  };
}

export function UsuarioForm({ usuario, onSave, onCancel }) {
  const [values, setValues] = useState(() => buildValues(usuario));
  const [errors, setErrors] = useState({});
  const [submitError, setSubmitError] = useState(null);

  const descriptorLabel = descriptorLabels[values.rol] ?? 'Descriptor';

  const fields = useMemo(
    () => [
      { name: 'nombre', label: 'Nombre', type: 'text', required: true },
      { name: 'email', label: 'Correo', type: 'email', required: true },
      { name: 'passwordHash', label: usuario ? 'Nueva contraseña (opcional)' : 'Contraseña', type: 'password', required: !usuario },
      { name: 'activo', label: 'Usuario activo', type: 'checkbox', full: true },
      { name: 'rol', label: 'Rol', type: 'select', required: true, options: rolesUsuario, disabled: Boolean(usuario), help: usuario ? 'El rol no cambia en edición para mantener consistente la herencia del modelo.' : undefined },
      { name: 'descriptor', label: descriptorLabel, type: 'text', required: true, full: true, help: 'Docente: materias. Coordinador: nivel. Administrador: cargo.' },
      ...(values.rol === 'DOCENTE'
        ? [
            { name: 'cargaActual', label: 'Carga actual', type: 'number', required: true, min: 0 },
            { name: 'puntajeGamificacion', label: 'Puntaje gamificación', type: 'number', required: true, min: 0 },
          ]
        : []),
    ],
    [descriptorLabel, usuario, values.rol]
  );

  const handleChange = (name, value) => {
    setValues((current) => ({ ...current, [name]: value }));
    setErrors((current) => ({ ...current, [name]: null }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const nextErrors = {};

    if (!values.nombre) nextErrors.nombre = 'Este campo es obligatorio';
    if (!values.email) nextErrors.email = 'Este campo es obligatorio';
    if (!/\S+@\S+\.\S+/.test(values.email || '')) nextErrors.email = 'Correo inválido';
    if (!usuario && !values.passwordHash) nextErrors.passwordHash = 'Este campo es obligatorio';
    if (!values.rol) nextErrors.rol = 'Este campo es obligatorio';
    if (!values.descriptor) nextErrors.descriptor = 'Este campo es obligatorio';
    if (values.rol === 'DOCENTE' && `${values.cargaActual}` === '') nextErrors.cargaActual = 'Este campo es obligatorio';
    if (values.rol === 'DOCENTE' && `${values.puntajeGamificacion}` === '') nextErrors.puntajeGamificacion = 'Este campo es obligatorio';

    if (Object.keys(nextErrors).length > 0) {
      setErrors(nextErrors);
      return;
    }

    try {
      const payload = {
        ...values,
        cargaActual: Number(values.cargaActual ?? 0),
        puntajeGamificacion: Number(values.puntajeGamificacion ?? 0),
      };

      const action = usuario ? updateUsuario(usuario.id, payload) : createUsuario(payload);
      await action;
      onSave();
    } catch (error) {
      setSubmitError(error.message);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card" style={{ width: 'min(780px, 94vw)' }}>
        <div className="panel-intro">
          <h3>{usuario ? 'Editar usuario' : 'Nuevo usuario'}</h3>
          <p>Gestione el perfil base y el descriptor específico según el rol.</p>
        </div>

        {submitError && <div className="alert error">{submitError}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            {fields.map((field) => (
              <FormField
                key={field.name}
                field={field}
                value={values[field.name]}
                error={errors[field.name]}
                options={field.options}
                onChange={handleChange}
                disabled={field.disabled}
              />
            ))}
          </div>

          <div className="form-actions">
            <button type="button" className="secondary-button" onClick={onCancel}>
              Cancelar
            </button>
            <button type="submit" className="primary-button">
              Guardar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
