import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { HouseLogo } from '../components/HouseLogo';
import { setRole, setUser } from '../roleConfig';
import { getUsuarios } from '../api/usuario.api';

function RoleIcon({ kind }) {
  if (kind === 'docente') return <HouseLogo />;
  if (kind === 'administrador') return <HouseLogo />;
  return <HouseLogo />;
}

export function Login() {
  const navigate = useNavigate();

  const [selectedRole, setSelectedRole] = useState(null);
  const [usuarios, setUsuarios] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadUsuarios();
  }, []);

  async function loadUsuarios() {
    try {
      setLoading(true);
      const data = await getUsuarios();
      setUsuarios(data.filter((u) => u.activo));
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  }

  const handleSelectUser = (user) => {
  localStorage.clear(); // limpia usuario anterior

  setRole(selectedRole);
  setUser(user);

  navigate('/dashboard');
};

  const users = usuarios.filter(
    (u) => u.rol?.toLowerCase() === selectedRole
  );

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

        {!selectedRole ? (
          <div className="role-selector">
            <div className="section-heading">
              <h2>Selecciona tu rol</h2>
              <p>Elige cómo quieres acceder al sistema.</p>
            </div>

            <div className="role-grid">
              <button className="role-card green" onClick={() => setSelectedRole('coordinador')}>
                <div className="role-icon"><RoleIcon kind="coordinador" /></div>
                <span>Coordinador</span>
              </button>

              <button className="role-card blue" onClick={() => setSelectedRole('docente')}>
                <div className="role-icon"><RoleIcon kind="docente" /></div>
                <span>Docente</span>
              </button>

              <button className="role-card purple" onClick={() => setSelectedRole('administrador')}>
                <div className="role-icon"><RoleIcon kind="administrador" /></div>
                <span>Administrador</span>
              </button>
            </div>
          </div>
        ) : (
          <div className="role-selector">
            <div className="section-heading">
              <h2>Selecciona tu perfil</h2>
            </div>

            {loading ? (
              <p>Cargando...</p>
            ) : (
              <div className="role-grid">
                {users.map((user) => (
                  <button
                    key={user.id}
                    className="role-card blue"
                    onClick={() => handleSelectUser(user)}
                  >
                    <div className="role-icon">
                      <RoleIcon kind={selectedRole} />
                    </div>

                    <span>{user.nombre}</span>
                    <small>{user.email}</small>
                  </button>
                ))}
              </div>
            )}

            <div style={{ marginTop: '18px' }}>
              <button
                className="ghost-button"
                onClick={() => setSelectedRole(null)}
              >
                Volver
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}