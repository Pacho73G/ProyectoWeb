/* Archivo documentado: Cliente del recurso REST correspondiente. Encapsula las llamadas GET, POST, PUT y DELETE usadas por las pantallas de la SPA. */
import { apiUrl } from './config';
import { getJson } from './http';

const BASE = apiUrl('/reportes/resumen');

export const getReporteResumen = () => getJson(BASE, 'Error al cargar reportes');
