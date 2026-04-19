import { getJson, sendJson, sendVoid } from './http';

const BASE = 'http://localhost:8080/api/turnos';
const headers = { 'Content-Type': 'application/json' };

export const getTurnos = () => getJson(BASE, 'Error al cargar turnos');
export const getTurno = (id) => getJson(`${BASE}/${id}`, 'Turno no encontrado');
export const createTurno = (data) => sendJson(BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear turno');
export const updateTurno = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar turno');
export const deleteTurno = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar turno');
