/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/reasignaciones');

export const getReasignaciones = () => getJson(BASE, 'Error al cargar reasignaciones');
export const getReasignacion = (id) => getJson(`${BASE}/${id}`, 'Reasignacion no encontrada');
export const createReasignacion = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear reasignacion');
export const updateReasignacion = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar reasignacion');
export const deleteReasignacion = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar reasignacion');
