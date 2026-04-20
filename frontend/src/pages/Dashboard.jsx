/* Archivo documentado: Pantalla principal de la SPA. Consume la API y presenta una vista funcional del módulo correspondiente. */
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getDocentes } from '../api/usuario.api';
import { getTurnos } from '../api/turno.api';
import { getIncidentes } from '../api/incidente.api';
import { getReasignaciones } from '../api/reasignacion.api';
import { getRecorridos } from '../api/recorrido.api';
import { getReconocimientos } from '../api/reconocimiento.api';
import { Spinner } from '../components/Spinner';
import { getRole, ROLE_VIEW_LABELS } from '../roleConfig';

const DASHBOARD_CONFIG = {
  coordinador: {
    cards: [
      { key: 'docentes', label: 'Docentes activos', className: 'green' },
      { key: 'turnos', label: 'Turnos', className: 'blue' },
      { key: 'incidentes', label: 'Incidentes', className: 'orange' },
      { key: 'reasignaciones', label: 'Reasignaciones', className: 'purple' },
    ],
    panels: [
      {
        title: 'Gestión operativa',
        actions: [
          { to: '/turnos', label: 'Turnos' },
          { to: '/zonas', label: 'Cobertura' },
          { to: '/checkins', label: 'Check-ins' },
          { to: '/incidentes', label: 'Incidentes' },
          { to: '/reasignaciones', label: 'Reasignaciones' },
          { to: '/reconocimientos', label: 'Reconocimientos' },
          { to: '/reportes', label: 'Reportes' },
        ],
      },
      {
        title: 'Vista del coordinador',
        items: [
          { title: 'Tablero en vivo', text: 'Cobertura por zonas, alertas y check-ins.' },
          { title: 'Supervisión operativa', text: 'Incidentes, recorridos, reasignaciones y mapas de calor.' },
        ],
      },
    ],
  },
  docente: {
    cards: [
      { key: 'turnos', label: 'Turnos asignados', className: 'blue' },
      { key: 'recorridos', label: 'Recorridos', className: 'green' },
      { key: 'incidentes', label: 'Reportes', className: 'orange' },
      { key: 'reconocimientos', label: 'Reconocimientos', className: 'purple' },
    ],
    panels: [
      {
        title: 'Mi jornada',
        actions: [
          { to: '/turnos', label: 'Mis turnos' },
          { to: '/checkins', label: 'Check-in QR o PIN' },
          { to: '/recorridos', label: 'Recorridos' },
          { to: '/incidentes', label: 'Reportar incidente' },
          { to: '/reasignaciones', label: 'Solicitar reasignación' },
          { to: '/limpiezas', label: 'Registrar limpieza' },
        ],
      },
      {
        title: 'Vista del docente',
        items: [
          { title: 'Opera su turno', text: 'Check-in, recorridos, incidentes y limpieza.' },
          { title: 'Sigue su desempeño', text: 'Notificaciones, métricas y reconocimientos.' },
        ],
      },
    ],
  },
  administrador: {
    cards: [
      { key: 'docentes', label: 'Usuarios operativos', className: 'green' },
      { key: 'turnos', label: 'Turnos cargados', className: 'blue' },
      { key: 'incidentes', label: 'Incidentes globales', className: 'orange' },
      { key: 'reasignaciones', label: 'Reasignaciones', className: 'purple' },
    ],
    panels: [
      {
        title: 'Administración estructural',
        actions: [
          { to: '/usuarios', label: 'Usuarios' },
          { to: '/turnos', label: 'Turnos' },
          { to: '/zonas', label: 'Zonas' },
          { to: '/checkpoints', label: 'Checkpoints' },
          { to: '/configuraciones', label: 'Configuración' },
        ],
      },
      {
        title: 'Vista del administrador',
        items: [
          { title: 'Configura el sistema', text: 'Usuarios, zonas, checkpoints y reglas operativas.' },
          { title: 'Administra la estructura', text: 'Turnos, incidencias globales, métricas y reportes.' },
        ],
      },
      {
        title: 'Analítica global',
        actions: [
          { to: '/mapas-calor', label: 'Mapas de calor' },
          { to: '/metricas', label: 'Métricas' },
          { to: '/incidentes', label: 'Incidentes' },
          { to: '/reasignaciones', label: 'Reasignaciones' },
          { to: '/notificaciones', label: 'Notificaciones' },
          { to: '/reportes', label: 'Reportes' },
        ],
      },
    ],
  },
};

export function Dashboard() {
  const role = getRole();
  const [counts, setCounts] = useState({
    docentes: 0,
    turnos: 0,
    incidentes: 0,
    reasignaciones: 0,
    recorridos: 0,
    reconocimientos: 0,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let active = true;

    Promise.all([
      getDocentes(),
      getTurnos(),
      getIncidentes(),
      getReasignaciones(),
      getRecorridos(),
      getReconocimientos(),
    ])
      .then(([docentes, turnos, incidentes, reasignaciones, recorridos, reconocimientos]) => {
        if (!active) return;
        setCounts({
          docentes: docentes.filter((item) => item.activo).length,
          turnos: turnos.length,
          incidentes: incidentes.length,
          reasignaciones: reasignaciones.length,
          recorridos: recorridos.length,
          reconocimientos: reconocimientos.length,
        });
      })
      .catch((e) => {
        if (!active) return;
        setError(e.message);
      })
      .finally(() => {
        if (active) setLoading(false);
      });

    return () => {
      active = false;
    };
  }, []);

  const config = DASHBOARD_CONFIG[role];

  return (
    <div>
      <div className="topbar">
        <div>
          <p className="eyebrow">Colegio San José · Supervisión escolar</p>
          <h1>Dashboard</h1>
          <p className="muted role-label">{ROLE_VIEW_LABELS[role]}</p>
        </div>
        <div className="topbar-actions">
          <Link className="ghost-button" to="/">Volver al acceso</Link>
        </div>
      </div>

      {error && <div className="alert error">{error}</div>}

      {loading ? (
        <Spinner />
      ) : (
        <>
          <div className="stats-grid">
            {config.cards.map((card) => (
              <div key={card.key} className={`metric-card ${card.className}`}>
                <strong>{counts[card.key]}</strong>
                <span>{card.label}</span>
              </div>
            ))}
          </div>

          <div className="panel-grid">
            {config.panels.slice(0, 2).map((panel) => (
              <article key={panel.title} className="panel">
                <div className="panel-title-row">
                  <h3>{panel.title}</h3>
                </div>
                {panel.actions ? (
                  <div className="quick-actions">
                    {panel.actions.map((action) => (
                      <Link key={action.to} className="action-chip" to={action.to}>
                        {action.label}
                      </Link>
                    ))}
                  </div>
                ) : (
                  <div className="stack-list">
                    {panel.items.map((item) => (
                      <div key={item.title} className="item-card">
                        <strong>{item.title}</strong>
                        <span>{item.text}</span>
                      </div>
                    ))}
                  </div>
                )}
              </article>
            ))}
          </div>

          {config.panels[2] && (
            <div className="panel-grid">
              <article className="panel">
                <div className="panel-title-row">
                  <h3>{config.panels[2].title}</h3>
                </div>
                <div className="quick-actions">
                  {config.panels[2].actions.map((action) => (
                    <Link key={action.to} className="action-chip" to={action.to}>
                      {action.label}
                    </Link>
                  ))}
                </div>
              </article>
            </div>
          )}
        </>
      )}
    </div>
  );
}
