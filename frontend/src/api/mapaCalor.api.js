/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/mapas-calor');

export const getMapasCalor = () => getJson(BASE, 'Error al cargar mapas de calor');
export const getMapaCalor = (id) => getJson(`${BASE}/${id}`, 'Mapa de calor no encontrado');
export const createMapaCalor = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear mapa de calor');
export const updateMapaCalor = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar mapa de calor');
export const deleteMapaCalor = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar mapa de calor');
