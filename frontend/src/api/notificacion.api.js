import { getJson, sendJson, sendVoid } from './http';

const BASE = 'http://localhost:8080/api/notificaciones';
const headers = { 'Content-Type': 'application/json' };

export const getNotificaciones = () => getJson(BASE, 'Error al cargar notificaciones');
export const getNotificacion = (id) => getJson(`${BASE}/${id}`, 'Notificación no encontrada');
export const createNotificacion = (data) => sendJson(BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear notificación');
export const updateNotificacion = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar notificación');
export const deleteNotificacion = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar notificación');
