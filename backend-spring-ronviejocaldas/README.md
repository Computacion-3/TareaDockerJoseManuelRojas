# Icesi Fit — Backend de Actividad Física

Backend desarrollado con **Spring Boot 3.4.4** para gestionar actividad física en la Universidad Icesi. Emplea **Spring Data JPA** con **Hibernate** para el consumo de datos desde una base de datos H2 en memoria.

## Tecnologías

| Tecnología | Versión |
|------------|---------|
| Java | 17 |
| Spring Boot | 3.4.4 |
| Spring Data JPA / Hibernate | 6.6.11 |
| Spring Security | 6 (JWT + Form Login) |
| H2 Database | En memoria |
| Lombok | 1.18.32 |
| MapStruct | 1.5.5.Final |
| JJWT | 0.12.6 |
| SpringDoc OpenAPI (Swagger) | 2.8.8 |
| JUnit 5 + Mockito | Incluidos en `spring-boot-starter-test` |
| JaCoCo | 0.8.12 |
| Maven Wrapper | Incluido (`mvnw`) |

## Estructura del Proyecto

```
GymApp/
├── src/main/java/com/icesi/fit/
│   ├── IcesiFitApplication.java
│   ├── ServletInitializer.java
│   ├── config/            # SecurityConfig (MVC), RestSecurityConfig (JWT), OpenApiConfig
│   ├── controller/        # Controladores REST (/api/**) y MVC (Thymeleaf)
│   ├── dto/               # Data Transfer Objects para request/response REST
│   ├── exception/         # ResourceNotFoundException y GlobalExceptionHandler
│   ├── mapper/            # Interfaces MapStruct (entityToDto / dtoToEntity)
│   ├── model/             # Entidades JPA (Usuario, Rol, Permiso, etc.)
│   ├── repository/        # Repositorios JPA
│   ├── security/          # JwtUtil y JwtFilter
│   ├── service/           # Servicios y lógica de negocio
│   └── util/              # Clases estáticas y de utilidad
├── src/main/resources/
│   ├── application.properties
│   ├── import.sql         # Datos y registros iniciales
│   ├── static/            # Archivos estáticos como el CSS de Icesi
│   └── templates/         # Vistas HTML Thymeleaf segmentadas (autenticación y administración)
├── src/test/java/com/icesi/fit/service/
│   ├── PermisoServiceTest.java
│   ├── RolServiceTest.java
│   └── UsuarioServiceTest.java
└── pom.xml
```

## Seguridad y Autenticación

El sistema implementa Spring Security para la protección de recursos y rutas:
- Formularios personalizados de `login` y `register`.
- Contraseñas encriptadas de forma segura (BCrypt).
- Autorización estricta basada en roles institucionales.
- Redirección automática controlada a las vistas correspondientes según el perfil del usuario autenticado.

### REST API — Autenticación JWT

Los endpoints REST (`/api/**`) utilizan autenticación stateless mediante JWT:
1. Obtener token: `POST /api/auth/login` con `{"correoInstitucional": "...", "password": "..."}`
2. Incluir en cada request: header `Authorization: Bearer <token>`
3. El token expira en 24 horas e incluye el rol del usuario.

## Vistas, Templates y Estilos

El frontend dinámico está acoplado de la siguiente manera:
- **Thymeleaf Fragments:** Reutilización de componentes como barras laterales y menús en un sistema de plantillas modulares ubicadas en `/templates/`.
- **Diseño UI / Marca Icesi:** Se diseñaron múltiples hojas de estilo CSS exclusivas que respetan la marca, interfaz limpia y la paleta de colores de la Universidad Icesi (abandonando la apariencia genérica de Bootstrap).

## Requisitos Previos

- **Java 17** o superior instalado
- **Git** (para clonar el repositorio)
- No se necesita instalar Maven — el proyecto incluye **Maven Wrapper** (`mvnw`)

## Cómo Ejecutar la Aplicación

### 1. Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd ProyectoComp2-Gym/GymApp
```

### 2. Ejecutar en modo desarrollo

```bash
# En Windows
.\mvnw.cmd spring-boot:run

