import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
  Outlet,
  useLocation,
} from 'react-router-dom';

import { AuthProvider, useAuth } from './auth/AuthContext';
import { Sidebar } from './components/Sidebar';
import { SidebarProvider } from './pages/SidebarContext';

import { canAccessPath, getRole } from './roleConfig';

import { Login } from './pages/Login';
import { Dashboard } from './pages/Dashboard';
import { TurnosPage } from './pages/TurnosPage';
import { ZonasPage } from './pages/ZonasPage';
import { IncidentesPage } from './pages/IncidentesPage';
import { CheckInsPage } from './pages/CheckInsPage';
import { ReasignacionesPage } from './pages/ReasignacionesPage';
import { LimpiezasPage } from './pages/LimpiezasPage';
import { NotificacionesPage } from './pages/NotificacionesPage';
import { MapasCalorPage } from './pages/MapasCalorPage';
import { MetricasPage } from './pages/MetricasPage';
import { ReconocimientosPage } from './pages/ReconocimientosPage';
import { RecorridosPage } from './pages/RecorridosPage';
import { CheckpointsPage } from './pages/CheckpointsPage';
import { UsuariosPage } from './pages/UsuariosPage';
import { ConfiguracionesPage } from './pages/ConfiguracionesPage';
import { ReportesPage } from './pages/ReportesPage';

/* ================= LAYOUT INTERNO ================= */

function LayoutContent() {
  return (
    <div className="app-shell">
      <Sidebar />
      <main className="content">
        <Outlet />
      </main>
    </div>
  );
}

/* ================= LAYOUT PRINCIPAL ================= */

function AppLayout() {
  return (
    <SidebarProvider>
      <LayoutContent />
    </SidebarProvider>
  );
}

/* ================= PROTECTED ================= */

function ProtectedRoute({ children }) {
  const { isAuthenticated, loading } = useAuth();
  const role = getRole();
  const location = useLocation();

  if (loading) {
    return null;
  }

  if (!isAuthenticated || !role) return <Navigate to="/" replace />;

  if (!canAccessPath(role, location.pathname)) {
    return <Navigate to="/dashboard" replace />;
  }

  return children;
}

function RootRedirect() {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return null;
  }

  return <Navigate to={isAuthenticated ? '/dashboard' : '/'} replace />;
}

/* ================= APP ================= */

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Login />} />

          <Route element={<AppLayout />}>
            <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
            <Route path="/turnos" element={<ProtectedRoute><TurnosPage /></ProtectedRoute>} />
            <Route path="/zonas" element={<ProtectedRoute><ZonasPage /></ProtectedRoute>} />
            <Route path="/incidentes" element={<ProtectedRoute><IncidentesPage /></ProtectedRoute>} />
            <Route path="/checkins" element={<ProtectedRoute><CheckInsPage /></ProtectedRoute>} />
            <Route path="/reasignaciones" element={<ProtectedRoute><ReasignacionesPage /></ProtectedRoute>} />
            <Route path="/limpiezas" element={<ProtectedRoute><LimpiezasPage /></ProtectedRoute>} />
            <Route path="/notificaciones" element={<ProtectedRoute><NotificacionesPage /></ProtectedRoute>} />
            <Route path="/mapas-calor" element={<ProtectedRoute><MapasCalorPage /></ProtectedRoute>} />
            <Route path="/metricas" element={<ProtectedRoute><MetricasPage /></ProtectedRoute>} />
            <Route path="/reconocimientos" element={<ProtectedRoute><ReconocimientosPage /></ProtectedRoute>} />
            <Route path="/recorridos" element={<ProtectedRoute><RecorridosPage /></ProtectedRoute>} />
            <Route path="/checkpoints" element={<ProtectedRoute><CheckpointsPage /></ProtectedRoute>} />
            <Route path="/usuarios" element={<ProtectedRoute><UsuariosPage /></ProtectedRoute>} />
            <Route path="/configuraciones" element={<ProtectedRoute><ConfiguracionesPage /></ProtectedRoute>} />
            <Route path="/reportes" element={<ProtectedRoute><ReportesPage /></ProtectedRoute>} />
          </Route>

          <Route path="*" element={<RootRedirect />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
