# Sistema de Vigilancia Docente

Primera entrega del proyecto de desarrollo web orientado a la supervisión escolar durante recreos, almuerzos y otros bloques de vigilancia institucional.

**Diseñado y desarrollado por:** Francisco Javier Guzman Villegas

## Descripción

Este proyecto propone una aplicación web para mejorar el control de presencia docente, el registro de recorridos, la trazabilidad de incidentes y la toma de decisiones a partir de información operativa y analítica.

La solución busca responder al problema de ausencias, retrasos, vigilancia pasiva, falta de registro oportuno e inexistencia de evidencia estructurada en los turnos de supervisión escolar.

## Alcance de Esta Primera Entrega

Esta entrega presenta:

- Aplicación multipágina (`MPA`) con diseño visual básico.
- Implementación de funcionalidades `CRUD` para las entidades y asociaciones principales del sistema.
- Script de estructura de base de datos.
- Programa `Batch` en Java Spring para la carga inicial de información.
- Navegación diferenciada por rol a nivel de experiencia de usuario.

## Usuarios del Sistema

- **Docente en turno**
  Registra check-in, recorridos, incidentes, limpieza y solicitudes de reasignación. También consulta turnos, métricas, reconocimientos y notificaciones.

- **Coordinador**
  Supervisa la operación, consulta incidentes, reasignaciones, mapas de calor, métricas, reportes y tablero general.

- **Administrador**
  Gestiona usuarios, zonas, checkpoints, turnos y configuración operativa del sistema.

## Funcionalidades Implementadas

### Módulo operativo

- Gestión de turnos.
- Registro de check-in por turno.
- Registro de incidentes.
- Solicitud y seguimiento de reasignaciones.
- Registro de limpieza al cierre del turno.
- Gestión de notificaciones.
- Registro de recorridos y checkpoints.

### Módulo analítico

- Mapas de calor.
- Métricas docentes.
- Reconocimientos.
- Reportes generales.

### Módulo administrativo

- Gestión de usuarios.
- Gestión de docentes, coordinadores y administradores.
- Gestión de zonas.
- Gestión de checkpoints.
- Configuración del sistema.

## Modelo de Datos

El sistema fue construido con base en un modelo persistente que incluye, entre otras, las siguientes entidades:

- `Usuario`
- `Docente`
- `Coordinador`
- `Administrador`
- `ConfiguracionSistema`
- `Zona`
- `Turno`
- `CheckIn`
- `Incidente`
- `Reasignacion`
- `RegistroLimpieza`
- `Notificacion`
- `Recorrido`
- `CheckpointRecorrido`
- `MapaCalor`
- `MetricaDocente`
- `Reconocimiento`

## Arquitectura del Proyecto

El proyecto está organizado en capas:

- `model`: entidades y enumeraciones del dominio
- `repository`: acceso a datos con Spring Data JPA
- `service`: reglas de negocio y operaciones del sistema
- `web`: controladores MVC y formularios
- `templates`: vistas Thymeleaf
- `static`: estilos y recursos visuales
- `config`: carga inicial, datos compartidos y configuración general

## Tecnologías Utilizadas

- Java 21
- Spring Boot 3
- Spring MVC
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- Maven
- HTML + CSS

## Base de Datos

La estructura de la base está definida en:

- [`schema.sql`](./proyecto/demo/src/main/resources/schema.sql)

La carga inicial de información se realiza mediante el batch:

- [`DataLoader.java`](./proyecto/demo/src/main/java/com/example/demo/config/DataLoader.java)

## Ejecución del Proyecto

Ubícate en la carpeta del proyecto Spring Boot:

```bash
cd proyecto/demo
```

Ejecuta la aplicación con:

```bash
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Luego abre:

```text
http://localhost:8080/
```

## Configuración de Base de Datos

Revisa el archivo:

- [`application.properties`](./proyecto/demo/src/main/resources/application.properties)

Debes tener una base PostgreSQL creada y accesible con las credenciales configuradas allí.

## Evidencias de la Entrega

### Video de sustentación

[`[AGREGAR_LINK_DEL_VIDEO_AQUI]`](https://youtu.be/pK5Q_UcGY0I?si=AmNmo-J5jtSWKzyN)

### Mockup de la aplicación

[`[AGREGAR_LINK_DEL_MOCKUP_AQUI]`](https://ninth-grain-92242194.figma.site/login)

## Diagramas y Documentación

### Casos de uso

Puedes abrir el documento desde:

- [`CasosDeUSo.pdf`](./CasosDeUSo.pdf)

### Diagrama de clases

Puedes abrir el documento desde:

- [`Diagrama_Clases.pdf`](./Diagrama_Clases.pdf)

## Estructura del Repositorio

```text
Proyecto/
├── README.md
├── Diagrama_Clase.html
└── proyecto/
    └── demo/
        ├── pom.xml
        ├── src/
        │   ├── main/
        │   │   ├── java/
        │   │   └── resources/
        │   └── test/
        └── mvnw / mvnw.cmd
```

## Observaciones

El proyecto fue construido con una orientación modular, separando la lógica de dominio, persistencia, servicios, controladores y vistas, con el objetivo de facilitar su evolución hacia entregas posteriores centradas en servicios REST, SPA y autenticación.
