/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/reconocimientos');

export const getReconocimientos = () => getJson(BASE, 'Error al cargar reconocimientos');
export const getReconocimiento = (id) => getJson(`${BASE}/${id}`, 'Reconocimiento no encontrado');
export const createReconocimiento = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear reconocimiento');
export const updateReconocimiento = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar reconocimiento');
export const deleteReconocimiento = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar reconocimiento');
