# Sistema de Vigilancia Docente

Aplicación web para la supervisión operativa de docentes en turnos, recorridos, incidentes, limpiezas y notificaciones.

El proyecto está dividido en:
- `frontend/`: SPA en React + Vite.
- `proyecto/demo/`: API REST en Spring Boot + JPA + PostgreSQL.

## Estado actual

La versión actual ya incluye:
- notificaciones reales persistidas en base de datos,
- asignación de turnos con aviso al docente,
- alertas automáticas por ausencia de turno,
- registro de incidentes con o sin turno,
- recorridos con notificación a coordinación y administración,
- limpiezas asignables por docente y zona, incluso sin turno,
- flujo de limpieza separado entre `admin` y `docente`.

## Arquitectura

### Frontend

Ruta base: [frontend](/mnt/c/Users/Francisco%20Guzman/OneDrive/Documentos/Septimo/Web/Proyecto/frontend:1)

```text
frontend/
├── src/
│   ├── api/           # Clientes HTTP por recurso
│   ├── components/    # Sidebar, tabla, modal, badges, etc.
│   ├── hooks/         # Hooks compartidos como useApi
│   ├── pages/         # Vistas principales
│   ├── pages/forms/   # Formularios CRUD
│   ├── roleConfig.js  # Navegación y permisos por rol
│   ├── App.jsx        # Ruteo principal
│   └── main.jsx       # Punto de entrada
├── package.json
└── vite.config.js
```

### Backend

Ruta base: [proyecto/demo](/mnt/c/Users/Francisco%20Guzman/OneDrive/Documentos/Septimo/Web/Proyecto/proyecto/demo:1)

```text
proyecto/demo/
├── src/main/java/com/example/demo/
│   ├── config/                  # Configuración y datos semilla
│   ├── exception/               # Excepciones de dominio
│   ├── model/                   # Entidades JPA y enums
│   ├── repository/              # Repositorios Spring Data
│   ├── service/                 # Lógica de negocio
│   └── web/api/
│       ├── controller/          # Endpoints REST
│       ├── dto/                 # DTOs de salida
│       ├── handler/             # Manejador global de errores
│       ├── mapper/              # Mappers entidad <-> API
│       └── request/             # Payloads de entrada
├── src/main/resources/
│   ├── application.properties
│   └── schema.sql
├── pom.xml
└── mvnw
```

## Tecnologías

- React 19
- Vite
- Spring Boot 3.3
- Spring Web
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven Wrapper

## Cambios implementados

### 1. Notificaciones persistentes

Antes las notificaciones estaban simuladas con `localStorage`. Ahora se guardan en base de datos y se comparten entre usuarios reales.

Cambios principales:
- nueva lógica central en [NotificacionManagementService.java](/mnt/c/Users/Francisco%20Guzman/OneDrive/Documentos/Septimo/Web/Proyecto/proyecto/demo/src/main/java/com/example/demo/service/NotificacionManagementService.java:1),
- la entidad `Notificacion` ahora tiene `destinatario` y `titulo`,
- la página de notificaciones consulta la API real,
- el contador del sidebar consulta no leídas reales.

Se agregaron estos flujos automáticos:
- al crear un turno: notificación al docente asignado,
- al iniciar turno: aviso al docente, coordinadores y administradores,
- al cerrar turno: aviso al docente, coordinadores y administradores,
- al registrar incidente: aviso a coordinadores y administradores,
- al registrar recorrido: aviso a coordinadores y administradores,
- al crear reasignación: aviso a coordinadores y administradores,
- al responder una reasignación: aviso al docente solicitante,
- al asignar una limpieza: aviso al docente asignado,
- al detectar turno vencido sin check-in: alerta de ausencia.

### 2. Incidentes con o sin turno

El módulo de incidentes ya no obliga a que todo reporte pertenezca a un turno.

Ahora un docente puede:
- registrar un incidente durante un turno,
- o registrar un incidente fuera de turno, indicando zona y fecha.

