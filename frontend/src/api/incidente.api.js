/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/incidentes');

export const getIncidentes = () => getJson(BASE, 'Error al cargar incidentes');
export const getIncidente = (id) => getJson(`${BASE}/${id}`, 'Incidente no encontrado');
export const createIncidente = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear incidente');
export const updateIncidente = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar incidente');
export const deleteIncidente = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar incidente');
