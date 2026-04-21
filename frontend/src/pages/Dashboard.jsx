/* Dashboard corregido: dinámico por docente + limpio + sin duplicaciones */

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

/* ================= CONFIG ================= */

const DASHBOARD_CONFIG = {
  coordinador: {
    cards: [
      { key: 'docentes', label: 'Docentes activos', className: 'green' },
      { key: 'turnos', label: 'Turnos', className: 'blue' },
      { key: 'incidentes', label: 'Incidentes', className: 'orange' },
      { key: 'reasignaciones', label: 'Reasignaciones', className: 'purple' },
    ],
  },
  docente: {
    cards: [
      { key: 'turnos', label: 'Turnos asignados', className: 'blue' },
      { key: 'recorridos', label: 'Recorridos', className: 'green' },
      { key: 'incidentes', label: 'Reportes', className: 'orange' },
      { key: 'reconocimientos', label: 'Reconocimientos', className: 'purple' },
    ],
  },
  administrador: {
    cards: [
      { key: 'docentes', label: 'Usuarios operativos', className: 'green' },
      { key: 'turnos', label: 'Turnos cargados', className: 'blue' },
      { key: 'incidentes', label: 'Incidentes globales', className: 'orange' },
      { key: 'reasignaciones', label: 'Reasignaciones', className: 'purple' },
    ],
  },
};

/* ================= COMPONENT ================= */

export function Dashboard() {
  const role = getRole();
  const docenteId = localStorage.getItem('docenteId');

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

  /* ================= DATA LOAD ================= */

  useEffect(() => {
    let active = true;

    async function loadData() {
      try {
        const [
          docentes,
          turnos,
          incidentes,
          reasignaciones,
          recorridos,
          reconocimientos,
        ] = await Promise.all([
          getDocentes(),
          getTurnos(),
          getIncidentes(),
          getReasignaciones(),
          getRecorridos(),
          getReconocimientos(),
        ]);

        if (!active) return;

        // 🔥 FILTRO CLAVE PARA DOCENTE
        const filtroDocente = (data) =>
          role === 'docente'
            ? data.filter((x) => String(x.docenteId) === String(docenteId))
            : data;

        setCounts({
          docentes: docentes.filter((d) => d.activo).length,
          turnos: filtroDocente(turnos).length,
          incidentes: filtroDocente(incidentes).length,
          reasignaciones: filtroDocente(reasignaciones).length,
          recorridos: filtroDocente(recorridos).length,
          reconocimientos: filtroDocente(reconocimientos).length,
        });
      } catch (e) {
        setError(e.message);
      } finally {
        setLoading(false);
      }
    }

    loadData();

    return () => {
      active = false;
    };
  }, [role, docenteId]);

  const config = DASHBOARD_CONFIG[role];

  /* ================= UI ================= */

  return (
    <div>

      {/* HEADER */}
      <div className="topbar">
        <div>
          <p className="eyebrow">Colegio San José · Supervisión escolar</p>
          <h1>Dashboard</h1>
          <p className="muted role-label">{ROLE_VIEW_LABELS[role]}</p>
        </div>

        <div className="topbar-actions">
          <Link className="ghost-button" to="/">
            Volver al acceso
          </Link>
        </div>
      </div>

      {error && <div className="alert error">{error}</div>}

      {loading ? (
        <Spinner />
      ) : (
        <>
          {/* CARDS */}
          <div className="stats-grid">
            {config.cards.map((card) => (
              <div key={card.key} className={`metric-card ${card.className}`}>
                <strong>{counts[card.key]}</strong>
                <span>{card.label}</span>
              </div>
            ))}
          </div>
        </>
      )}
    </div>
  );
}