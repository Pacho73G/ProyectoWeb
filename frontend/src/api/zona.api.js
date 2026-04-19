import { getJson, sendJson, sendVoid } from './http';

const BASE = 'http://localhost:8080/api/zonas';
const headers = { 'Content-Type': 'application/json' };

export const getZonas = () => getJson(BASE, 'Error al cargar zonas');
export const getZona = (id) => getJson(`${BASE}/${id}`, 'Zona no encontrada');
export const createZona = (data) => sendJson(BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear zona');
export const updateZona = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar zona');
export const deleteZona = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar zona');
