/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/notificaciones');

export const getNotificaciones = () => getJson(BASE, 'Error al cargar notificaciones');
export const getNotificacion = (id) => getJson(`${BASE}/${id}`, 'Notificacion no encontrada');
export const createNotificacion = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear notificacion');
export const updateNotificacion = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar notificacion');
export const deleteNotificacion = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar notificacion');
