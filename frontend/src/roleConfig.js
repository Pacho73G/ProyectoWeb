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

const STORAGE_KEYS = {
  token: 'authToken',
  role: 'rol',
  userId: 'userId',
  userNombre: 'userNombre',
  userEmail: 'userEmail',
  docenteId: 'docenteId',
  docenteNombre: 'docenteNombre',
  docenteEmail: 'docenteEmail',
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
    { to: '/limpiezas', label: 'Limpieza', icon: 'sparkles' },
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


function normalizeRole(role) {
  return role ? role.trim().toLowerCase() : null;
}

export function getRole() {
  return normalizeRole(localStorage.getItem(STORAGE_KEYS.role));
}

export function getAuthToken() {
  return localStorage.getItem(STORAGE_KEYS.token) ?? null;
}

export function hasActiveSession() {
  return Boolean(getAuthToken());
}

export function clearAuthSession() {
  Object.values(STORAGE_KEYS).forEach((key) => localStorage.removeItem(key));
}

export function setAuthSession(authResponse, options = {}) {
  const role = normalizeRole(authResponse?.rol);
  const currentToken = getAuthToken();
  const token = authResponse?.token ?? (options.preserveToken ? currentToken : null);

  if (token) {
    localStorage.setItem(STORAGE_KEYS.token, token);
  } else {
    localStorage.removeItem(STORAGE_KEYS.token);
  }

  localStorage.setItem(STORAGE_KEYS.role, role ?? '');
  localStorage.setItem(STORAGE_KEYS.userId, String(authResponse.id));
  localStorage.setItem(STORAGE_KEYS.userNombre, authResponse.nombre ?? '');
  localStorage.setItem(STORAGE_KEYS.userEmail, authResponse.email ?? '');

  if (role === 'docente') {
    // En el modelo actual cada docente autenticado usa su propio id como filtro principal.
    localStorage.setItem(STORAGE_KEYS.docenteId, String(authResponse.id));
    localStorage.setItem(STORAGE_KEYS.docenteNombre, authResponse.nombre ?? '');
    localStorage.setItem(STORAGE_KEYS.docenteEmail, authResponse.email ?? '');
  } else {
    localStorage.removeItem(STORAGE_KEYS.docenteId);
    localStorage.removeItem(STORAGE_KEYS.docenteNombre);
    localStorage.removeItem(STORAGE_KEYS.docenteEmail);
  }

  return getStoredSession();
}

export function getStoredSession() {
  const role = getRole();
  const token = getAuthToken();
  const userId = getUserId();

  if (!token || !role || !userId) {
    return null;
  }

  return {
    token,
    role,
    id: userId,
    nombre: getUserNombre(),
    email: getUserEmail(),
    docenteId: getDocenteId(),
  };
}

export function getUserId() {
  const raw = localStorage.getItem(STORAGE_KEYS.userId);
  return raw ? Number(raw) : null;
}

export function getUserNombre() {
  return localStorage.getItem(STORAGE_KEYS.userNombre) ?? null;
}

export function getUserEmail() {
  return localStorage.getItem(STORAGE_KEYS.userEmail) ?? null;
}


export function getDocenteId() {
  const raw = localStorage.getItem(STORAGE_KEYS.docenteId);
  return raw ? Number(raw) : null;
}

export function getDocenteNombre() {
  return localStorage.getItem(STORAGE_KEYS.docenteNombre) ?? null;
}

export function getDocenteEmail() {
  return localStorage.getItem(STORAGE_KEYS.docenteEmail) ?? null;
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
    case 'reportes':
      return readOnly(action);

    case 'limpiezas':
      // El admin administra la asignación completa de limpiezas.
      return managed(action);

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
      return createOnly(action);

    case 'limpiezas':
      // El docente no crea asignaciones; solo visualiza y completa las que le asignaron.
      return ['view', 'edit', 'save'].includes(action);

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
    case 'reportes':
      return readOnly(action);

    case 'reasignaciones':
      return ['view', 'create', 'edit'].includes(action);

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
