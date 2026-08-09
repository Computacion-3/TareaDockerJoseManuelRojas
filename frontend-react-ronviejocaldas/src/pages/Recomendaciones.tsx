import { useState, useEffect } from 'react'
import type { Recomendacion, RecomendacionRequest } from '../types'
import { getRecomendaciones, createRecomendacion, deleteRecomendacion } from '../services/recomendacionService'
import Modal from '../components/Modal'
import ConfirmDialog from '../components/ConfirmDialog'
import useAuth from '../hooks/useAuth'

const Recomendaciones = () => {
  const { user } = useAuth()
  const EMPTY_FORM: RecomendacionRequest = { contenido: '', entrenadorId: user?.id ?? 0, estudianteId: 0 }
  const [recomendaciones, setRecomendaciones] = useState<Recomendacion[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [refreshKey, setRefreshKey] = useState(0)
  const [modalOpen, setModalOpen] = useState(false)
  const [confirmOpen, setConfirmOpen] = useState(false)
  const [selected, setSelected] = useState<Recomendacion | null>(null)
  const [form, setForm] = useState<RecomendacionRequest>(EMPTY_FORM)
  const [saving, setSaving] = useState(false)

  const canCreate = user?.rol === 'ROLE_ADMIN' || user?.rol === 'ROLE_ENTRENADOR'

  useEffect(() => {
    let cancelled = false
    const fetchData = async () => {
      setLoading(true)
      try {
        const data = await getRecomendaciones()
        if (!cancelled) setRecomendaciones(data)
      } catch {
        if (!cancelled) setError('Error al cargar las recomendaciones.')
      } finally {
        if (!cancelled) setLoading(false)
      }
    }
    void fetchData()
    return () => { cancelled = true }
  }, [refreshKey])

  const handleSave = async () => {
    if (!form.contenido.trim()) return
    setSaving(true)
    try {
      await createRecomendacion(form)
      setModalOpen(false)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al crear la recomendación.')
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async () => {
    if (!selected) return
    try {
      await deleteRecomendacion(selected.id)
      setConfirmOpen(false)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al eliminar la recomendación.')
    }
  }

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Recomendaciones</h1>
          <p className="text-gray-500 text-sm mt-0.5">Consejos para los estudiantes</p>
        </div>
        {canCreate && (
          <button
            onClick={() => { setForm(EMPTY_FORM); setModalOpen(true) }}
            className="px-4 py-2 bg-primary-600 hover:bg-primary-700 text-white text-sm font-medium rounded-lg"
          >
            + Nueva recomendación
          </button>
        )}
      </div>

      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">{error}</div>
      )}

      {loading ? (
        <div className="flex justify-center py-12">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" />
        </div>
      ) : recomendaciones.length === 0 ? (
        <div className="text-center py-12 text-gray-400 text-sm">No hay recomendaciones disponibles.</div>
      ) : (
        <div className="space-y-3">
          {recomendaciones.map((r) => (
            <div key={r.id} className="bg-white rounded-xl border border-gray-100 shadow-sm p-5">
              <div className="flex items-start justify-between gap-4">
                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-2">
                    <span className="w-7 h-7 bg-primary-100 text-primary-700 rounded-full flex items-center justify-center text-xs font-bold">
                      {r.entrenadorId}
                    </span>
                    <span className="text-xs text-gray-500">Entrenador #{r.entrenadorId} → Estudiante #{r.estudianteId}</span>
                  </div>
                  <p className="text-sm text-gray-700 leading-relaxed">{r.contenido}</p>
                </div>
                {canCreate && (
                  <button
                    onClick={() => { setSelected(r); setConfirmOpen(true) }}
                    className="text-xs px-2.5 py-1 bg-red-50 hover:bg-red-100 text-red-600 rounded-md flex-shrink-0"
                  >
                    Eliminar
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}

      <Modal isOpen={modalOpen} title="Nueva recomendación" onClose={() => setModalOpen(false)}>
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Contenido</label>
            <textarea
              rows={4}
              value={form.contenido}
              onChange={(e) => setForm({ ...form, contenido: e.target.value })}
              placeholder="Escribe la recomendación para el estudiante..."
              className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500 resize-none"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">ID del estudiante</label>
            <input
              type="number"
              min={1}
              value={form.estudianteId}
              onChange={(e) => setForm({ ...form, estudianteId: Number(e.target.value) })}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
            />
          </div>
          <div className="flex gap-3 justify-end pt-2">
            <button
              onClick={() => setModalOpen(false)}
              className="px-4 py-2 text-sm border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50"
            >
              Cancelar
            </button>
            <button
              onClick={() => void handleSave()}
              disabled={saving}
              className="px-4 py-2 text-sm bg-primary-600 hover:bg-primary-700 disabled:opacity-60 text-white rounded-lg"
            >
              {saving ? 'Guardando...' : 'Publicar'}
            </button>
          </div>
        </div>
      </Modal>

      <ConfirmDialog
        isOpen={confirmOpen}
        message="¿Eliminar esta recomendación?"
        onConfirm={() => void handleDelete()}
        onCancel={() => setConfirmOpen(false)}
      />
    </div>
  )
}

export default Recomendaciones