# En Linux/Mac
./mvnw spring-boot:run
```

La aplicación se levantará en **http://localhost:8080**.

### 3. Acceder a los endpoints

> **Todos los endpoints REST requieren token JWT** (excepto `/api/auth/login`).
> Primero hacer login, luego incluir `Authorization: Bearer <token>` en cada request.

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/api/auth/login` | POST | Obtener token JWT |
| `/api/usuarios` | GET | Listar todos los usuarios |
| `/api/usuarios/{id}` | GET | Obtener usuario por ID |
| `/api/usuarios` | POST | Crear usuario |
| `/api/usuarios/{id}` | PUT | Actualizar usuario |
| `/api/usuarios/{id}` | DELETE | Eliminar usuario |
| `/api/usuarios/{id}/entrenador/{entrenadorId}` | PUT | Asignar entrenador |
| `/api/ejercicios` | GET/POST/PUT/DELETE | CRUD de ejercicios |
| `/api/rutinas` | GET/POST/PUT/DELETE | CRUD de rutinas |
| `/api/progresos` | GET/POST/PUT/DELETE | CRUD de progresos |
| `/api/notificaciones` | GET/POST/PUT/DELETE | CRUD de notificaciones |
| `/api/eventos` | GET/POST/PUT/DELETE | CRUD de eventos |
| `/api/recomendaciones` | GET/POST/PUT/DELETE | CRUD de recomendaciones |
| `/api/roles` | GET/POST/PUT/DELETE | CRUD de roles (solo ADMIN) |
| `/api/permisos` | GET/POST/PUT/DELETE | CRUD de permisos (solo ADMIN) |

### 4. Documentación interactiva — Swagger UI

Con la aplicación corriendo, abrir en el navegador:

```
http://localhost:8080/swagger-ui/index.html
```

Para probar endpoints protegidos desde Swagger:
1. Ejecutar `POST /api/auth/login` para obtener el token.
2. Clic en el botón **Authorize** (arriba a la derecha).
3. Ingresar `<token>` (sin el prefijo Bearer — Swagger lo agrega automáticamente).
4. Todos los requests siguientes incluirán el token.

### 5. Consola H2 (explorar la base de datos)

Acceder a: **http://localhost:8080/h2-console**

| Campo | Valor |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:icesifit` |
| User | `sa` |
| Password | *(dejar vacío)* |

## Cómo Probar la Aplicación

### Ejecutar pruebas Postman

**1. Importar archivos**

Importar los archivos de la carpeta `postman/` en Postman:
- `IcesiFit-Tests-V2.json` — colección completa con tests automatizados
- `IcesiFit-Local.postman_environment.json` — variables de entorno (baseUrl, token, IDs)

**2. Configurar environment**

En Postman:
- Ir a **Environments** (panel izquierdo)
- Seleccionar **IcesiFit - Local**
- Verificar que las variables estén configuradas:
  - `baseUrl`: `http://localhost:8080`
  - `adminEmail`: `admin@icesi.edu.co`
  - `adminPassword`: `admin123`

**3. Ejecutar pruebas**

- Abrir la colección **IcesiFit REST API**
- Clic en **Run** o usar **Collection Runner**
- Seleccionar el environment **IcesiFit - Local**
- Ejecutar en **orden secuencial** (importante: ejecutar 0. Autenticacion primero)

**4. Estructura de la colección**

La colección está organizada en carpetas que deben ejecutarse en orden:

| Carpeta | Descripción |
|--------|-------------|
| 0. Autenticacion | Login para obtener token JWT |
| 1. Permisos | CRUD de permisos (solo ADMIN) |
| 2. Roles | CRUD de roles (solo ADMIN) |
| 3. Usuarios | CRUD de usuarios (ADMIN/ENTRENADOR) |
| 4. Ejercicios | CRUD de ejercicios |
| 5. Rutinas | CRUD de rutinas |
| 6. Progresos | CRUD de progresos |
| 7. Recomendaciones | CRUD de recomendaciones (ADMIN/ENTRENADOR) |

**5. Usuarios de prueba**

| Email | Password | Rol |
|-------|----------|-----|
| admin@icesi.edu.co | admin123 | ADMIN |
| entrenador1@icesi.edu.co | admin123 | ENTRENADOR |
| estudiante1@icesi.edu.co | admin123 | ESTUDIANTE |

**6. Verificar resultados**

