/* Archivo documentado: Componente reutilizable de la interfaz. Aísla una pieza visual o de interacción compartida por varias pantallas. */
export function FormField({ field, value, error, onChange, options = [], disabled = false }) {
  const inputId = `field-${field.name}`;

  if (field.type === 'checkbox') {
    return (
      <div className={`field ${field.full ? 'full' : ''}`}>
        <label className="checkbox" htmlFor={inputId}>
          <input
            id={inputId}
            type="checkbox"
            checked={Boolean(value)}
            disabled={disabled}
            onChange={(event) => onChange(field.name, event.target.checked)}
          />
          <span>{field.label}</span>
        </label>
        {field.help && <small className="muted">{field.help}</small>}
        {error && <span className="field-error">{error}</span>}
      </div>
    );
  }

  return (
    <div className={`field ${field.full ? 'full' : ''}`}>
      <label htmlFor={inputId}>{field.label}</label>
      {field.type === 'select' ? (
        <select
          id={inputId}
          value={value ?? ''}
          disabled={disabled || field.disabled}
          onChange={(event) => onChange(field.name, event.target.value)}
        >
          <option value="">{field.placeholder ?? 'Seleccione una opción'}</option>
          {options.map((option) => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
      ) : field.type === 'textarea' ? (
        <textarea
          id={inputId}
          value={value ?? ''}
          placeholder={field.placeholder}
          disabled={disabled || field.disabled}
          onChange={(event) => onChange(field.name, event.target.value)}
        />
      ) : (
        <input
          id={inputId}
          type={field.type ?? 'text'}
          value={value ?? ''}
          placeholder={field.placeholder}
          min={
          field.min ??
            (
            field.type === 'date'
            ? new Date().toISOString().split('T')[0]
            : field.type === 'datetime-local'
            ? new Date().toISOString().slice(0, 16)
            : undefined
            )
          }
          max={field.max}
          step={field.step}
          disabled={disabled || field.disabled}
          onChange={(event) => onChange(field.name, event.target.value)}
        />
      )}
      {field.help && <small className="muted">{field.help}</small>}
      {error && <span className="field-error">{error}</span>}
    </div>
  );
}
