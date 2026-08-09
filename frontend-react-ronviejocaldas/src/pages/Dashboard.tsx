import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import useAuth from '../hooks/useAuth'
import { getRutinas } from '../services/rutinaService'
import { getEjercicios } from '../services/ejercicioService'

interface StatCard {
  title: string
  value: number | string
  description: string
  color: string
  route: string
}

const Dashboard = () => {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [totalRutinas, setTotalRutinas] = useState(0)
  const [totalEjercicios, setTotalEjercicios] = useState(0)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [rutinas, ejercicios] = await Promise.all([
          getRutinas(),
          getEjercicios(),
        ])
        setTotalRutinas(rutinas.length)
        setTotalEjercicios(ejercicios.length)
      } catch {
        // Stats no críticos, no bloquear el dashboard
      } finally {
        setLoading(false)
      }
    }
    void fetchStats()
  }, [])

  const rolLabel = user?.rol?.replace('ROLE_', '') ?? ''

  const cards: StatCard[] = [
    {
      title: 'Rutinas disponibles',
      value: loading ? '...' : totalRutinas,
      description: 'Rutinas de entrenamiento registradas',
      color: 'bg-green-500',
      route: '/rutinas',
    },
    {
      title: 'Ejercicios',
      value: loading ? '...' : totalEjercicios,
      description: 'Ejercicios en el catálogo',
      color: 'bg-blue-500',
      route: '/ejercicios',
    },
    {
      title: 'Mi progreso',
      value: '→',
      description: 'Registra y consulta tu progreso',
      color: 'bg-purple-500',
      route: '/progreso',
    },
    {
      title: 'Eventos',
      value: '→',
      description: 'Próximos eventos del gimnasio',
      color: 'bg-orange-500',
      route: '/eventos',
    },
  ]

  return (
    <div className="p-6">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-800">
          Bienvenido, {user?.correoInstitucional?.split('@')[0]}
        </h1>
        <p className="text-gray-500 text-sm mt-1">
          Sesión iniciada como <span className="font-medium text-primary-600">{rolLabel}</span>
        </p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        {cards.map((card) => (
          <button
            key={card.title}
            onClick={() => navigate(card.route)}
            className="text-left bg-white rounded-xl shadow-sm border border-gray-100 p-5 hover:shadow-md transition-shadow"
          >
            <div className={`w-10 h-10 ${card.color} rounded-lg mb-3`} />
            <p className="text-2xl font-bold text-gray-800">{card.value}</p>
            <p className="text-sm font-medium text-gray-700 mt-1">{card.title}</p>
            <p className="text-xs text-gray-400 mt-0.5">{card.description}</p>
          </button>
        ))}
      </div>

      <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-5">
        <h2 className="text-base font-semibold text-gray-800 mb-3">Accesos rápidos</h2>
        <div className="flex flex-wrap gap-2">
          <button
            onClick={() => navigate('/rutinas')}
            className="px-4 py-2 text-sm bg-gray-100 hover:bg-primary-100 hover:text-primary-700 rounded-lg transition-colors"
          >
            Ver rutinas
          </button>
          <button
            onClick={() => navigate('/ejercicios')}
            className="px-4 py-2 text-sm bg-gray-100 hover:bg-primary-100 hover:text-primary-700 rounded-lg transition-colors"
          >
            Ver ejercicios
          </button>
          <button
            onClick={() => navigate('/notificaciones')}
            className="px-4 py-2 text-sm bg-gray-100 hover:bg-primary-100 hover:text-primary-700 rounded-lg transition-colors"
          >
            Notificaciones
          </button>
          <button
            onClick={() => navigate('/eventos')}
            className="px-4 py-2 text-sm bg-gray-100 hover:bg-primary-100 hover:text-primary-700 rounded-lg transition-colors"
          >
            Eventos
          </button>
        </div>
      </div>
    </div>
  )
}

export default Dashboard
