import { Outlet, NavLink, useNavigate } from 'react-router-dom'
import useAuth from '../hooks/useAuth'
import { useAppSelector } from '../hooks/reduxHooks'

const navLinkClass = ({ isActive }: { isActive: boolean }) =>
  `flex items-center gap-2 px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
    isActive
      ? 'bg-primary-700 text-white'
      : 'text-gray-300 hover:bg-primary-800 hover:text-white'
  }`

const Layout = () => {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const noLeidas = useAppSelector((state) => state.notificaciones.noLeidas)

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const isAdmin = user?.rol === 'ROLE_ADMIN'
  const isEntrenador = user?.rol === 'ROLE_ENTRENADOR' || isAdmin
  const isEstudiante = user?.rol === 'ROLE_ESTUDIANTE'

  return (
    <div className="flex h-screen bg-gray-100">
      <aside className="w-64 bg-gray-900 flex flex-col">
        <div className="p-4 border-b border-gray-700">
          <h1 className="text-xl font-bold text-primary-500">ICESI FIT</h1>
          <p className="text-xs text-gray-400 mt-1 truncate">{user?.correoInstitucional}</p>
          <span className="inline-block mt-1 text-xs bg-primary-700 text-white px-2 py-0.5 rounded-full">
            {user?.rol?.replace('ROLE_', '')}
          </span>
        </div>

        <nav className="flex-1 p-3 space-y-1 overflow-y-auto">
          <NavLink to="/dashboard" className={navLinkClass}>
            Dashboard
          </NavLink>
          <NavLink to="/rutinas" className={navLinkClass}>
            Rutinas
          </NavLink>
          <NavLink to="/ejercicios" className={navLinkClass}>
            Ejercicios
          </NavLink>
          <NavLink to="/eventos" className={navLinkClass}>
            Eventos
          </NavLink>
          <NavLink to="/notificaciones" className={navLinkClass}>
            <span>Notificaciones</span>
            {noLeidas > 0 && (
              <span className="ml-auto bg-red-500 text-white text-xs rounded-full px-1.5 py-0.5 min-w-[20px] text-center">
                {noLeidas}
              </span>
            )}
          </NavLink>
          {(isEstudiante || isEntrenador) && (
            <NavLink to="/progreso" className={navLinkClass}>
              Mi Progreso
            </NavLink>
          )}
          {isEntrenador && (
            <NavLink to="/recomendaciones" className={navLinkClass}>
              Recomendaciones
            </NavLink>
          )}
          {isAdmin && (
            <NavLink to="/usuarios" className={navLinkClass}>
              Usuarios
            </NavLink>
          )}
        </nav>

        <div className="p-3 border-t border-gray-700">
          <button
            onClick={handleLogout}
            className="w-full px-3 py-2 text-sm text-gray-300 hover:text-white hover:bg-red-700 rounded-lg transition-colors text-left"
          >
            Cerrar sesión
          </button>
        </div>
      </aside>

      <main className="flex-1 overflow-y-auto">
        <Outlet />
      </main>
    </div>
  )
}

export default Layout
