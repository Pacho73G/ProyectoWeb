export function FormField({
  field,
  value,
  error,
  onChange,
  options = [],
  disabled = false,
}) {
  const inputId = `field-${field.name}`;

  /* =========================
     FIX FECHAS (LOCAL TIME)
  ========================= */
  let minValue = field.min;

  if (!minValue && field.type === 'datetime-local') {
    const now = new Date();
    const local = new Date(
      now.getTime() - now.getTimezoneOffset() * 60000
    )
      .toISOString()
      .slice(0, 16);

    minValue = local;
  }

  if (!minValue && field.type === 'date') {
    const now = new Date();
    const local = new Date(
      now.getTime() - now.getTimezoneOffset() * 60000
    )
      .toISOString()
      .split('T')[0];

    minValue = local;
  }

  /* =========================
     CHECKBOX
  ========================= */
  if (field.type === 'checkbox') {
    return (
      <div className={`field ${field.full ? 'full' : ''}`}>
        <label className="checkbox" htmlFor={inputId}>
          <input
            id={inputId}
            type="checkbox"
            checked={Boolean(value)}
            disabled={disabled}
            onChange={(e) => onChange(field.name, e.target.checked)}
          />
          <span>{field.label}</span>
        </label>

        {field.help && <small className="muted">{field.help}</small>}
        {error && <span className="field-error">{error}</span>}
      </div>
    );
  }

  /* =========================
     INPUTS
  ========================= */
  return (
    <div className={`field ${field.full ? 'full' : ''}`}>
      <label htmlFor={inputId}>{field.label}</label>

      {field.type === 'select' ? (
        <select
          id={inputId}
          value={value ?? ''}
          disabled={disabled || field.disabled}
          onChange={(e) => onChange(field.name, e.target.value)}
        >
          <option value="">
            {field.placeholder ?? 'Seleccione una opción'}
          </option>

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
          onChange={(e) => onChange(field.name, e.target.value)}
        />
      ) : (
        <input
          id={inputId}
          type={field.type ?? 'text'}
          value={value ?? ''}
          placeholder={field.placeholder}
          min={minValue}   
          max={field.max}
          step={field.step}
          disabled={disabled || field.disabled}
          onChange={(e) => onChange(field.name, e.target.value)}
        />
      )}

      {field.help && <small className="muted">{field.help}</small>}
      {error && <span className="field-error">{error}</span>}
    </div>
  );
}