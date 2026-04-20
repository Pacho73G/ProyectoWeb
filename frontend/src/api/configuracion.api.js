/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl, JSON_HEADERS } from './config';
import { getJson, sendJson, sendVoid } from './http';

const BASE = apiUrl('/configuraciones');

export const getConfiguraciones = () => getJson(BASE, 'Error al cargar configuraciones');
export const getConfiguracion = (id) => getJson(`${BASE}/${id}`, 'Configuracion no encontrada');
export const createConfiguracion = (data) => sendJson(BASE, { method: 'POST', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al crear configuracion');
export const updateConfiguracion = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers: JSON_HEADERS, body: JSON.stringify(data) }, 'Error al actualizar configuracion');
export const deleteConfiguracion = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar configuracion');
