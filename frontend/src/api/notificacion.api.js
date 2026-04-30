import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/notificaciones');

// CRUD general para administración y soporte.
export const getNotificaciones = () =>
  getJson(BASE, 'Error al cargar notificaciones');

export const getNotificacion = (id) =>
  getJson(`${BASE}/${id}`, 'Notificacion no encontrada');

export const createNotificacion = (data) =>
  sendJson(
    BASE,
    {
      method: 'POST',
      headers: JSON_HEADERS,
      body: JSON.stringify(data),
    },
    'Error al crear notificacion'
  );

export const updateNotificacion = (id, data) =>
  sendJson(
    `${BASE}/${id}`,
    {
      method: 'PUT',
      headers: JSON_HEADERS,
      body: JSON.stringify(data),
    },
    'Error al actualizar notificacion'
  );

export const deleteNotificacion = (id) =>
  sendVoid(
    `${BASE}/${id}`,
    { method: 'DELETE' },
    'Error al eliminar notificacion'
  );

export const getUserNotifications = (userId) =>
  getJson(
    `${BASE}?userId=${userId}`,
    'Error al cargar notificaciones del usuario'
  );

// El badge del sidebar consume este conteo en lugar de recalcular en cliente.
export const getUnreadCount = (userId) =>
  getJson(
    `${BASE}/unread-count?userId=${userId}`,
    'Error al contar notificaciones'
  );

// Al entrar al centro de notificaciones se marcan como leídas en backend.
export const markAllAsRead = (userId) =>
  sendVoid(
    `${BASE}/mark-read?userId=${userId}`,
    { method: 'PUT', headers: JSON_HEADERS },
    'Error al marcar notificaciones como leidas'
  );
