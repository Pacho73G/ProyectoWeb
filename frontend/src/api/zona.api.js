/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/zonas');

export const getZonas = () => getJson(BASE, 'Error al cargar zonas');
export const getZona = (id) => getJson(`${BASE}/${id}`, 'Zona no encontrada');
export const createZona = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear zona');
export const updateZona = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar zona');
export const deleteZona = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar zona');
