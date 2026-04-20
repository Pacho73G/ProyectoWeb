/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/metricas');

export const getMetricas = () => getJson(BASE, 'Error al cargar metricas');
export const getMetrica = (id) => getJson(`${BASE}/${id}`, 'Metrica no encontrada');
export const createMetrica = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear metrica');
export const updateMetrica = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar metrica');
export const deleteMetrica = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar metrica');
