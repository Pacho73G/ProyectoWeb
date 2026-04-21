/* Archivo documentado: Pantalla de acceso al sistema. Permite elegir el rol y redirige al selector de docente si se elige ese perfil. */
import { useNavigate } from 'react-router-dom';
import { HouseLogo } from '../components/HouseLogo';

function RoleIcon({ kind }) {
  if (kind === 'docente') {
    return (
      <svg viewBox="0 0 64 64" aria-hidden="true">
        <path d="M12 26 32 16l20 10-20 10z" fill="none" stroke="currentColor" strokeWidth="4" strokeLinejoin="round" />
        <path d="M20 32v8c0 3 6 6 12 6s12-3 12-6v-8" fill="none" stroke="currentColor" strokeWidth="4" strokeLinecap="round" strokeLinejoin="round" />
        <path d="M52 26v12" fill="none" stroke="currentColor" strokeWidth="4" strokeLinecap="round" />
      </svg>
    );
  }

  if (kind === 'administrador') {
    return (
      <svg viewBox="0 0 64 64" aria-hidden="true">
        <circle cx="32" cy="22" r="8" fill="none" stroke="currentColor" strokeWidth="4" />
        <path d="M20 48c0-7 5-12 12-12s12 5 12 12" fill="none" stroke="currentColor" strokeWidth="4" strokeLinecap="round" />
      </svg>
    );
  }

  return <HouseLogo />;
}

export function Login() {
  const navigate = useNavigate();

  const selectRole = (role) => {
    localStorage.setItem('rol', role);
    if (role === 'docente') {
      // Docente goes to the sub-selector to pick which teacher
      navigate('/docente-selector');
    } else {
      navigate('/dashboard');
    }
  };

  return (
    <div className="login-page">
      <div className="login-card">
        <div className="hero">
          <div className="hero-icon">
            <HouseLogo />
          </div>
          <h1>Sistema de Vigilancia Docente</h1>
          <p>Colegio San José · Gestión de Supervisión Escolar</p>
        </div>

        <div className="role-selector">
          <div className="section-heading">
            <h2>Selecciona tu rol</h2>
            <p>Elige cómo quieres acceder al sistema.</p>
          </div>

          <div className="role-grid">
            <button type="button" className="role-card green" onClick={() => selectRole('coordinador')}>
              <div className="role-icon">
                <RoleIcon kind="coordinador" />
              </div>
              <span>Coordinador</span>
              <small>Gestión completa del sistema</small>
            </button>

            <button type="button" className="role-card blue" onClick={() => selectRole('docente')}>
              <div className="role-icon">
                <RoleIcon kind="docente" />
              </div>
              <span>Docente</span>
              <small>Check-in, recorridos y reportes</small>
            </button>

            <button type="button" className="role-card purple" onClick={() => selectRole('administrador')}>
              <div className="role-icon">
                <RoleIcon kind="administrador" />
              </div>
              <span>Administrador</span>
              <small>Usuarios, zonas y configuración</small>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}