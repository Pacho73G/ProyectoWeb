/* Archivo documentado: Pantalla principal de la SPA. Consume la API y presenta una vista funcional del módulo correspondiente. */
import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Spinner } from '../components/Spinner';
import { getReporteResumen } from '../api/reporte.api';
import { getRole, ROLE_VIEW_LABELS } from '../roleConfig';
import { useAuth } from '../auth/AuthContext';

export function ReportesPage() {
  const role = getRole();
  const { logout } = useAuth();
  const navigate = useNavigate();
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    let active = true;

    getReporteResumen()
      .then((response) => {
        if (active) setData(response);
      })
      .catch((e) => {
        if (active) setError(e.message);
      });

    return () => {
      active = false;
    };
  }, []);

  return (
    <div>
      <div className="topbar">
        <div>
          <p className="eyebrow">Colegio San José · Supervisión escolar</p>
          <h1>Reportes</h1>
          <p className="muted role-label">{ROLE_VIEW_LABELS[role]}</p>
        </div>
        <div className="topbar-actions">
          <button className="ghost-button" onClick={() => { logout(); navigate('/'); }}>Volver al acceso</button>
        </div>
      </div>

      {error && <div className="alert error">{error}</div>}

      {!data ? (
        <Spinner />
      ) : (
        <>
          <div className="stats-grid">
            <div className="metric-card blue">
              <strong>{data.totalTurnos}</strong>
              <span>Turnos reportados</span>
            </div>
            <div className="metric-card orange">
              <strong>{data.totalIncidentes}</strong>
              <span>Incidentes</span>
            </div>
            <div className="metric-card purple">
              <strong>{data.totalReasignaciones}</strong>
              <span>Reasignaciones</span>
            </div>
            <div className="metric-card green">
              <strong>{data.totalRecorridos}</strong>
              <span>Recorridos</span>
            </div>
          </div>

          <div className="panel-grid">
            <article className="panel">
              <div className="panel-title-row">
                <h3>Resumen operativo</h3>
              </div>
              <div className="stack-list">
                <div className="item-card">
                  <strong>Cobertura de turnos</strong>
                  <span>{data.totalTurnos} turnos con seguimiento operativo</span>
                </div>
                <div className="item-card">
                  <strong>Atención de incidentes</strong>
                  <span>{data.totalIncidentes} registros consolidados</span>
                </div>
                <div className="item-card">
                  <strong>Movilidad del personal</strong>
                  <span>{data.totalReasignaciones} reasignaciones tramitadas</span>
                </div>
              </div>
            </article>

            <article className="panel">
              <div className="panel-title-row">
                <h3>Accesos rápidos</h3>
              </div>
              <div className="quick-actions">
                <Link className="action-chip" to="/turnos">Ver turnos</Link>
                <Link className="action-chip" to="/incidentes">Ver incidentes</Link>
                <Link className="action-chip" to="/reasignaciones">Ver reasignaciones</Link>
                <Link className="action-chip" to="/mapas-calor">Ver mapas de calor</Link>
                <Link className="action-chip" to="/metricas">Ver métricas</Link>
              </div>
            </article>
          </div>
        </>
      )}
    </div>
  );
}