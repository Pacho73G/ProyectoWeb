import { getJson, sendJson, sendVoid } from './http';

const BASE = 'http://localhost:8080/api/configuraciones';
const headers = { 'Content-Type': 'application/json' };

export const getConfiguraciones = () => getJson(BASE, 'Error al cargar configuraciones');
export const getConfiguracion = (id) => getJson(`${BASE}/${id}`, 'Configuración no encontrada');
export const createConfiguracion = (data) => sendJson(BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear configuración');
export const updateConfiguracion = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar configuración');
export const deleteConfiguracion = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar configuración');