Cambios:
- `turno_id` en `incidentes` pasó a ser opcional,
- el formulario permite `Turno` vacío,
- la tabla muestra si fue `Sin turno` o asociado a una franja.

### 3. Limpiezas asignadas

El módulo de limpieza cambió de ser solo “cierre de turno” a un flujo de asignación real.

Ahora una limpieza puede:
- estar asociada a un turno,
- o existir sin turno,
- pero siempre queda asociada a un `docente` y una `zona`.

Nuevos campos:
- `docente_id`
- `zona_id`
- `asignada_en`
- `completada`

Reglas funcionales:
- `admin` crea y asigna limpiezas,
- `docente` solo ve sus limpiezas asignadas,
- `docente` solo puede completar las pendientes,
- si una limpieza ya está `COMPLETADA`, al docente ya no le salen acciones.

### 4. Ajustes de permisos y navegación

Cambios por rol:

- `administrador`
  - puede gestionar limpiezas,
  - ve la opción `Limpieza` en el menú,
  - crea y edita asignaciones.

- `docente`
  - no crea limpiezas nuevas,
  - solo completa las asignadas,
  - ve `Completar` únicamente cuando la limpieza está pendiente.

### 5. Alineación del modelo JPA

Se corrigió la relación entre `Turno` y `RegistroLimpieza`:
- `Turno.registroLimpieza` mantiene `@OneToOne`,
- `RegistroLimpieza.turno` volvió a `@OneToOne`,
- el turno sigue siendo opcional para limpieza, pero si existe mantiene unicidad.

## Módulos funcionales

- Usuarios
- Docentes
- Turnos
- Zonas
- Check-ins
- Incidentes
- Reasignaciones
- Limpiezas
- Notificaciones
- Recorridos
- Checkpoints
- Mapas de calor
- Métricas
- Reconocimientos
- Reporte resumen

## Flujo por rol

### Administrador

- gestiona usuarios, turnos, zonas, checkpoints y configuración,
- asigna limpiezas,
- consulta incidentes, recorridos, reportes y notificaciones,
- recibe avisos operativos.

### Docente

- consulta sus turnos,
- inicia y finaliza turno,
- registra incidentes,
- registra recorridos,
- crea solicitudes de reasignación,
- consulta y completa limpiezas asignadas,
- recibe sus notificaciones personales.

### Coordinador

- consulta operación general,
- ve incidencias, recorridos, reportes y notificaciones,
- recibe alertas por incidentes, recorridos, reasignaciones y ausencias.

## API REST

Base URL:

```text
http://localhost:8080/api
```

Recursos principales:
- `GET/POST /usuarios`
- `GET/PUT/DELETE /usuarios/{id}`
- `GET/POST /docentes`
- `GET/PUT/DELETE /docentes/{id}`
- `GET/POST /zonas`
- `GET/PUT/DELETE /zonas/{id}`
- `GET/POST /turnos`
- `GET/PUT/DELETE /turnos/{id}`
- `GET/POST /checkins`
- `GET/PUT/DELETE /checkins/{id}`
- `GET/POST /incidentes`
- `GET/PUT/DELETE /incidentes/{id}`
- `GET/POST /reasignaciones`
- `GET/PUT/DELETE /reasignaciones/{id}`
- `GET/POST /limpiezas`
- `GET/PUT/DELETE /limpiezas/{id}`
- `GET /notificaciones?userId={id}`
- `GET /notificaciones/unread-count?userId={id}`
- `PUT /notificaciones/mark-read?userId={id}`
- `GET/POST /notificaciones`
- `GET/PUT/DELETE /notificaciones/{id}`
- `GET/POST /recorridos`
- `GET/PUT/DELETE /recorridos/{id}`
- `GET/POST /checkpoints`
- `GET/PUT/DELETE /checkpoints/{id}`
- `GET/POST /mapas-calor`
- `GET/PUT/DELETE /mapas-calor/{id}`
- `GET/POST /metricas`
- `GET/PUT/DELETE /metricas/{id}`
- `GET/POST /reconocimientos`
- `GET/PUT/DELETE /reconocimientos/{id}`
- `GET /reportes/resumen`

