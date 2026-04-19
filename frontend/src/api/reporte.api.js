import { getJson } from './http';

const BASE = 'http://localhost:8080/api/reportes/resumen';

export const getReporteResumen = () => getJson(BASE, 'Error al cargar reportes');
