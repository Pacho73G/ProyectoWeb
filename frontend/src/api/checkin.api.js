import { getJson, sendJson, sendVoid } from './http';

const BASE = 'http://localhost:8080/api/checkins';
const headers = { 'Content-Type': 'application/json' };

export const getCheckIns = () => getJson(BASE, 'Error al cargar check-ins');
export const getCheckIn = (id) => getJson(`${BASE}/${id}`, 'Check-in no encontrado');
export const createCheckIn = (data) => sendJson(BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear check-in');
export const updateCheckIn = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar check-in');
export const deleteCheckIn = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar check-in');