Después de ejecutar:
- Login debe retornar **200** y guardar el token automáticamente
- Los endpoints GET deben retornar **200**
- Los endpoints POST deben retornar **201**
- Los endpoints DELETE deben retornar **204**

### Ejecutar pruebas unitarias

```bash
.\mvnw.cmd test
```

Resultado esperado: **35 tests, 0 failures, 0 errors**

| Clase de Test | Tests | Descripción |
|---------------|-------|-------------|
| `UsuarioServiceTest` | 15 | CRUD + asignar entrenador + progreso de asignados |
| `RolServiceTest` | 11 | CRUD + validación "Evite Roles sin permisos" |
| `PermisoServiceTest` | 9 | CRUD + validación de nombre requerido |

### Verificar cobertura con JaCoCo

```bash
.\mvnw.cmd verify
```

- Genera reporte de cobertura en: `target/site/jacoco/index.html`
- Verifica que la cobertura de línea del paquete `service` sea ≥ 80%
- Resultado esperado: `All coverage checks have been met.`

### Generar el WAR para despliegue

```bash
.\mvnw.cmd clean package
```

El archivo WAR se genera en: `target/icesi-fit-0.0.1-SNAPSHOT.war`

## Despliegue SSH en Tomcat (IAsLab)

La aplicación fue configurada y desplegada remotamente conectándose mediante SSH a un servidor hospedado:

- **IP del Servidor:** `10.147.20.70`
- **Usuario SSH:** `x205m10`
- `computacion2`

**Pasos de despliegue:**
1. Generar el compilado WAR localmente: `.\mvnw.cmd clean package -DskipTests`
2. Transferir el archivo `target/icesi-fit-0.0.1-SNAPSHOT.war` al servidor remoto.
3. Copiarlo en el directorio `webapps/` de Tomcat en el servidor remoto.
4. Renombrarlo a `RonViejoCaldas-icesi-fit.war`.
5. Tomcat lo desplegará automáticamente y estará disponible en:
   - **Aplicación MVC:** `http://10.147.20.70:8080/RonViejoCaldas-icesi-fit/`
   - **Swagger UI:** `http://10.147.20.70:8080/RonViejoCaldas-icesi-fit/swagger-ui/index.html`
   - **API REST:** `http://10.147.20.70:8080/RonViejoCaldas-icesi-fit/api/`

## Modelo de Datos

### Entidades y Relaciones

- **Usuario** → `@ManyToOne` **Rol** (un usuario tiene un rol obligatorio)
- **Rol** ↔ **Permiso** (relación `@ManyToMany` vía tabla `rol_permiso`)
- **Usuario** → `@ManyToOne` **Usuario** (estudiante asignado a entrenador)
- **Rutina** ↔ **Ejercicio** (relación `@ManyToMany` vía `rutina_ejercicio` con atributos extra)
- **Usuario** ↔ **Evento** (relación `@ManyToMany` vía `usuario_evento` con fecha de inscripción)
- **Progreso**, **Notificacion**, **Recomendacion** → `@ManyToOne` **Usuario**

### Datos Iniciales (import.sql)

| Entidad | Cantidad |
|---------|----------|
| Permisos | 9 |
| Roles | 3 (ADMIN, ENTRENADOR, ESTUDIANTE) |
| Usuarios | 6 (1 admin, 2 entrenadores, 3 estudiantes) |
| Ejercicios | 5 |
| Rutinas | 2 (con 4 ejercicios asignados) |
| Progresos | 4 |
| Eventos | 1 (con 2 inscripciones) |
| Notificaciones | 3 |
| Recomendaciones | 2 |

## Reglas de Negocio

- **Evite usuarios sin Rol**: `UsuarioService.saveUsuario()` lanza excepción si el rol es nulo o no existe.
- **Evite Roles sin permisos**: `RolService.saveRol()` lanza excepción si la lista de permisos está vacía.
- **Validación de entrenador**: Solo usuarios con rol `ENTRENADOR` pueden ser asignados como entrenador.

## Integrantes

| Nombre | Correo |
|--------|--------|
 |Daniel Escobar | <daniellondono2710@gmail.com> |
| Jose Rojas | <josem.r2026@gmail.com> |
|Puerre Cuevas|<Pierreandrescue@gmail.com> |
