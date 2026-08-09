# Icesi Fit — Contenerización con Docker y Docker Compose

Proyecto final de **Computación en Internet II** (Spring Boot + React) migrado a una arquitectura de contenedores con **Docker** y **Docker Compose**.

La solución levanta **tres servicios**:

| Servicio  | Tecnología                                   | Descripción                                   |
|-----------|----------------------------------------------|-----------------------------------------------|
| `db`      | PostgreSQL 16                                | Base de datos relacional del sistema          |
| `backend` | Spring Boot 3.4.4 (Java 17, empaquetado WAR) | API REST + WebSocket + JWT                    |
| `frontend`| React 19 + Vite 8 (TypeScript)               | Interfaz de usuario SPA                       |

---

## Estructura del repositorio

```
proyectoFinalCompu2/
├── backend-spring-ronviejocaldas/
│   └── GymApp/
│       ├── Dockerfile          # Imagen del backend (una sola etapa)
│       ├── .dockerignore
│       ├── pom.xml             # Incluye driver PostgreSQL
│       └── src/                # Código fuente Spring Boot
├── frontend-react-ronviejocaldas/
│   ├── Dockerfile              # Imagen del frontend (una sola etapa)
│   ├── .dockerignore
│   └── src/                    # Código fuente React (Vite)
├── docker-compose.yml          # Orquestación de los 3 servicios
├── .env.example                # Plantilla de variables de entorno
└── .env                        # Variables de entorno (NO versionar)
```

---

## Requisitos previos

- **Docker** instalado (con Docker Engine o Docker Desktop).
- **Docker Compose** (incluido en Docker Desktop / `docker-compose-plugin`).

Verificar:

```bash
docker --version
docker compose version
```

---

## 1. Configurar las variables de entorno

Copie el archivo de ejemplo y ajuste los valores según su entorno:

```bash
cp .env.example .env
```

| Variable              | Descripción                                              | Valor por defecto                       |
|-----------------------|----------------------------------------------------------|-----------------------------------------|
| `POSTGRES_DB`         | Nombre de la base de datos                               | `icesifit`                              |
| `POSTGRES_USER`       | Usuario de PostgreSQL                                    | `icesifit`                              |
| `POSTGRES_PASSWORD`   | Contraseña de PostgreSQL                                 | `icesifit123`                           |
| `BACKEND_PORT`        | Puerto en el host para el backend                        | `8080`                                  |
| `FRONTEND_PORT`       | Puerto en el host para el frontend                       | `8081`                                  |
| `JWT_SECRET`          | Clave secreta para firmar los tokens JWT                 | (valor de ejemplo)                      |
| `JWT_EXPIRATION`      | Expiración del token en milisegundos (24 h)              | `86400000`                              |
| `CORS_ALLOWED_ORIGINS`| Orígenes permitidos por CORS (separados por coma)        | `http://localhost:8081`                 |
| `VITE_API_URL`        | URL pública de la API (se inyecta al compilar el front)  | `http://localhost:8080/api`             |
| `VITE_WS_URL`         | URL pública del WebSocket                                | `http://localhost:8080/ws`              |
| `VITE_BASE`           | Ruta base de la SPA                                      | `/`                                     |

> **Importante:** `VITE_API_URL`, `VITE_WS_URL` y `VITE_BASE` se resuelven en **tiempo de build** del frontend. Si modifica `.env`, ejecute `docker compose build frontend` para recompilar.

---

## 2. Construir las imágenes

```bash
docker compose build
```

También puede construir cada servicio por separado:

```bash
docker compose build backend
docker compose build frontend
```

---

## 3. Levantar la aplicación

```bash
docker compose up -d
```

Ver el estado de los servicios:

```bash
docker compose ps
```

Ver los logs en tiempo real:

```bash
docker compose logs -f
```

---

## 4. Acceder a los servicios

| Servicio  | URL                                             |
|-----------|-------------------------------------------------|
| Frontend  | http://localhost:8081                          |
| API REST  | http://localhost:8080/api                      |
| Swagger   | http://localhost:8080/swagger-ui/index.html    |
| PostgreSQL| Solo accesible desde la red interna de Docker (`db:5432`) |

### Usuarios de prueba (creados por `import.sql`)

| Correo                  | Contraseña | Rol          |
|-------------------------|------------|--------------|
| admin@icesi.edu.co      | admin123   | ADMIN        |
| entrenador1@icesi.edu.co| admin123   | ENTRENADOR   |
| estudiante1@icesi.edu.co| admin123   | ESTUDIANTE   |

> Si la contraseña `admin123` no funcionara en PostgreSQL, ejecute el siguiente comando para regenerar los hashes BCrypt (ver `GymApp/hash_gen.py`) y actualícelos en `src/main/resources/import.sql`.

### Probar la API

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"correoInstitucional":"admin@icesi.edu.co","password":"admin123"}'
```

La respuesta incluye el token JWT. Para las rutas protegidas:

```bash
curl http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer <token>"
```

### Nota sobre CORS

El frontend y el backend están en puertos distintos, por lo que la API responde con cabeceras CORS para el origen configurado en `CORS_ALLOWED_ORIGINS` (por defecto `http://localhost:8081`). Si accede al frontend desde otra IP/puerto, ajuste esa variable y reinicie el backend.

---

## 5. Detener y limpiar

```bash
# Detener los servicios
docker compose down

# Detener y eliminar también los volúmenes (borra la base de datos)
docker compose down -v
```

---

## Cómo funciona el backend con PostgreSQL

- El `pom.xml` incluye el driver `org.postgresql:postgresql` (scope `runtime`) junto con H2.
- `application.properties` lee la configuración desde variables de entorno con valores por defecto:

  ```properties
  spring.datasource.url=${DB_URL:jdbc:h2:mem:icesifit}
  spring.datasource.username=${DB_USERNAME:sa}
  spring.datasource.password=${DB_PASSWORD:}
  jwt.secret=${JWT_SECRET:...}
  app.cors.allowed-origins=${CORS_ALLOWED_ORIGINS:...}
  ```

- En desarrollo local sin Docker sigue usando H2 en memoria (sin cambios).


