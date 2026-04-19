import { useEffect, useMemo, useState } from 'react';
import { FormField } from '../../components/FormField';
import { Spinner } from '../../components/Spinner';

function toInputValue(field, value) {
  if (value === null || value === undefined) {
    return field.type === 'checkbox' ? Boolean(field.defaultValue) : field.defaultValue ?? '';
  }
  if (field.type === 'date') return String(value).slice(0, 10);
  if (field.type === 'time') return String(value).slice(0, 5);
  if (field.type === 'datetime-local') return String(value).slice(0, 16);
  if (field.type === 'checkbox') return Boolean(value);
  return value;
}

function buildInitialValues(fields, entity) {
  return fields.reduce((acc, field) => {
    acc[field.name] = toInputValue(field, entity?.[field.name]);
    return acc;
  }, {});
}

function validateFields(fields, values) {
  const nextErrors = {};

  fields.forEach((field) => {
    if (!field.required) return;
    const value = values[field.name];
    const empty =
      field.type === 'checkbox'
        ? value === undefined || value === null
        : value === undefined || value === null || value === '';

    if (empty) nextErrors[field.name] = 'Este campo es obligatorio';
    if (field.type === 'email' && value && !/\S+@\S+\.\S+/.test(value)) {
      nextErrors[field.name] = 'Correo inválido';
    }
  });

  return nextErrors;
}

function serializeField(field, value) {
  if (field.type === 'checkbox') return Boolean(value);
  if (field.type === 'number') return value === '' ? null : Number(value);
  if (field.type === 'select' && field.allowEmpty && value === '') return null;
  return value === '' ? null : value;
}

export function EntityForm({
  entity,
  title,
  description = 'Complete los campos requeridos y confirme el registro.',
  fields,
  loaders = {},
  createAction,
  updateAction,
  onSave,
  onCancel,
}) {
  const [values, setValues] = useState(() => buildInitialValues(fields, entity));
  const [errors, setErrors] = useState({});
  const [submitError, setSubmitError] = useState(null);
  const [optionsMap, setOptionsMap] = useState({});
  const [loadingOptions, setLoadingOptions] = useState(false);

  useEffect(() => {
    setValues(buildInitialValues(fields, entity));
    setErrors({});
    setSubmitError(null);
  }, [entity]);

  const loaderEntries = useMemo(() => Object.entries(loaders), [loaders]);

  useEffect(() => {
    let active = true;
    if (loaderEntries.length === 0) return undefined;

    setLoadingOptions(true);
    Promise.all(loaderEntries.map(async ([key, loader]) => [key, await loader.fetcher()]))
      .then((entries) => {
        if (!active) return;
        setOptionsMap(Object.fromEntries(entries));
      })
      .catch((error) => {
        if (!active) return;
        setSubmitError(error.message);
      })
      .finally(() => {
        if (active) setLoadingOptions(false);
      });

    return () => {
      active = false;
    };
  }, [loaderEntries]);

  const handleChange = (name, value) => {
    setValues((current) => ({ ...current, [name]: value }));
    setErrors((current) => ({ ...current, [name]: null }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const nextErrors = validateFields(fields, values);
    if (Object.keys(nextErrors).length > 0) {
      setErrors(nextErrors);
      return;
    }

    const payload = fields.reduce((acc, field) => {
      acc[field.name] = serializeField(field, values[field.name]);
      return acc;
    }, {});

    try {
      if (entity?.id) {
        await updateAction(entity.id, payload);
      } else {
        await createAction(payload);
      }
      onSave();
    } catch (error) {
      setSubmitError(error.message);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card" style={{ width: 'min(780px, 94vw)' }}>
        <div className="panel-intro">
          <h3>{title}</h3>
          <p>{description}</p>
        </div>

        {submitError && <div className="alert error">{submitError}</div>}

        {loadingOptions ? (
          <Spinner />
        ) : (
          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              {fields.map((field) => {
                const dynamicOptions = field.loader
                  ? (optionsMap[field.loader] ?? []).map((item) => ({
                      value: item[field.optionValue ?? 'id'],
                      label: field.optionLabel ? field.optionLabel(item) : item.nombre ?? item.id,
                    }))
                  : field.options ?? [];

                return (
                  <FormField
                    key={field.name}
                    field={field}
                    value={values[field.name]}
                    error={errors[field.name]}
                    options={dynamicOptions}
                    onChange={handleChange}
                  />
                );
              })}
            </div>

            <div className="form-actions">
              <button type="button" className="secondary-button" onClick={onCancel}>
                Cancelar
              </button>
              <button type="submit" className="primary-button">
                {entity?.id ? 'Guardar cambios' : 'Guardar'}
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
}
