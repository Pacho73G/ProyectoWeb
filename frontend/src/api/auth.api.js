import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson } from './http';

const BASE = apiUrl('/auth');

export const login = (credentials) =>
  sendJson(
    `${BASE}/login`,
    {
      method: 'POST',
      headers: JSON_HEADERS,
      body: JSON.stringify(credentials),
    },
    'Error al iniciar sesión'
  );

export const getCurrentSession = () =>
  getJson(`${BASE}/me`, 'Error al recuperar la sesión actual');