## Base de datos

Archivo de esquema: [schema.sql](/mnt/c/Users/Francisco%20Guzman/OneDrive/Documentos/Septimo/Web/Proyecto/proyecto/demo/src/main/resources/schema.sql:1)

Aspectos importantes del esquema actual:
- `notificaciones` ahora soporta destinatario y título,
- `incidentes.turno_id` es nullable,
- `notificaciones.turno_id` es nullable,
- `registros_limpieza` ahora soporta:
  - `turno_id` opcional,
  - `docente_id` obligatorio,
  - `zona_id` obligatorio,
  - `asignada_en`,
  - `completada`.

## Ejecución local

### Backend

Desde [proyecto/demo](/mnt/c/Users/Francisco%20Guzman/OneDrive/Documentos/Septimo/Web/Proyecto/proyecto/demo:1):

```bash
./mvnw spring-boot:run
```

En PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

### Frontend

Desde [frontend](/mnt/c/Users/Francisco%20Guzman/OneDrive/Documentos/Septimo/Web/Proyecto/frontend:1):

```bash
npm install
npm run dev
```

Aplicación:

```text
http://localhost:5173
```

## Configuración

### Frontend

- `VITE_API_BASE_URL`
  Si no se define, usa `http://localhost:8080/api`.

### Backend

Configuración principal en [application.properties](/mnt/c/Users/Francisco%20Guzman/OneDrive/Documentos/Septimo/Web/Proyecto/proyecto/demo/src/main/resources/application.properties:1).

Actualmente usa:
- PostgreSQL local,
- inicialización por `schema.sql`,
- `app.seed.enabled=true` para cargar datos base.

## Datos semilla

La carga inicial se realiza desde [DataLoader.java](/mnt/c/Users/Francisco%20Guzman/OneDrive/Documentos/Septimo/Web/Proyecto/proyecto/demo/src/main/java/com/example/demo/config/DataLoader.java:1).

Incluye ejemplos de:
- administrador,
- coordinador,
- docentes,
- turnos,
- incidente,
- reasignación,
- limpieza,
- recorrido,
- notificación,
- métricas y reconocimiento.

## Casos de prueba manual

### Notificaciones

1. Crear un turno desde admin.
2. Verificar que el docente asignado reciba notificación.
3. Iniciar turno desde docente.
4. Revisar notificación de check-in en coordinador/admin.
5. Dejar vencer un turno pendiente y consultar notificaciones.

### Incidentes

1. Crear incidente con turno.
2. Crear incidente sin turno.
3. Verificar que ambos aparezcan en la tabla.

### Limpiezas

1. Entrar como admin.
2. Ir a `Limpieza`.
3. Crear una limpieza con docente y zona.
4. Opcionalmente dejar `turno` vacío.
5. Entrar como docente asignado.
6. Verificar que aparezca la limpieza.
7. Completarla.
8. Confirmar que ya no aparezca acción si quedó `COMPLETADA`.

## Verificación realizada

Frontend:

```bash
npm run build
```

Resultado:
- compilación correcta de la SPA.

Backend:
- el arranque y las correcciones SQL se ajustaron para que `schema.sql` sea compatible con el inicializador de Spring Boot,
- la validación final del backend depende de tener Java y PostgreSQL disponibles en la máquina local.

## Documentación complementaria

- [`CasosDeUSo.pdf`](./CasosDeUSo.pdf)
- [`Diagrama_Clases.pdf`](./Diagrama_Clases.pdf)
- [`Mockup`](https://ninth-grain-92242194.figma.site/login)
- [`Video`](https://youtu.be/pK5Q_UcGY0I?si=AmNmo-J5jtSWKzyN)
