/* Pantalla de selección de docente */

import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getDocentes } from '../api/usuario.api';
import { HouseLogo } from '../components/HouseLogo';
import { Spinner } from '../components/Spinner';

function DocenteAvatar({ nombre }) {
  const initials = nombre
    ? nombre
        .split(' ')
        .slice(0, 2)
        .map((w) => w[0])
        .join('')
        .toUpperCase()
    : 'DO';

  return <div className="docente-avatar">{initials}</div>;
}

export function DocenteSelector() {
  const navigate = useNavigate();

  const [docentes, setDocentes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function loadDocentes() {
      try {
        const data = await getDocentes();
        // Solo activos
        setDocentes(data.filter((d) => d.activo));
      } catch (e) {
        setError('Error cargando docentes');
      } finally {
        setLoading(false);
      }
    }

    loadDocentes();
  }, []);

  // ✅ FUNCIÓN ÚNICA Y CORRECTA
  const handleSelect = (docente) => {
    localStorage.setItem('docenteId', String(docente.id));
    localStorage.setItem('docenteNombre', docente.nombre);
    localStorage.setItem('docenteEmail', docente.email || '');

    navigate('/dashboard');
  };

  const goBack = () => {
    localStorage.removeItem('rol');
    navigate('/');
  };

  return (
    <div className="login-page">
      <div className="login-card">
        {/* HEADER */}
        <div className="hero">
          <div className="hero-icon">
            <HouseLogo />
          </div>
          <h1>Sistema de Vigilancia Docente</h1>
          <p>Colegio San José · Selecciona tu perfil</p>
        </div>

        {/* CONTENIDO */}
        <div className="role-selector">
          <div className="section-heading">
            <h2>Selecciona tu docente</h2>
            <p>Elige el perfil con el que deseas ingresar al sistema.</p>
          </div>

          {error && <div className="alert error">{error}</div>}

          {loading ? (
            <Spinner />
          ) : (
            <div className="docente-grid">
              {docentes.map((docente) => (
                <div
                  key={docente.id}
                  className="docente-card"
                  onClick={() => handleSelect(docente)}
                >
                  {/* ICONO */}
                  <div className="docente-icon">
                    <HouseLogo />
                  </div>

                  {/* INFO */}
                  <h3>{docente.nombre}</h3>
                  <p>{docente.email || 'Sin correo'}</p>
                </div>
              ))}
            </div>
          )}

          {/* BOTÓN VOLVER */}
          <div style={{ textAlign: 'center', marginTop: '28px' }}>
            <button
              type="button"
              className="ghost-button"
              onClick={goBack}
            >
              ← Volver a selección de rol
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}