/* Archivo documentado: Hook reutilizable para ejecutar consultas HTTP desde componentes React y manejar estados de carga, error y respuesta. */
import { useState, useEffect, useCallback } from 'react';

export function useApi(fetchFn, deps = []) {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      setData(await fetchFn());
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }, deps);

  useEffect(() => {
    load();
  }, [load]);

  return { data, loading, error, reload: load };
}
