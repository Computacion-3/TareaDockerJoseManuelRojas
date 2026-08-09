import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import PrivateRoute from './components/PrivateRoute'
import RoleWrapper from './components/RoleWrapper'
import Layout from './components/Layout'
import Login from './pages/Login'
import Unauthorized from './pages/Unauthorized'
import Dashboard from './pages/Dashboard'
import Rutinas from './pages/Rutinas'
import Ejercicios from './pages/Ejercicios'
import Progreso from './pages/Progreso'
import Notificaciones from './pages/Notificaciones'
import Eventos from './pages/Eventos'
import Usuarios from './pages/Usuarios'
import Recomendaciones from './pages/Recomendaciones'

const App = () => {
  return (
    <BrowserRouter basename={import.meta.env.BASE_URL}>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/unauthorized" element={<Unauthorized />} />

        <Route element={<PrivateRoute />}>
          <Route element={<Layout />}>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/rutinas" element={<Rutinas />} />
            <Route path="/ejercicios" element={<Ejercicios />} />
            <Route path="/progreso" element={<Progreso />} />
            <Route path="/notificaciones" element={<Notificaciones />} />
            <Route path="/eventos" element={<Eventos />} />

            <Route element={<RoleWrapper allowedRoles={['ROLE_ADMIN', 'ROLE_ENTRENADOR']} />}>
              <Route path="/recomendaciones" element={<Recomendaciones />} />
            </Route>

            <Route element={<RoleWrapper allowedRoles={['ROLE_ADMIN']} />}>
              <Route path="/usuarios" element={<Usuarios />} />
            </Route>
          </Route>
        </Route>

        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
