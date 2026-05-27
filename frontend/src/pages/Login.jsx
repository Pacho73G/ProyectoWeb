import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { HouseLogo } from '../components/HouseLogo';
import { useAuth } from '../auth/AuthContext';

export function Login() {
  const navigate = useNavigate();
  const { login, isAuthenticated, loading } = useAuth();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!loading && isAuthenticated) {
      navigate('/dashboard', { replace: true });
    }
  }, [isAuthenticated, loading, navigate]);

  async function handleSubmit(event) {
    event.preventDefault();
    setSubmitting(true);
    setError(null);

    try {
      // El frontend ya no simula roles: el backend autentica y devuelve el perfil real.
      await login({ email, password });
      navigate('/dashboard', { replace: true });
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSubmitting(false);
    }
  }

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
            <h2>Ingreso institucional</h2>
            <p>
              Inicia sesión con tu correo y contraseña para entrar con el rol
              asignado por el sistema.
            </p>
          </div>

          {error && <div className="alert error">{error}</div>}

          <form onSubmit={handleSubmit}>
            <div className="form-grid" style={{ maxWidth: '640px', margin: '0 auto' }}>
              <div className="field full">
                <label htmlFor="login-email">Correo institucional</label>
                <input
                  id="login-email"
                  type="email"
                  value={email}
                  placeholder="nombre@colegio.edu"
                  autoComplete="username"
                  required
                  onChange={(event) => setEmail(event.target.value)}
                />
              </div>

              <div className="field full">
                <label htmlFor="login-password">Contraseña</label>
                <input
                  id="login-password"
                  type="password"
                  value={password}
                  placeholder="Ingresa tu contraseña"
                  autoComplete="current-password"
                  required
                  onChange={(event) => setPassword(event.target.value)}
                />
              </div>
            </div>

            <div className="form-actions" style={{ justifyContent: 'center', marginTop: '20px' }}>
              <button
                type="submit"
                className="primary-button"
                disabled={submitting}
              >
                {submitting ? 'Ingresando...' : 'Iniciar sesión'}
              </button>
            </div>
          </form>

          <div className="panel" style={{ maxWidth: '640px', margin: '28px auto 0' }}>
            <div className="panel-intro">
              <h3>Credenciales semilla</h3>
              <p>
                Mientras sigues sin módulo de cambio de contraseña, puedes usar
                las credenciales cargadas por el batch inicial.
              </p>
            </div>
            <div className="stack-list">
              <div className="item-card">
                <strong>Administrador</strong>
                <span><code>laura.admin@colegio.edu</code> / <code>hash-admin</code></span>
              </div>
              <div className="item-card">
                <strong>Coordinador</strong>
                <span><code>ana.coord@colegio.edu</code> / <code>hash-coord</code></span>
              </div>
              <div className="item-card">
                <strong>Docente</strong>
                <span><code>carlos@colegio.edu</code> / <code>hash-docente</code></span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
