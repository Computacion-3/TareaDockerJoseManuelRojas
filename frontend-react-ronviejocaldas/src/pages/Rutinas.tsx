import { useState, useEffect } from 'react'
import type { Rutina, RutinaRequest } from '../types'
import { getRutinas, createRutina, updateRutina, deleteRutina } from '../services/rutinaService'
import DataTable, { type Column } from '../components/DataTable'
import Modal from '../components/Modal'
import FormInput from '../components/FormInput'
import ConfirmDialog from '../components/ConfirmDialog'
import Badge from '../components/Badge'
import useAuth from '../hooks/useAuth'

const EMPTY_FORM: RutinaRequest = { nombre: '', esPredisenada: false, creadorId: 0 }

const Rutinas = () => {
  const { user } = useAuth()
  const [rutinas, setRutinas] = useState<Rutina[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [refreshKey, setRefreshKey] = useState(0)
  const [modalOpen, setModalOpen] = useState(false)
  const [confirmOpen, setConfirmOpen] = useState(false)
  const [selected, setSelected] = useState<Rutina | null>(null)
  const [form, setForm] = useState<RutinaRequest>(EMPTY_FORM)
  const [saving, setSaving] = useState(false)

  const canEdit = user?.rol === 'ROLE_ADMIN' || user?.rol === 'ROLE_ENTRENADOR'

  useEffect(() => {
    let cancelled = false
    const fetchData = async () => {
      setLoading(true)
      try {
        const data = await getRutinas()
        if (!cancelled) setRutinas(data)
      } catch {
        if (!cancelled) setError('Error al cargar las rutinas.')
      } finally {
        if (!cancelled) setLoading(false)
      }
    }
    void fetchData()
    return () => { cancelled = true }
  }, [refreshKey])

  const openCreate = () => {
    setSelected(null)
    setForm(EMPTY_FORM)
    setModalOpen(true)
  }

  const openEdit = (rutina: Rutina) => {
    setSelected(rutina)
    setForm({ nombre: rutina.nombre, esPredisenada: rutina.esPredisenada, creadorId: rutina.creadorId })
    setModalOpen(true)
  }

  const openDelete = (rutina: Rutina) => {
    setSelected(rutina)
    setConfirmOpen(true)
  }

  const handleSave = async () => {
    if (!form.nombre.trim()) return
    setSaving(true)
    try {
      if (selected) {
        await updateRutina(selected.id, form)
      } else {
        await createRutina(form)
      }
      setModalOpen(false)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al guardar la rutina.')
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async () => {
    if (!selected) return
    try {
      await deleteRutina(selected.id)
      setConfirmOpen(false)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al eliminar la rutina.')
    }
  }

  const columns: Column<Record<string, unknown>>[] = [
    { key: 'id', label: '#' },
    { key: 'nombre', label: 'Nombre' },
    {
      key: 'esPredisenada',
      label: 'Tipo',
      render: (val) => (
        <Badge value={val ? 'PREDISEÑADA' : 'PERSONALIZADA'} label={val ? 'Prediseñada' : 'Personalizada'} />
      ),
    },
  ]

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Rutinas</h1>
          <p className="text-gray-500 text-sm mt-0.5">Gestión de rutinas de entrenamiento</p>
        </div>
        {canEdit && (
          <button
            onClick={openCreate}
            className="px-4 py-2 bg-primary-600 hover:bg-primary-700 text-white text-sm font-medium rounded-lg"
          >
            + Nueva rutina
          </button>
        )}
      </div>

      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">{error}</div>
      )}

      <DataTable
        columns={columns}
        data={rutinas as unknown as Record<string, unknown>[]}
        loading={loading}
        onEdit={canEdit ? (row) => openEdit(row as unknown as Rutina) : undefined}
        onDelete={canEdit ? (row) => openDelete(row as unknown as Rutina) : undefined}
      />

      <Modal
        isOpen={modalOpen}
        title={selected ? 'Editar rutina' : 'Nueva rutina'}
        onClose={() => setModalOpen(false)}
      >
        <div className="space-y-4">
          <FormInput
            label="Nombre"
            value={form.nombre}
            onChange={(e) => setForm({ ...form, nombre: e.target.value })}
            placeholder="Ej: Rutina de pecho"
          />
          <div className="flex items-center gap-2">
            <input
              id="prediseñada"
              type="checkbox"
              checked={form.esPredisenada}
              onChange={(e) => setForm({ ...form, esPredisenada: e.target.checked })}
              className="w-4 h-4 accent-primary-600"
            />
            <label htmlFor="prediseñada" className="text-sm text-gray-700">
              Rutina prediseñada
            </label>
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
        message={`¿Eliminar la rutina "${selected?.nombre}"? Esta acción no se puede deshacer.`}
        onConfirm={() => void handleDelete()}
        onCancel={() => setConfirmOpen(false)}
      />
    </div>
  )
}

export default Rutinas
