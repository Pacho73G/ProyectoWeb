/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/turnos');

export const getTurnos = () => getJson(BASE, 'Error al cargar turnos');
export const getTurno = (id) => getJson(`${BASE}/${id}`, 'Turno no encontrado');
export const createTurno = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear turno');
export const updateTurno = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar turno');
export const deleteTurno = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar turno');
