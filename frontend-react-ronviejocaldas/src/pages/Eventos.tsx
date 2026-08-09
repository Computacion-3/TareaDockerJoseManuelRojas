import { useState, useEffect } from 'react'
import type { Evento, EventoRequest } from '../types'
import { getEventos, createEvento, deleteEvento, inscribirseEvento } from '../services/eventoService'
import Modal from '../components/Modal'
import FormInput from '../components/FormInput'
import ConfirmDialog from '../components/ConfirmDialog'
import useAuth from '../hooks/useAuth'

const EMPTY_FORM: EventoRequest = {
  nombre: '',
  descripcion: '',
  fechaHora: '',
  lugar: '',
  administradorId: 1,
}

const Eventos = () => {
  const { user } = useAuth()
  const [eventos, setEventos] = useState<Evento[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [refreshKey, setRefreshKey] = useState(0)
  const [modalOpen, setModalOpen] = useState(false)
  const [confirmOpen, setConfirmOpen] = useState(false)
  const [selected, setSelected] = useState<Evento | null>(null)
  const [form, setForm] = useState<EventoRequest>(EMPTY_FORM)
  const [saving, setSaving] = useState(false)

  const isAdmin = user?.rol === 'ROLE_ADMIN'

  useEffect(() => {
    let cancelled = false
    const fetchData = async () => {
      setLoading(true)
      try {
        const data = await getEventos()
        if (!cancelled) setEventos(data)
      } catch {
        if (!cancelled) setError('Error al cargar los eventos.')
      } finally {
        if (!cancelled) setLoading(false)
      }
    }
    void fetchData()
    return () => { cancelled = true }
  }, [refreshKey])

  const handleCreate = async () => {
    if (!form.nombre.trim() || !form.fechaHora || !form.lugar.trim()) return
    setSaving(true)
    try {
      await createEvento(form)
      setModalOpen(false)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al crear el evento.')
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async () => {
    if (!selected) return
    try {
      await deleteEvento(selected.id)
      setConfirmOpen(false)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al eliminar el evento.')
    }
  }

  const handleInscribirse = async (eventoId: number) => {
    try {
      await inscribirseEvento(eventoId, user?.id ?? 0)
      setSuccess('¡Te has inscrito en el evento!')
      setTimeout(() => setSuccess(''), 3000)
    } catch {
      setError('Error al inscribirse en el evento.')
    }
  }

  const formatFecha = (fecha: string) =>
    new Date(fecha).toLocaleString('es-CO', { dateStyle: 'medium', timeStyle: 'short' })

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Eventos</h1>
          <p className="text-gray-500 text-sm mt-0.5">Próximos eventos del gimnasio</p>
        </div>
        {isAdmin && (
          <button
            onClick={() => { setForm(EMPTY_FORM); setModalOpen(true) }}
            className="px-4 py-2 bg-primary-600 hover:bg-primary-700 text-white text-sm font-medium rounded-lg"
          >
            + Nuevo evento
          </button>
        )}
      </div>

      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">{error}</div>
      )}
      {success && (
        <div className="mb-4 p-3 bg-green-50 border border-green-200 rounded-lg text-green-700 text-sm">{success}</div>
      )}

      {loading ? (
        <div className="flex justify-center py-12">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" />
        </div>
      ) : eventos.length === 0 ? (
        <div className="text-center py-12 text-gray-400 text-sm">No hay eventos programados.</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {eventos.map((ev) => (
            <div key={ev.id} className="bg-white rounded-xl border border-gray-100 shadow-sm p-5 flex flex-col gap-3">
              <div>
                <h2 className="font-semibold text-gray-800">{ev.nombre}</h2>
                <p className="text-xs text-gray-500 mt-0.5">{ev.descripcion}</p>
              </div>
              <div className="text-xs text-gray-600 space-y-1">
                <p>📅 {formatFecha(ev.fechaHora)}</p>
                <p>📍 {ev.lugar}</p>
              </div>
              <div className="flex gap-2 mt-auto">
                <button
                  onClick={() => void handleInscribirse(ev.id)}
                  className="flex-1 py-1.5 text-sm bg-primary-600 hover:bg-primary-700 text-white rounded-lg"
                >
                  Inscribirse
                </button>
                {isAdmin && (
                  <button
                    onClick={() => { setSelected(ev); setConfirmOpen(true) }}
                    className="py-1.5 px-3 text-sm bg-red-50 hover:bg-red-100 text-red-600 rounded-lg"
                  >
                    Eliminar
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}

      <Modal isOpen={modalOpen} title="Nuevo evento" onClose={() => setModalOpen(false)}>
        <div className="space-y-4">
          <FormInput
            label="Nombre del evento"
            value={form.nombre}
            onChange={(e) => setForm({ ...form, nombre: e.target.value })}
            placeholder="Ej: Clase de yoga"
          />
          <FormInput
            label="Descripción"
            value={form.descripcion}
            onChange={(e) => setForm({ ...form, descripcion: e.target.value })}
            placeholder="Descripción breve"
          />
          <FormInput
            label="Fecha y hora"
            type="datetime-local"
            value={form.fechaHora}
            onChange={(e) => setForm({ ...form, fechaHora: e.target.value })}
          />
          <FormInput
            label="Lugar"
            value={form.lugar}
            onChange={(e) => setForm({ ...form, lugar: e.target.value })}
            placeholder="Ej: Sala principal"
          />
          <div className="flex gap-3 justify-end pt-2">
            <button
              onClick={() => setModalOpen(false)}
              className="px-4 py-2 text-sm border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50"
            >
              Cancelar
            </button>
            <button
              onClick={() => void handleCreate()}
              disabled={saving}
              className="px-4 py-2 text-sm bg-primary-600 hover:bg-primary-700 disabled:opacity-60 text-white rounded-lg"
            >
              {saving ? 'Guardando...' : 'Crear evento'}
            </button>
          </div>
        </div>
      </Modal>

      <ConfirmDialog
        isOpen={confirmOpen}
        message={`¿Eliminar el evento "${selected?.nombre}"?`}
        onConfirm={() => void handleDelete()}
        onCancel={() => setConfirmOpen(false)}
      />
    </div>
  )
}

export default Eventos
