/* Archivo documentado: Utilidades HTTP genéricas del frontend. Estandarizan parseo de respuestas, envío del JWT y manejo básico de errores de red. */
import { clearAuthSession, getAuthToken } from '../roleConfig';

async function parseResponse(response, fallbackMessage) {
  if (response.ok) {
    if (response.status === 204) return null;
    return response.json();
  }

  if (response.status === 401) {
    // Cuando el token expira o ya no es válido, la sesión local se limpia
    // para obligar a un nuevo login coherente con el backend.
    clearAuthSession();
    window.dispatchEvent(new Event('auth:unauthorized'));
  }

  let message = fallbackMessage;
  try {
    const body = await response.json();
    message = body.message || fallbackMessage;
  } catch {}

  throw new Error(message);
}

function withAuthHeaders(options = {}) {
  const headers = new Headers(options.headers ?? {});
  const token = getAuthToken();

  if (token && !headers.has('Authorization')) {
    headers.set('Authorization', `Bearer ${token}`);
  }

  return {
    ...options,
    headers,
  };
}

export function getJson(url, fallbackMessage) {
  return fetch(url, withAuthHeaders()).then((response) => parseResponse(response, fallbackMessage));
}

export function sendJson(url, options, fallbackMessage) {
  return fetch(url, withAuthHeaders(options)).then((response) => parseResponse(response, fallbackMessage));
}

export function sendVoid(url, options, fallbackMessage) {
  return fetch(url, withAuthHeaders(options)).then((response) => parseResponse(response, fallbackMessage));
}
