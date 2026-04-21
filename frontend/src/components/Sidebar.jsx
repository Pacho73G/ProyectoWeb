import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useContext, useEffect, useRef } from 'react'; // Añade useRef
import { SidebarContext } from '../pages/SidebarContext';
import { HouseLogo } from './HouseLogo';
import { getUnreadCount } from '../api/notificacion.api';
import { getDocenteId } from '../roleConfig';
import {
  getRole,
  NAV_ITEMS,
  ROLE_INITIALS,
  ROLE_LABELS,
} from '../roleConfig';

/* ICONOS */
function NavIcon({ type }) {
  const icons = {
    dashboard: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2h-5v-7H7v7H5a2 2 0 0 1-2-2z"/>
        <path d="M9 22v-7h6v7"/>
      </svg>
    ),
    calendar: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
        <line x1="16" y1="2" x2="16" y2="6"/>
        <line x1="8" y1="2" x2="8" y2="6"/>
        <line x1="3" y1="10" x2="21" y2="10"/>
      </svg>
    ),
    pin: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
        <circle cx="12" cy="10" r="3"/>
      </svg>
    ),
    checkin: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M20 6L9 17l-5-5"/>
      </svg>
    ),
    route: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <circle cx="12" cy="12" r="10"/>
        <path d="M12 2v20"/>
        <path d="M2 12h20"/>
      </svg>
    ),
    alert: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <circle cx="12" cy="12" r="10"/>
        <line x1="12" y1="8" x2="12" y2="12"/>
        <line x1="12" y1="16" x2="12.01" y2="16"/>
      </svg>
    ),
    refresh: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M23 4v6h-6"/>
        <path d="M1 20v-6h6"/>
        <path d="M3.51 9a9 9 0 0 1 14.98-2.48L23 10"/>
        <path d="M20.49 15a9 9 0 0 1-14.98 2.48L1 14"/>
      </svg>
    ),
    sparkles: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M12 3v18M3 12h18"/>
      </svg>
    ),
    bell: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/>
        <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
      </svg>
    ),
    trophy: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M6 9H4.5a2.5 2.5 0 0 1 0-5H6"/>
        <path d="M18 9h1.5a2.5 2.5 0 0 0 0-5H18"/>
        <path d="M4 22h16"/>
        <path d="M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20.24 7 22"/>
        <path d="M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20.24 17 22"/>
        <path d="M8 2h8"/>
      </svg>
    ),
    medal: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <circle cx="12" cy="8" r="6"/>
        <path d="M5 21l4-4 3 3 3-3 4 4"/>
      </svg>
    ),
    map: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M1 6v16l7-4 8 4 7-4V2l-7 4-8-4-7 4z"/>
      </svg>
    ),
    user: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
        <circle cx="12" cy="7" r="4"/>
      </svg>
    ),
    settings: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <circle cx="12" cy="12" r="3"/>
        <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"/>
      </svg>
    ),
    report: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
        <polyline points="14 2 14 8 20 8"/>
        <line x1="16" y1="13" x2="8" y2="13"/>
        <line x1="16" y1="17" x2="8" y2="17"/>
        <polyline points="10 9 9 9 8 9"/>
      </svg>
    ),
    checkpoint: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <circle cx="12" cy="12" r="10"/>
        <path d="M12 2v20"/>
        <path d="M2 12h20"/>
      </svg>
    ),
  };

  const svgContent = icons[type] || icons.dashboard;
  return <span className="nav-icon">{svgContent}</span>;
}

export function Sidebar() {
  const { collapsed, toggle } = useContext(SidebarContext);
  const role = getRole();
  const location = useLocation();
  const navigate = useNavigate();
  const toggleButtonRef = useRef(null);
  const unread = getUnreadCount(role, getDocenteId());

  useEffect(() => {
    if (toggleButtonRef.current) {
      const sidebarWidth = collapsed ? 70 : 250;
      const leftPosition = sidebarWidth + 5;
      toggleButtonRef.current.style.left = `${leftPosition}px`;
    }
  }, [collapsed]);

  const handleLogout = () => {
    localStorage.clear();
    navigate('/');
  };

  const items = NAV_ITEMS[role] || [];

  return (
    <>
      {/* BOTÓN GLOBAL */}
      <button ref={toggleButtonRef} className="global-toggle" onClick={toggle}>
        ☰
      </button>

      {/* OVERLAY (solo cuando sidebar está abierto en móvil) */}
      {!collapsed && <div className="sidebar-overlay" onClick={toggle}></div>}

      <aside className={`sidebar ${collapsed ? 'collapsed' : 'open'}`}>
        {/* HEADER */}
        <div className="sidebar-hero">
          <Link to="/dashboard" className="brand">
            <div className="brand-badge">
              <HouseLogo />
            </div>
            {!collapsed && (
              <div className="brand-copy">
                <strong>Sistema de Vigilancia</strong>
                <span>Colegio San José</span>
              </div>
            )}
          </Link>
        </div>

        {/* MENÚ */}
        <nav className="nav-links">
          {items.map((item) => {
            const active =
              item.to === '/dashboard'
                ? location.pathname === '/dashboard'
                : location.pathname.startsWith(item.to);
            return (
              <Link
  key={item.to}
  to={item.to}
  className={`nav-item ${active ? 'active' : ''}`}
>
  <NavIcon type={item.icon} />

  {!collapsed && <span>{item.label}</span>}

  {item.to === '/notificaciones' && unread > 0 && !collapsed && (
    <span className="notif-badge">{unread}</span>
  )}

  {item.to === '/notificaciones' && unread > 0 && collapsed && (
    <span className="notif-dot"></span>
  )}
</Link>
            );
          })}
        </nav>

        {/* PERFIL */}
        <div className="sidebar-profile">
          <div className="profile-row">
            <div className="profile-avatar">
              {ROLE_INITIALS[role] || 'U'}
            </div>
            {!collapsed && (
              <div className="profile-copy">
                <strong>{ROLE_LABELS[role] || 'Usuario'}</strong>
              </div>
            )}
          </div>
          {!collapsed && (
            <button className="sidebar-logout" onClick={handleLogout}>
              Cerrar sesión
            </button>
          )}
        </div>
      </aside>
    </>
  );
}