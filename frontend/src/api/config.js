/* Archivo documentado: Configuración base del cliente HTTP del frontend. Centraliza la URL de la API y encabezados JSON compartidos. */
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api';

export const JSON_HEADERS = {
  'Content-Type': 'application/json',
};

export const apiUrl = (path) => `${API_BASE_URL}${path}`;
