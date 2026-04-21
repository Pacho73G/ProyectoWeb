import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/notificaciones');

/* API original */
export const getNotificaciones = () => getJson(BASE, 'Error al cargar notificaciones');
export const getNotificacion = (id) => getJson(`${BASE}/${id}`, 'Notificacion no encontrada');
export const createNotificacion = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear notificacion');
export const updateNotificacion = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar notificacion');
export const deleteNotificacion = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar notificacion');

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

export function getUserNotifications(role, docenteId = null) {
  const list = readLocal();

  return list.filter((n) => {
    if (n.role && n.role !== role) return false;
    if (role === 'docente' && docenteId) {
      return Number(n.docenteId) === Number(docenteId);
    }
    return true;
  });
}

export function getUnreadCount(role, docenteId = null) {
  return getUserNotifications(role, docenteId).filter((n) => !n.leida).length;
}

export function markAllAsRead(role, docenteId = null) {
  const list = readLocal().map((n) => {
    const matchRole = !n.role || n.role === role;
    const matchDocente =
      role !== 'docente' || !docenteId || Number(n.docenteId) === Number(docenteId);

    if (matchRole && matchDocente) {
      return { ...n, leida: true };
    }

    return n;
  });

  saveLocal(list);
}