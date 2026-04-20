# Sistema de Vigilancia Docente

Aplicación web para gestión de vigilancia docente con frontend SPA en React y backend API REST en Spring Boot.

## Estado actual

El proyecto ya no usa vistas Thymeleaf ni recursos estáticos renderizados por el backend.

- `frontend/` contiene la SPA React/Vite.
- `proyecto/demo/` contiene la API REST, el acceso a datos y la carga semilla.
- El backend entrega JSON y la SPA consume esos endpoints desde `http://localhost:8080/api`.

## Arquitectura

### Frontend

Ruta base: [frontend](/mnt/c/users/francisco%20guzman/onedrive/documentos/septimo/web/proyecto/frontend:1)

Estructura principal:

```text
frontend/
├── src/
│   ├── api/          # Clientes HTTP por recurso REST
│   ├── components/   # Componentes reutilizables
│   ├── hooks/        # Hooks compartidos
│   ├── pages/        # Pantallas principales de la SPA
│   ├── roleConfig.js # Permisos y navegación por rol
│   ├── App.jsx       # Rutas principales
│   └── main.jsx      # Punto de entrada
├── package.json
└── vite.config.js
```

### Backend

Ruta base: [proyecto/demo](/mnt/c/users/francisco%20guzman/onedrive/documentos/septimo/web/proyecto/proyecto/demo:1)

Estructura principal:

```text
proyecto/demo/
├── src/main/java/com/example/demo/
│   ├── config/                 # Configuración y carga inicial
│   ├── exception/              # Excepciones de dominio
│   ├── model/                  # Entidades y enums JPA
│   ├── repository/             # Repositorios Spring Data
│   ├── service/                # Lógica de negocio
│   └── web/api/
│       ├── controller/         # Endpoints REST
│       ├── dto/                # DTOs de salida
│       ├── handler/            # Manejo global de errores REST
│       ├── mapper/             # Conversión entre dominio y API
│       └── request/            # Payloads de entrada
├── src/main/resources/
│   ├── application.properties  # Configuración de Spring
│   └── schema.sql              # Esquema de base de datos
├── pom.xml
└── mvnw
```

## Tecnologías

- React 19
- Vite
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven Wrapper

## Módulos funcionales

- Gestión de usuarios y docentes
- Gestión de zonas y turnos
- Registro de check-ins
- Registro de incidentes
- Gestión de reasignaciones
- Registro de limpieza
- Gestión de notificaciones
- Gestión de recorridos y checkpoints
- Mapas de calor
- Métricas
- Reconocimientos
- Reporte resumen

## API REST

Base URL:

```text
http://localhost:8080/api
```

Recursos disponibles:

- `GET/POST /usuarios`
- `GET/PUT/DELETE /usuarios/{id}`
- `GET/POST /docentes`
- `GET/PUT/DELETE /docentes/{id}`
- `GET/POST /configuraciones`
- `GET/PUT/DELETE /configuraciones/{id}`
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

## Ejecución

### Backend

Desde [proyecto/demo](/mnt/c/users/francisco%20guzman/onedrive/documentos/septimo/web/proyecto/proyecto/demo:1):

```bash
./mvnw spring-boot:run
```

En PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

### Frontend

Desde [frontend](/mnt/c/users/francisco%20guzman/onedrive/documentos/septimo/web/proyecto/frontend:1):

```bash
npm install
npm run dev
```

Frontend por defecto:

```text
http://localhost:5173
```

## Variables y configuración

Frontend:

- `VITE_API_BASE_URL`
  Si no se define, usa `http://localhost:8080/api`.

Backend:

- La configuración de base de datos está en [application.properties](/mnt/c/users/francisco%20guzman/onedrive/documentos/septimo/web/proyecto/proyecto/demo/src/main/resources/application.properties:1).
- El esquema inicial está en [schema.sql](/mnt/c/users/francisco%20guzman/onedrive/documentos/septimo/web/proyecto/proyecto/demo/src/main/resources/schema.sql:1).
- La carga semilla se ejecuta con [DataLoader.java](/mnt/c/users/francisco%20guzman/onedrive/documentos/septimo/web/proyecto/proyecto/demo/src/main/java/com/example/demo/config/DataLoader.java:1).

## Pruebas manuales rápidas

### Navegador

Puedes verificar respuestas JSON con:

- `http://localhost:8080/api/usuarios`
- `http://localhost:8080/api/zonas`
- `http://localhost:8080/api/turnos`
- `http://localhost:8080/api/reportes/resumen`

### Curl

```bash
curl http://localhost:8080/api/usuarios
curl http://localhost:8080/api/zonas
curl http://localhost:8080/api/reportes/resumen
curl -X DELETE http://localhost:8080/api/zonas/1 -i
```

### Postman

Ejemplos:

- `GET http://localhost:8080/api/usuarios`
- `GET http://localhost:8080/api/zonas`
- `POST http://localhost:8080/api/zonas`
- `PUT http://localhost:8080/api/zonas/{id}`
- `DELETE http://localhost:8080/api/zonas/{id}`

Payload ejemplo para `POST /zonas`:

```json
{
  "nombre": "Zona Prueba",
  "descripcion": "Zona creada desde Postman",
  "ubicacion": "Bloque C",
  "capacidadMaxima": 50,
  "activa": true
}
```

## Convención de comentarios

El código fuente quedó documentado con comentarios de propósito por archivo para que sea más fácil identificar la responsabilidad de cada pieza sin saturar la lógica con ruido innecesario.

## Documentación complementaria

- [`CasosDeUSo.pdf`](./CasosDeUSo.pdf)
- [`Diagrama_Clases.pdf`](./Diagrama_Clases.pdf)
- [`Mockup`](https://ninth-grain-92242194.figma.site/login)
- [`Video`](https://youtu.be/pK5Q_UcGY0I?si=AmNmo-J5jtSWKzyN)
