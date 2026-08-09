import { useState, useEffect } from 'react'
import type { Progreso as ProgresoData, ProgresoRequest } from '../types'
import { getProgresosByUsuario, createProgreso, deleteProgreso } from '../services/progresoService'
import DataTable, { type Column } from '../components/DataTable'
import Modal from '../components/Modal'
import FormInput from '../components/FormInput'
import ConfirmDialog from '../components/ConfirmDialog'
import useAuth from '../hooks/useAuth'

const today = () => new Date().toISOString().split('T')[0]

const Progreso = () => {
  const { user } = useAuth()
  const [registros, setRegistros] = useState<ProgresoData[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [refreshKey, setRefreshKey] = useState(0)
  const [modalOpen, setModalOpen] = useState(false)
  const [confirmOpen, setConfirmOpen] = useState(false)
  const [selected, setSelected] = useState<ProgresoData | null>(null)
  const [saving, setSaving] = useState(false)
  const [form, setForm] = useState<ProgresoRequest>({
    usuarioId: 0,
    fecha: today(),
    repeticionesRealizadas: 0,
    tiempoRealizadoMinutos: 0,
    nivelEsfuerzo: 5,
  })

  useEffect(() => {
    if (!user) return
    let cancelled = false
    const fetchData = async () => {
      setLoading(true)
      try {
        const data = await getProgresosByUsuario(user?.id ?? 0)
        if (!cancelled) setRegistros(data)
      } catch {
        if (!cancelled) setError('Error al cargar el progreso.')
      } finally {
        if (!cancelled) setLoading(false)
      }
    }
    void fetchData()
    return () => { cancelled = true }
  }, [refreshKey, user])

  const openCreate = () => {
    setForm({ usuarioId: user?.id ?? 0, fecha: today(), repeticionesRealizadas: 0, tiempoRealizadoMinutos: 0, nivelEsfuerzo: 5 })
    setModalOpen(true)
  }

  const openDelete = (r: ProgresoData) => {
    setSelected(r)
    setConfirmOpen(true)
  }

  const handleSave = async () => {
    setSaving(true)
    try {
      await createProgreso(form)
      setModalOpen(false)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al guardar el registro.')
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async () => {
    if (!selected) return
    try {
      await deleteProgreso(selected.id)
      setConfirmOpen(false)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al eliminar el registro.')
    }
  }

  const columns: Column<Record<string, unknown>>[] = [
    { key: 'fecha', label: 'Fecha' },
    { key: 'repeticionesRealizadas', label: 'Repeticiones' },
    { key: 'tiempoRealizadoMinutos', label: 'Tiempo (min)' },
    {
      key: 'nivelEsfuerzo',
      label: 'Esfuerzo',
      render: (val) => (
        <div className="flex items-center gap-1">
          <div className="w-20 bg-gray-200 rounded-full h-2">
            <div
              className="bg-primary-500 h-2 rounded-full"
              style={{ width: `${(Number(val) / 10) * 100}%` }}
            />
          </div>
          <span className="text-xs text-gray-600">{String(val)}/10</span>
        </div>
      ),
    },
  ]

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Mi Progreso</h1>
          <p className="text-gray-500 text-sm mt-0.5">Historial de entrenamiento</p>
        </div>
        <button
          onClick={openCreate}
          className="px-4 py-2 bg-primary-600 hover:bg-primary-700 text-white text-sm font-medium rounded-lg"
        >
          + Registrar sesión
        </button>
      </div>

      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">{error}</div>
      )}

      <DataTable
        columns={columns}
        data={registros as unknown as Record<string, unknown>[]}
        loading={loading}
        onDelete={(row) => openDelete(row as unknown as ProgresoData)}
      />

      <Modal isOpen={modalOpen} title="Registrar sesión de entrenamiento" onClose={() => setModalOpen(false)}>
        <div className="space-y-4">
          <FormInput
            label="Fecha"
            type="date"
            value={form.fecha}
            onChange={(e) => setForm({ ...form, fecha: e.target.value })}
          />
          <FormInput
            label="Repeticiones realizadas"
            type="number"
            min={0}
            value={form.repeticionesRealizadas}
            onChange={(e) => setForm({ ...form, repeticionesRealizadas: Number(e.target.value) })}
          />
          <FormInput
            label="Tiempo realizado (minutos)"
            type="number"
            min={0}
            value={form.tiempoRealizadoMinutos}
            onChange={(e) => setForm({ ...form, tiempoRealizadoMinutos: Number(e.target.value) })}
          />
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Nivel de esfuerzo: {form.nivelEsfuerzo}/10
            </label>
            <input
              type="range"
              min={1}
              max={10}
              value={form.nivelEsfuerzo}
              onChange={(e) => setForm({ ...form, nivelEsfuerzo: Number(e.target.value) })}
              className="w-full accent-primary-600"
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
              {saving ? 'Guardando...' : 'Guardar'}
            </button>
          </div>
        </div>
      </Modal>

      <ConfirmDialog
        isOpen={confirmOpen}
        message="¿Eliminar este registro de progreso?"
        onConfirm={() => void handleDelete()}
        onCancel={() => setConfirmOpen(false)}
      />
    </div>
  )
}

export default Progreso
