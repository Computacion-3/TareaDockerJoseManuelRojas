import { useState, useEffect } from 'react'
import type { Notificacion } from '../types'
import { getNotificaciones, marcarLeida, deleteNotificacion } from '../services/notificacionService'
import { useAppDispatch, useAppSelector } from '../hooks/reduxHooks'
import { resetNoLeidas } from '../store/slices/notificacionesSlice'
import useWebSocket from '../hooks/useWebSocket'
import useAuth from '../hooks/useAuth'
import ConfirmDialog from '../components/ConfirmDialog'

const Notificaciones = () => {
  const dispatch = useAppDispatch()
  const mensajesWS = useAppSelector((state) => state.notificaciones.mensajesWS)
  const [notificaciones, setNotificaciones] = useState<Notificacion[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [refreshKey, setRefreshKey] = useState(0)
  const [confirmOpen, setConfirmOpen] = useState(false)
  const [selectedId, setSelectedId] = useState<number | null>(null)

  const { user } = useAuth()
  useWebSocket(user?.id ?? null)

  useEffect(() => {
    dispatch(resetNoLeidas())
  }, [dispatch])

  useEffect(() => {
    let cancelled = false
    const fetchData = async () => {
      setLoading(true)
      try {
        const data = await getNotificaciones()
        if (!cancelled) setNotificaciones(data)
      } catch {
        if (!cancelled) setError('Error al cargar las notificaciones.')
      } finally {
        if (!cancelled) setLoading(false)
      }
    }
    void fetchData()
    return () => { cancelled = true }
  }, [refreshKey])

  const handleMarcarLeida = async (id: number) => {
    try {
      await marcarLeida(id)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al marcar como leída.')
    }
  }

  const handleDelete = async () => {
    if (!selectedId) return
    try {
      await deleteNotificacion(selectedId)
      setConfirmOpen(false)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al eliminar la notificación.')
    }
  }

  const noLeidas = notificaciones.filter((n) => !n.leido).length

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Notificaciones</h1>
          <p className="text-gray-500 text-sm mt-0.5">
            {noLeidas > 0 ? `${noLeidas} sin leer` : 'Todo al día'}
          </p>
        </div>
      </div>

      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">{error}</div>
      )}

      {mensajesWS.length > 0 && (
        <div className="mb-4">
          <h2 className="text-sm font-semibold text-gray-600 mb-2">Mensajes en tiempo real</h2>
          <div className="space-y-2">
            {mensajesWS.map((msg, idx) => (
              <div key={idx} className="p-3 bg-primary-50 border border-primary-200 rounded-lg text-sm text-primary-800">
                🔔 {msg}
              </div>
            ))}
          </div>
        </div>
      )}

      {loading ? (
        <div className="flex justify-center py-12">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" />
        </div>
      ) : notificaciones.length === 0 ? (
        <div className="text-center py-12 text-gray-400 text-sm">No tienes notificaciones.</div>
      ) : (
        <div className="space-y-3">
          {notificaciones.map((n) => (
            <div
              key={n.id}
              className={`p-4 rounded-xl border flex items-start justify-between gap-4 ${
                n.leido ? 'bg-white border-gray-100' : 'bg-primary-50 border-primary-200'
              }`}
            >
              <div className="flex-1">
                <div className="flex items-center gap-2 mb-1">
                  <span className="text-xs font-semibold text-primary-600 uppercase">{n.tipo}</span>
                  {!n.leido && (
                    <span className="w-2 h-2 bg-primary-500 rounded-full" />
                  )}
                </div>
                <p className="text-sm text-gray-800">{n.contenido}</p>
                <p className="text-xs text-gray-400 mt-1">{new Date(n.fechaEnvio).toLocaleString('es-CO')}</p>
              </div>
              <div className="flex gap-2 flex-shrink-0">
                {!n.leido && (
                  <button
                    onClick={() => void handleMarcarLeida(n.id)}
                    className="text-xs px-2.5 py-1 bg-primary-100 hover:bg-primary-200 text-primary-700 rounded-md"
                  >
                    Marcar leída
                  </button>
                )}
                <button
                  onClick={() => { setSelectedId(n.id); setConfirmOpen(true) }}
                  className="text-xs px-2.5 py-1 bg-red-50 hover:bg-red-100 text-red-600 rounded-md"
                >
                  Eliminar
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      <ConfirmDialog
        isOpen={confirmOpen}
        message="¿Eliminar esta notificación?"
        onConfirm={() => void handleDelete()}
        onCancel={() => setConfirmOpen(false)}
      />
    </div>
  )
}

export default Notificaciones
