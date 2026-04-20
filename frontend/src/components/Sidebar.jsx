/* Archivo documentado: Componente reutilizable de la interfaz. Aísla una pieza visual o de interacción compartida por varias pantallas. */
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { HouseLogo } from './HouseLogo';
import { getRole, NAV_ITEMS, ROLE_BADGES, ROLE_INITIALS, ROLE_LABELS } from '../roleConfig';

function NavGlyph({ type }) {
  const icons = {
    dashboard: 'M5 15l4-4 3 3 7-7 M16 7h3v3',
    calendar: 'M3 5h18v16H3z M8 3v4 M16 3v4 M3 10h18',
    pin: 'M12 21s6-5.4 6-11a6 6 0 1 0-12 0c0 5.6 6 11 6 11z M12 10a2.5 2.5 0 1 0 0 .01',
    checkin: 'M12 3a9 9 0 1 0 9 9 9 9 0 0 0-9-9z M8.5 12.5 11 15l5-6',
    alert: 'M12 4 3 20h18L12 4z M12 9v4 M12 17h.01',
    refresh: 'M20 6v5h-5 M4 18v-5h5 M6.5 9A7 7 0 0 1 18 6 M17.5 15A7 7 0 0 1 6 18',
    route: 'M6 6a2.5 2.5 0 1 0 0 .01 M18 18a2.5 2.5 0 1 0 0 .01 M8.5 6h4a3 3 0 0 1 0 6h-1a3 3 0 0 0 0 6h4',
    map: 'M4 6.5 9 4l6 2 5-2v13.5L15 20l-6-2-5 2z M9 4v14 M15 6v14',
    trophy: 'M8 4h8v3a4 4 0 0 1-8 0V4z M7 5H4a3 3 0 0 0 3 5 M17 5h3a3 3 0 0 1-3 5 M12 11v4 M9 20h6 M10 15h4',
    medal: 'M12 14m-4 0a4 4 0 1 0 8 0a4 4 0 1 0-8 0 M9 3h6l-1 5h-4z M10.5 18l-1.5 3 3-1 3 1-1.5-3',
    bell: 'M6 17h12l-1.2-1.8A5 5 0 0 1 16 12.4V10a4 4 0 1 0-8 0v2.4c0 1-.3 2-.8 2.8z M10 19a2 2 0 0 0 4 0',
    user: 'M12 8a3.5 3.5 0 1 0 0 .01 M5 20a7 7 0 0 1 14 0',
    sparkles: 'M12 4l1.5 4.5L18 10l-4.5 1.5L12 16l-1.5-4.5L6 10l4.5-1.5z M19 4l.7 2.3L22 7l-2.3.7L19 10l-.7-2.3L16 7l2.3-.7z M5 15l.7 2.3L8 18l-2.3.7L5 21l-.7-2.3L2 18l2.3-.7z',
    checkpoint: 'M12 21s6-5 6-10a6 6 0 1 0-12 0c0 5 6 10 6 10z M9.5 11.5 11 13l3.5-3.5',
    settings: 'M12 12m-3 0a3 3 0 1 0 6 0a3 3 0 1 0-6 0 M19 12a7 7 0 0 0-.1-1l2-1.5-2-3.5-2.3.8a7 7 0 0 0-1.7-1L14.5 3h-5L9 5.8a7 7 0 0 0-1.7 1L5 6 3 9.5 5 11a7 7 0 0 0 0 2l-2 1.5L5 18l2.3-.8a7 7 0 0 0 1.7 1l.5 2.8h5l.5-2.8a7 7 0 0 0 1.7-1l2.3.8 2-3.5-2-1.5c.1-.3.1-.7.1-1z',
    report: 'M7 3h7l5 5v13H7z M14 3v5h5 M10 12h6 M10 16h6',
  };

  return (
    <span className="nav-icon" aria-hidden="true">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d={icons[type] ?? icons.dashboard} />
      </svg>
    </span>
  );
}

export function Sidebar() {
  const role = getRole();
  const location = useLocation();
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('rol');
    navigate('/');
  };

  return (
    <aside className={`sidebar role-${role}`}>
      <div>
        <div className="sidebar-hero">
          <Link className="brand" to="/dashboard">
            <div className="brand-badge">
              <HouseLogo />
            </div>
            <div className="brand-copy">
              <strong>Sistema de Vigilancia</strong>
              <span>Colegio San José</span>
            </div>
          </Link>
        </div>

        <nav className="nav-links">
          {NAV_ITEMS[role].map((item) => {
            const active = item.to === '/dashboard'
              ? location.pathname === '/dashboard'
              : location.pathname.startsWith(item.to);
            return (
              <Link key={item.to} to={item.to} className={active ? 'active' : ''}>
                <NavGlyph type={item.icon} />
                <span>{item.label}</span>
              </Link>
            );
          })}
        </nav>
      </div>

      <div className="sidebar-profile">
        <div className="profile-row">
          <div className="profile-avatar">{ROLE_INITIALS[role]}</div>
          <div className="profile-copy">
            <strong>{ROLE_LABELS[role]}</strong>
            <div className="profile-badge">{ROLE_BADGES[role]}</div>
          </div>
        </div>
        <button className="sidebar-logout" onClick={handleLogout}>
          Cerrar sesión
        </button>
      </div>
    </aside>
  );
}
