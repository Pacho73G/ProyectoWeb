/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/checkins');

export const getCheckIns = () => getJson(BASE, 'Error al cargar check-ins');
export const getCheckIn = (id) => getJson(`${BASE}/${id}`, 'Check-in no encontrado');
export const createCheckIn = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear check-in');
export const updateCheckIn = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar check-in');
export const deleteCheckIn = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar check-in');
