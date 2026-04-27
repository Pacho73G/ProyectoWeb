import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';
import {
  getRole,
  getUserId,
} from '../roleConfig';

const BASE = apiUrl('/notificaciones');

/* ===============================
   API BACKEND ORIGINAL
================================= */

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

/* ===============================
   SISTEMA LOCAL DE NOTIFICACIONES
================================= */

const KEY = 'app_notifications';

function readLocal() {
  return JSON.parse(localStorage.getItem(KEY) || '[]');
}

function saveLocal(data) {
  localStorage.setItem(KEY, JSON.stringify(data));
}

/* Crear nueva notificación */
export function pushNotification(notification) {
  const list = readLocal();

  list.unshift({
    id: Date.now(),
    leida: false,
    fecha: new Date().toLocaleString(),
    ...notification,
  });

  saveLocal(list);
}

/* ==========================================
   Obtener notificaciones del usuario actual
========================================== */
export function getUserNotifications() {
  const role = getRole();
  const userId = getUserId();

  const list = readLocal();

  return list.filter((n) => {
    const sameRole = !n.role || n.role === role;
    const sameUser =
      n.userId === undefined ||
      Number(n.userId) === Number(userId);

    return sameRole && sameUser;
  });
}

/* Cantidad no leídas */
export function getUnreadCount() {
  return getUserNotifications().filter((n) => !n.leida).length;
}

/* Marcar todas como leídas */
export function markAllAsRead() {
  const role = getRole();
  const userId = getUserId();

  const updated = readLocal().map((n) => {
    const sameRole = !n.role || n.role === role;
    const sameUser =
      n.userId === undefined ||
      Number(n.userId) === Number(userId);

    if (sameRole && sameUser) {
      return {
        ...n,
        leida: true,
      };
    }

    return n;
  });

  saveLocal(updated);
}