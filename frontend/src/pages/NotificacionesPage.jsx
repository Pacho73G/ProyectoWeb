import { useEffect, useState } from 'react';
import {
  getUserNotifications,
  markAllAsRead,
} from '../api/notificacion.api';

import {
  getRole,
  getDocenteId,
  ROLE_VIEW_LABELS,
} from '../roleConfig';

export function NotificacionesPage() {
  const role = getRole();
  const docenteId = getDocenteId();

  const [items, setItems] = useState([]);

  useEffect(() => {
    loadNotifications();
  }, []);

  function loadNotifications() {
    const data = getUserNotifications(role, docenteId);
    setItems(data);
    markAllAsRead(role, docenteId);
  }

  return (
    <div>
      {/* TOPBAR */}
      <div className="topbar">
        <div>
          <p className="eyebrow">
            Colegio San José · Supervisión escolar
          </p>

          <h1>Notificaciones</h1>

          <p className="muted role-label">
            {ROLE_VIEW_LABELS[role]}
          </p>
        </div>
      </div>

      {/* PANEL */}
      <div className="panel">
        <div className="panel-intro">
          <h3>Centro de notificaciones</h3>
          <p>
            Consulta alertas, asignaciones y avisos del sistema
            según tu perfil.
          </p>
        </div>

        {/* LISTADO */}
        <div
          style={{
            display: 'grid',
            gap: '14px',
            marginTop: '20px',
          }}
        >
          {items.length === 0 && (
            <div className="empty-state">
              <h4>Sin novedades</h4>
              <p>No tienes notificaciones por ahora.</p>
            </div>
          )}

          {items.map((n) => (
            <div
              key={n.id}
              className="notification-card"
            >
              <div className="notification-icon">
                🔔
              </div>

              <div className="notification-content">
                <strong>{n.titulo}</strong>
                <p>{n.mensaje}</p>
                <small>{n.fecha}</small>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}