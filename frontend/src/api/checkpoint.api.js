import { getJson, sendJson, sendVoid } from './http';

const BASE = 'http://localhost:8080/api/checkpoints';
const headers = { 'Content-Type': 'application/json' };

export const getCheckpoints = () => getJson(BASE, 'Error al cargar checkpoints');
export const getCheckpoint = (id) => getJson(`${BASE}/${id}`, 'Checkpoint no encontrado');
export const createCheckpoint = (data) => sendJson(BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear checkpoint');
export const updateCheckpoint = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar checkpoint');
export const deleteCheckpoint = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar checkpoint');
