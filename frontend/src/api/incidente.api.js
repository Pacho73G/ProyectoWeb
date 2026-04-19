import { getJson, sendJson, sendVoid } from './http';

const BASE = 'http://localhost:8080/api/incidentes';
const headers = { 'Content-Type': 'application/json' };

export const getIncidentes = () => getJson(BASE, 'Error al cargar incidentes');
export const getIncidente = (id) => getJson(`${BASE}/${id}`, 'Incidente no encontrado');
export const createIncidente = (data) => sendJson(BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear incidente');
export const updateIncidente = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar incidente');
export const deleteIncidente = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar incidente');
