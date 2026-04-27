import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useContext, useEffect, useRef } from 'react';
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

/* =========================
   ICONOS
========================= */
function NavIcon({ type }) {
  const icons = {
    dashboard: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2h-5v-7H7v7H5a2 2 0 0 1-2-2z" />
      </svg>
    ),

    calendar: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <rect x="3" y="4" width="18" height="18" rx="2" />
        <line x1="3" y1="10" x2="21" y2="10" />
      </svg>
    ),

    pin: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
        <circle cx="12" cy="10" r="3" />
      </svg>
    ),

    checkin: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M20 6L9 17l-5-5" />
      </svg>
    ),

    route: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <circle cx="12" cy="12" r="10" />
        <path d="M12 2v20M2 12h20" />
      </svg>
    ),

    alert: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <circle cx="12" cy="12" r="10" />
        <line x1="12" y1="8" x2="12" y2="12" />
        <line x1="12" y1="16" x2="12.01" y2="16" />
      </svg>
    ),

    refresh: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M23 4v6h-6M1 20v-6h6" />
      </svg>
    ),

    sparkles: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M12 3v18M3 12h18" />
      </svg>
    ),

    bell: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" />
        <path d="M13.73 21a2 2 0 0 1-3.46 0" />
      </svg>
    ),

    trophy: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M6 9H4.5a2.5 2.5 0 0 1 0-5H6M18 9h1.5a2.5 2.5 0 0 0 0-5H18" />
      </svg>
    ),

    medal: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <circle cx="12" cy="8" r="6" />
      </svg>
    ),

    map: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M1 6v16l7-4 8 4 7-4V2l-7 4-8-4-7 4z" />
      </svg>
    ),

    user: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
        <circle cx="12" cy="7" r="4" />
      </svg>
    ),

    settings: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <circle cx="12" cy="12" r="3" />
      </svg>
    ),

    report: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
      </svg>
    ),

    checkpoint: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <circle cx="12" cy="12" r="10" />
      </svg>
    ),
  };

  return <span className="nav-icon">{icons[type] || icons.dashboard}</span>;
}

/* =========================
   SIDEBAR
========================= */
export function Sidebar() {
  const { collapsed, toggle } = useContext(SidebarContext);

  const role = getRole();
  const docenteId = getDocenteId();
  const unread = getUnreadCount(role, docenteId);

  const location = useLocation();
  const navigate = useNavigate();

  const toggleButtonRef = useRef(null);

  /* =========================
     BOTÓN MÓVIL / DESKTOP
  ========================= */
  useEffect(() => {
  if (toggleButtonRef.current) {
    const leftPosition = collapsed ? 15 : 290;
    toggleButtonRef.current.style.left = `${leftPosition}px`;
  }
}, [collapsed]);

  /* =========================
     LOGOUT
  ========================= */
  const handleLogout = () => {
    localStorage.clear();
    navigate('/');
  };

  const items = NAV_ITEMS[role] || [];

  return (
    <>
      {/* BOTÓN GLOBAL */}
      <button
        ref={toggleButtonRef}
        className="global-toggle"
        onClick={toggle}
      >
        ☰
      </button>

      {/* OVERLAY */}
      {!collapsed && (
        <div
          className="sidebar-overlay"
          onClick={toggle}
        />
      )}

      {/* SIDEBAR */}
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

                {/* Badge notificaciones */}
                {item.to === '/notificaciones' &&
                  unread > 0 &&
                  !collapsed && (
                    <span className="notif-badge">
                      {unread}
                    </span>
                  )}

                {item.to === '/notificaciones' &&
                  unread > 0 &&
                  collapsed && (
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
                <strong>
                  {ROLE_LABELS[role] || 'Usuario'}
                </strong>
              </div>
            )}
          </div>

          {!collapsed && (
            <button
              className="sidebar-logout"
              onClick={handleLogout}
            >
              Cerrar sesión
            </button>
          )}
        </div>
      </aside>
    </>
  );
}