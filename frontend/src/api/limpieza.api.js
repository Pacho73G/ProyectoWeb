/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/limpiezas');

export const getLimpiezas = () => getJson(BASE, 'Error al cargar limpiezas');
export const getLimpieza = (id) => getJson(`${BASE}/${id}`, 'Limpieza no encontrada');
export const createLimpieza = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear limpieza');
export const updateLimpieza = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar limpieza');
export const deleteLimpieza = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar limpieza');
