/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/recorridos');

export const getRecorridos = () => getJson(BASE, 'Error al cargar recorridos');
export const getRecorrido = (id) => getJson(`${BASE}/${id}`, 'Recorrido no encontrado');
export const createRecorrido = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear recorrido');
export const updateRecorrido = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar recorrido');
export const deleteRecorrido = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar recorrido');
