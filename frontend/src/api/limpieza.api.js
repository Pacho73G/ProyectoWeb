import { getJson, sendJson, sendVoid } from './http';

const BASE = 'http://localhost:8080/api/limpiezas';
const headers = { 'Content-Type': 'application/json' };

export const getLimpiezas = () => getJson(BASE, 'Error al cargar limpiezas');
export const getLimpieza = (id) => getJson(`${BASE}/${id}`, 'Limpieza no encontrada');
export const createLimpieza = (data) => sendJson(BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear limpieza');
export const updateLimpieza = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar limpieza');
export const deleteLimpieza = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar limpieza');
