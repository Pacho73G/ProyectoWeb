import { getJson, sendJson, sendVoid } from './http';

const BASE = 'http://localhost:8080/api/reconocimientos';
const headers = { 'Content-Type': 'application/json' };

export const getReconocimientos = () => getJson(BASE, 'Error al cargar reconocimientos');
export const getReconocimiento = (id) => getJson(`${BASE}/${id}`, 'Reconocimiento no encontrado');
export const createReconocimiento = (data) => sendJson(BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear reconocimiento');
export const updateReconocimiento = (id, data) => sendJson(`${BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar reconocimiento');
export const deleteReconocimiento = (id) => sendVoid(`${BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar reconocimiento');
