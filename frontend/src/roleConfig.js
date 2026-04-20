/* Archivo documentado: Configuración central de roles de la interfaz. Define permisos visibles, navegación y etiquetas usadas por la SPA según el perfil activo. */
export const ROLE_LABELS = {
  coordinador: 'Coordinador',
  docente: 'Docente',
  administrador: 'Administrador',
};

export const ROLE_BADGES = {
  coordinador: 'Coordinador',
  docente: 'Docente',
  administrador: 'Administrador',
};

export const ROLE_VIEW_LABELS = {
  coordinador: 'Vista de coordinador',
  docente: 'Vista de docente',
  administrador: 'Vista de administrador',
};

export const ROLE_INITIALS = {
  coordinador: 'CO',
  docente: 'DO',
  administrador: 'AD',
};

export const NAV_ITEMS = {
  coordinador: [
    { to: '/dashboard', label: 'Dashboard en vivo', icon: 'dashboard' },
    { to: '/turnos', label: 'Turnos', icon: 'calendar' },
    { to: '/zonas', label: 'Cobertura', icon: 'pin' },
    { to: '/checkins', label: 'Check-ins', icon: 'checkin' },
    { to: '/incidentes', label: 'Incidentes', icon: 'alert' },
    { to: '/reasignaciones', label: 'Reasignaciones', icon: 'refresh' },
    { to: '/recorridos', label: 'Recorridos', icon: 'route' },
    { to: '/mapas-calor', label: 'Mapas de calor', icon: 'map' },
    { to: '/metricas', label: 'Métricas', icon: 'trophy' },
    { to: '/reconocimientos', label: 'Reconocimientos', icon: 'medal' },
    { to: '/reportes', label: 'Reportes', icon: 'report' },
    { to: '/notificaciones', label: 'Notificaciones', icon: 'bell' },
  ],
  docente: [
    { to: '/dashboard', label: 'Dashboard', icon: 'dashboard' },
    { to: '/turnos', label: 'Mis turnos', icon: 'calendar' },
    { to: '/checkins', label: 'Check-in', icon: 'checkin' },
    { to: '/recorridos', label: 'Recorridos', icon: 'route' },
    { to: '/incidentes', label: 'Incidentes', icon: 'alert' },
    { to: '/reasignaciones', label: 'Reasignaciones', icon: 'refresh' },
    { to: '/limpiezas', label: 'Limpieza', icon: 'sparkles' },
    { to: '/notificaciones', label: 'Notificaciones', icon: 'bell' },
    { to: '/metricas', label: 'Mis métricas', icon: 'trophy' },
    { to: '/reconocimientos', label: 'Reconocimientos', icon: 'medal' },
  ],
  administrador: [
    { to: '/dashboard', label: 'Dashboard', icon: 'dashboard' },
    { to: '/usuarios', label: 'Usuarios', icon: 'user' },
    { to: '/turnos', label: 'Turnos', icon: 'calendar' },
    { to: '/zonas', label: 'Zonas', icon: 'pin' },
    { to: '/checkpoints', label: 'Checkpoints', icon: 'checkpoint' },
    { to: '/configuraciones', label: 'Configuración', icon: 'settings' },
    { to: '/incidentes', label: 'Incidentes', icon: 'alert' },
    { to: '/reasignaciones', label: 'Reasignaciones', icon: 'refresh' },
    { to: '/metricas', label: 'Métricas', icon: 'trophy' },
    { to: '/mapas-calor', label: 'Mapas de calor', icon: 'map' },
    { to: '/reportes', label: 'Reportes', icon: 'report' },
    { to: '/notificaciones', label: 'Notificaciones', icon: 'bell' },
  ],
};

const RESOURCE_BY_PATH = {
  '/usuarios': 'usuarios',
  '/turnos': 'turnos',
  '/zonas': 'zonas',
  '/checkins': 'checkins',
  '/incidentes': 'incidentes',
  '/reasignaciones': 'reasignaciones',
  '/limpiezas': 'limpiezas',
  '/notificaciones': 'notificaciones',
  '/mapas-calor': 'mapas-calor',
  '/metricas': 'metricas',
  '/reconocimientos': 'reconocimientos',
  '/recorridos': 'recorridos',
  '/checkpoints': 'checkpoints',
  '/configuraciones': 'configuraciones',
  '/reportes': 'reportes',
};

export function getRole() {
  return localStorage.getItem('rol') ?? 'coordinador';
}

function managed(action) {
  return ['view', 'create', 'edit', 'delete', 'save'].includes(action);
}

function readOnly(action) {
  return action === 'view';
}

function createOnly(action) {
  return ['view', 'create', 'save'].includes(action);
}

function allowsAdministrador(resource, action) {
  switch (resource) {
    case 'usuarios':
    case 'turnos':
    case 'zonas':
    case 'checkpoints':
    case 'notificaciones':
    case 'metricas':
    case 'mapas-calor':
    case 'reconocimientos':
    case 'configuraciones':
      return managed(action);
    case 'incidentes':
    case 'reasignaciones':
    case 'recorridos':
    case 'checkins':
    case 'limpiezas':
    case 'reportes':
      return readOnly(action);
    default:
      return false;
  }
}

function allowsDocente(resource, action) {
  switch (resource) {
    case 'turnos':
    case 'notificaciones':
    case 'metricas':
    case 'reconocimientos':
      return readOnly(action);
    case 'checkins':
    case 'incidentes':
    case 'recorridos':
    case 'limpiezas':
      return createOnly(action);
    case 'reasignaciones':
      return ['view', 'create'].includes(action);
    default:
      return false;
  }
}

function allowsCoordinador(resource, action) {
  switch (resource) {
    case 'turnos':
    case 'zonas':
    case 'checkins':
    case 'incidentes':
    case 'mapas-calor':
    case 'metricas':
    case 'notificaciones':
    case 'recorridos':
    case 'reconocimientos':
      return readOnly(action);
    case 'reasignaciones':
      return ['view', 'create', 'edit'].includes(action);
    case 'reportes':
      return readOnly(action);
    default:
      return false;
  }
}

export function allows(role, resource, action) {
  if (role === 'administrador') return allowsAdministrador(resource, action);
  if (role === 'docente') return allowsDocente(resource, action);
  return allowsCoordinador(resource, action);
}

export function showsActions(role, resource) {
  return allows(role, resource, 'edit') || allows(role, resource, 'delete');
}

export function canAccessPath(role, path) {
  if (path === '/dashboard') return true;
  const resource = RESOURCE_BY_PATH[path];
  return resource ? allows(role, resource, 'view') : false;
}
