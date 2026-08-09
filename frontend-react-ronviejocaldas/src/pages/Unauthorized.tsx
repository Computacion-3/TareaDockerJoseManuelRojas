import { useNavigate } from 'react-router-dom'

const Unauthorized = () => {
  const navigate = useNavigate()

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center px-4">
      <div className="text-center">
        <div className="text-6xl font-bold text-red-500 mb-4">403</div>
        <h1 className="text-2xl font-semibold text-gray-800 mb-2">Acceso denegado</h1>
        <p className="text-gray-500 mb-6">No tienes permisos para acceder a esta página.</p>
        <button
          onClick={() => navigate(-1)}
          className="px-6 py-2 bg-primary-600 hover:bg-primary-700 text-white rounded-lg font-medium transition-colors"
        >
          Volver
        </button>
      </div>
    </div>
  )
}

export default Unauthorized
