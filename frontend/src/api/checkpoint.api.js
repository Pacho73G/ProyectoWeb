/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/checkpoints');

export const getCheckpoints = () => getJson(BASE, 'Error al cargar checkpoints');
export const getCheckpoint = (id) => getJson(`${BASE}/${id}`, 'Checkpoint no encontrado');
export const createCheckpoint = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear checkpoint');
export const updateCheckpoint = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar checkpoint');
export const deleteCheckpoint = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar checkpoint');
