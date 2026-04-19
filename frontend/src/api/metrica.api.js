import { getJson, sendJson, sendVoid } from './http';

const BASE = 'http://localhost:8080/api/metricas';
const headers = { 'Content-Type': 'application/json' };

export const getMetricas = () => getJson(BASE, 'Error al cargar métricas');
export const getMetrica = (id) => getJson(`${BASE}/${id}`, 'Métrica no encontrada');
export const createMetrica = (data) => sendJson(BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear métrica');
export const updateMetrica = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar métrica');
export const deleteMetrica = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar métrica');
