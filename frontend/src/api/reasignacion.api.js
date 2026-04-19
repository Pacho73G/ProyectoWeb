import { getJson, sendJson, sendVoid } from './http';

const BASE = 'http://localhost:8080/api/reasignaciones';
const headers = { 'Content-Type': 'application/json' };

export const getReasignaciones = () => getJson(BASE, 'Error al cargar reasignaciones');
export const getReasignacion = (id) => getJson(`${BASE}/${id}`, 'Reasignación no encontrada');
export const createReasignacion = (data) => sendJson(BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear reasignación');
export const updateReasignacion = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar reasignación');
export const deleteReasignacion = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar reasignación');
