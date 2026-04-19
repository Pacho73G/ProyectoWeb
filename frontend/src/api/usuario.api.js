import { getJson, sendJson, sendVoid } from './http';

const USERS_BASE = 'http://localhost:8080/api/usuarios';
const DOCENTES_BASE = 'http://localhost:8080/api/docentes';
const headers = { 'Content-Type': 'application/json' };

export const getUsuarios = () => getJson(USERS_BASE, 'Error al cargar usuarios');
export const getUsuario = (id) => getJson(`${USERS_BASE}/${id}`, 'Usuario no encontrado');
export const createUsuario = (data) => sendJson(USERS_BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear usuario');
export const updateUsuario = (id, data) => sendJson(`${USERS_BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar usuario');
export const deleteUsuario = (id) => sendVoid(`${USERS_BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar usuario');
export const getDocentes = () => getJson(DOCENTES_BASE, 'Error al cargar docentes');
export const getDocente = (id) => getJson(`${DOCENTES_BASE}/${id}`, 'Docente no encontrado');
export const createDocente = (data) => sendJson(DOCENTES_BASE, { method: 'POST', headers, body: JSON.stringify(data) }, 'Error al crear docente');
export const updateDocente = (id, data) => sendJson(`${DOCENTES_BASE}/${id}`, { method: 'PUT', headers, body: JSON.stringify(data) }, 'Error al actualizar docente');
export const deleteDocente = (id) => sendVoid(`${DOCENTES_BASE}/${id}`, { method: 'DELETE' }, 'Error al eliminar docente');
