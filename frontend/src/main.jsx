/* Archivo documentado: Punto de entrada de la SPA React. Monta la aplicación sobre el contenedor raíz del documento HTML. */
import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './app.css';
import App from './App.jsx';
import './globals.css';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>
);
