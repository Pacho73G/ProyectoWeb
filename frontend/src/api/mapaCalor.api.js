import { getJson, sendJson, sendVoid } from './http';

const BASE = 'http://localhost:8080/api/mapas-calor';
const headers = { 'Content-Type': 'application/json' };

export const getMapasCalor = () => getJson(BASE, 'Error al cargar mapas de calor');
export const getMapaCalor = (id) => getJson(`${BASE}/${id}`, 'Mapa de calor no encontrado');
export const createMapaCalor = (data) => sendJson(BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear mapa de calor');
export const updateMapaCalor = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar mapa de calor');
export const deleteMapaCalor = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar mapa de calor');
