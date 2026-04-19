import { getJson, sendJson, sendVoid } from './http';

const BASE = 'http://localhost:8080/api/recorridos';
const headers = { 'Content-Type': 'application/json' };

export const getRecorridos = () => getJson(BASE, 'Error al cargar recorridos');
export const getRecorrido = (id) => getJson(`${BASE}/${id}`, 'Recorrido no encontrado');
export const createRecorrido = (data) => sendJson(BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear recorrido');
export const updateRecorrido = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar recorrido');
export const deleteRecorrido = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar recorrido');
