# Frontend React — Sistema de Gestión Deportiva

Aplicación web desarrollada con React + TypeScript para la gestión de rutinas, ejercicios, progreso, eventos y usuarios en un entorno deportivo institucional. Se conecta a un backend Spring Boot mediante REST y WebSocket.

---

## Requisitos previos

- [Node.js](https://nodejs.org/) v18 o superior
- npm v9 o superior
- Backend corriendo en `http://localhost:8080` (Spring Boot)

---

## Instalación

```bash
# Clonar el repositorio
git clone <url-del-repositorio>
cd frontend-react-ronviejocaldas

# Instalar dependencias
npm install
```

---

## Comandos disponibles

| Comando | Descripción |
|---|---|
| `npm run dev` | Inicia el servidor de desarrollo en `http://localhost:5173` |
| `npm run build` | Genera la build de producción en `/dist` |
| `npm run preview` | Previsualiza la build de producción localmente |
| `npm run lint` | Ejecuta ESLint sobre los archivos `.ts` y `.tsx` |

---

## Ejecutar en desarrollo

```bash
npm run dev
```

Abre el navegador en `http://localhost:5173`. El servidor recarga automáticamente al guardar cambios.

> El frontend consume la API en `http://localhost:8080/api`. Asegúrate de que el backend esté activo antes de iniciar la app.

---

## Build para producción

```bash
npm run build
```

Los archivos compilados quedan en la carpeta `dist/`. Para previsualizarlos localmente:

```bash
npm run preview
```

---

## Estructura del proyecto

```
src/
├── components/       # Componentes reutilizables (DataTable, Modal, FormInput, etc.)
├── hooks/            # Hooks personalizados (useAuth, useWebSocket, reduxHooks)
├── pages/            # Vistas principales de la aplicación
├── services/         # Clientes HTTP por módulo (axios)
├── store/            # Estado global con Redux Toolkit + redux-persist
│   └── slices/       # Slices de Redux (auth, notificaciones)
├── types/            # Interfaces y tipos TypeScript
├── App.tsx           # Definición de rutas
└── main.tsx          # Punto de entrada
```

---

## Rutas de la aplicación

| Ruta | Acceso | Descripción |
|---|---|---|
| `/login` | Público | Inicio de sesión |
| `/dashboard` | Autenticado | Panel principal |
| `/rutinas` | Autenticado | Gestión de rutinas |
| `/ejercicios` | Autenticado | Catálogo de ejercicios |
| `/progreso` | Autenticado | Registro de progreso |
| `/notificaciones` | Autenticado | Centro de notificaciones |
| `/eventos` | Autenticado | Eventos institucionales |
| `/recomendaciones` | Admin / Entrenador | Recomendaciones a estudiantes |
| `/usuarios` | Solo Admin | Administración de usuarios |

---

## Roles del sistema

| Rol | Permisos |
|---|---|
| `ROLE_ADMIN` | Acceso completo, gestión de usuarios |
| `ROLE_ENTRENADOR` | Acceso a recomendaciones y módulos generales |
| `ROLE_ESTUDIANTE` | Acceso a módulos generales |

---

## Tecnologías utilizadas

- **React 19** + **TypeScript**
- **Vite** — bundler y servidor de desarrollo
- **React Router v7** — enrutamiento del lado del cliente
- **Redux Toolkit** + **redux-persist** — estado global con persistencia
- **Axios** — cliente HTTP con interceptores JWT
- **STOMP / SockJS** — comunicación en tiempo real via WebSocket
- **Tailwind CSS** — estilos utilitarios
- **ESLint** + **Husky** + **lint-staged** — calidad de código en commits

---

## Despliegue en servidor Tomcat

### Servidor
| Campo | Valor |
|---|---|
| IP | `10.147.20.70` |
| Usuario SSH | `computacion2` |
| Tomcat webapps | `/home/computacion2/apache-tomcat-11.0.18/webapps/` |

### Construcción del WAR del frontend

```bash
# 1. Instalar dependencias
npm install

# 2. Generar build de producción
npm run build

# 3. Copiar WEB-INF dentro de dist/
cp -r WEB-INF dist/

# 4. Crear el WAR (desde la raíz del proyecto)
jar cf "iaslab#compu2#ronviejocaldas-front.war" -C dist .
```

> En Linux: reemplazar `jar` por `zip -r "../iaslab#compu2#ronviejocaldas-front.war" *` desde dentro de `dist/`.

### Subir al servidor

```bash
# Con pscp (PuTTY) desde Windows:
pscp -pw <contraseña> iaslab#compu2#ronviejocaldas-front.war computacion2@10.147.20.70:/home/computacion2/apache-tomcat-11.0.18/webapps/

# Con scp desde Linux:
sshpass -p <contraseña> scp iaslab#compu2#ronviejocaldas-front.war computacion2@10.147.20.70:/home/computacion2/apache-tomcat-11.0.18/webapps/
```

Tomcat detecta el WAR automáticamente y lo despliega sin reinicio.

### URLs finales

| Servicio | URL |
|---|---|
| Frontend | http://10.147.20.70:8080/iaslab/compu2/ronviejocaldas-front |
| Backend API | http://10.147.20.70:8080/iaslab/compu2/ronviejocaldas/api |

---

## Variables de entorno

Por defecto la app apunta al backend en `http://localhost:8080/api`. Si necesitas cambiar la URL base, modifica el archivo `src/services/apiClient.ts`:

```ts
baseURL: 'http://localhost:8080/api'
```
