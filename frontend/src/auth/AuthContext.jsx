import {
  createContext,
  useContext,
  useEffect,
  useMemo,
  useState,
} from 'react';
import { getCurrentSession, login as loginRequest } from '../api/auth.api';
import {
  clearAuthSession,
  getStoredSession,
  hasActiveSession,
  setAuthSession,
} from '../roleConfig';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [session, setSession] = useState(() => getStoredSession());
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let active = true;

    async function bootstrapSession() {
      if (!hasActiveSession()) {
        if (active) setLoading(false);
        return;
      }

      try {
        // Si ya hay token persistido, se reconstruye el perfil desde backend
        // para no confiar ciegamente en lo que quedó guardado en el navegador.
        const currentSession = await getCurrentSession();
        if (!active) return;
        const nextSession = setAuthSession(currentSession, { preserveToken: true });
        setSession(nextSession);
      } catch {
        if (!active) return;
        clearAuthSession();
        setSession(null);
      } finally {
        if (active) setLoading(false);
      }
    }

    bootstrapSession();

    const handleUnauthorized = () => {
      clearAuthSession();
      setSession(null);
      setLoading(false);
    };

    window.addEventListener('auth:unauthorized', handleUnauthorized);
    return () => {
      active = false;
      window.removeEventListener('auth:unauthorized', handleUnauthorized);
    };
  }, []);

  const value = useMemo(
    () => ({
      session,
      loading,
      isAuthenticated: Boolean(session?.token && session?.role),
      async login(credentials) {
        const authResponse = await loginRequest(credentials);
        const nextSession = setAuthSession(authResponse);
        setSession(nextSession);
        return nextSession;
      },
      logout() {
        clearAuthSession();
        setSession(null);
      },
    }),
    [loading, session]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe usarse dentro de AuthProvider');
  }
  return context;
}
