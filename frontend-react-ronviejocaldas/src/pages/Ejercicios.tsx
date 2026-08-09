import { useState, useEffect } from 'react'
import type { Ejercicio, EjercicioRequest } from '../types'
import { getEjercicios, createEjercicio, updateEjercicio, deleteEjercicio } from '../services/ejercicioService'
import DataTable, { type Column } from '../components/DataTable'
import Modal from '../components/Modal'
import FormInput from '../components/FormInput'
import ConfirmDialog from '../components/ConfirmDialog'
import Badge from '../components/Badge'
import useAuth from '../hooks/useAuth'

const TIPOS = ['CARDIO', 'FUERZA', 'FLEXIBILIDAD', 'EQUILIBRIO', 'OTRO']
const DIFICULTADES = ['FÁCIL', 'MEDIO', 'DIFÍCIL']

const EMPTY_FORM: EjercicioRequest = {
  nombre: '',
  tipo: 'CARDIO',
  descripcion: '',
  duracionMinutos: 15,
  dificultad: 'MEDIO',
  urlVideo: '',
}

const Ejercicios = () => {
  const { user } = useAuth()
  const [ejercicios, setEjercicios] = useState<Ejercicio[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [refreshKey, setRefreshKey] = useState(0)
  const [modalOpen, setModalOpen] = useState(false)
  const [confirmOpen, setConfirmOpen] = useState(false)
  const [selected, setSelected] = useState<Ejercicio | null>(null)
  const [form, setForm] = useState<EjercicioRequest>(EMPTY_FORM)
  const [saving, setSaving] = useState(false)

  const canEdit = user?.rol === 'ROLE_ADMIN' || user?.rol === 'ROLE_ENTRENADOR'

  useEffect(() => {
    let cancelled = false
    const fetchData = async () => {
      setLoading(true)
      try {
        const data = await getEjercicios()
        if (!cancelled) setEjercicios(data)
      } catch {
        if (!cancelled) setError('Error al cargar los ejercicios.')
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

  const openEdit = (ej: Ejercicio) => {
    setSelected(ej)
    setForm({
      nombre: ej.nombre,
      tipo: ej.tipo,
      descripcion: ej.descripcion,
      duracionMinutos: ej.duracionMinutos,
      dificultad: ej.dificultad,
      urlVideo: ej.urlVideo,
    })
    setModalOpen(true)
  }

  const openDelete = (ej: Ejercicio) => {
    setSelected(ej)
    setConfirmOpen(true)
  }

  const handleSave = async () => {
    if (!form.nombre.trim()) return
    setSaving(true)
    try {
      if (selected) {
        await updateEjercicio(selected.id, form)
      } else {
        await createEjercicio(form)
      }
      setModalOpen(false)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al guardar el ejercicio.')
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async () => {
    if (!selected) return
    try {
      await deleteEjercicio(selected.id)
      setConfirmOpen(false)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al eliminar el ejercicio.')
    }
  }

  const columns: Column<Record<string, unknown>>[] = [
    { key: 'nombre', label: 'Nombre' },
    {
      key: 'tipo',
      label: 'Tipo',
      render: (val) => <Badge value={String(val)} />,
    },
    {
      key: 'dificultad',
      label: 'Dificultad',
      render: (val) => <Badge value={String(val)} />,
    },
    { key: 'duracionMinutos', label: 'Duración (min)' },
  ]

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Ejercicios</h1>
          <p className="text-gray-500 text-sm mt-0.5">Catálogo de ejercicios disponibles</p>
        </div>
        {canEdit && (
          <button
            onClick={openCreate}
            className="px-4 py-2 bg-primary-600 hover:bg-primary-700 text-white text-sm font-medium rounded-lg"
          >
            + Nuevo ejercicio
          </button>
        )}
      </div>

      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">{error}</div>
      )}

      <DataTable
        columns={columns}
        data={ejercicios as unknown as Record<string, unknown>[]}
        loading={loading}
        onEdit={canEdit ? (row) => openEdit(row as unknown as Ejercicio) : undefined}
        onDelete={canEdit ? (row) => openDelete(row as unknown as Ejercicio) : undefined}
      />

      <Modal
        isOpen={modalOpen}
        title={selected ? 'Editar ejercicio' : 'Nuevo ejercicio'}
        onClose={() => setModalOpen(false)}
      >
        <div className="space-y-4">
          <FormInput
            label="Nombre"
            value={form.nombre}
            onChange={(e) => setForm({ ...form, nombre: e.target.value })}
            placeholder="Ej: Flexiones"
          />
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Tipo</label>
            <select
              value={form.tipo}
              onChange={(e) => setForm({ ...form, tipo: e.target.value })}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
              {TIPOS.map((t) => <option key={t}>{t}</option>)}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Dificultad</label>
            <select
              value={form.dificultad}
              onChange={(e) => setForm({ ...form, dificultad: e.target.value })}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
              {DIFICULTADES.map((d) => <option key={d}>{d}</option>)}
            </select>
          </div>
          <FormInput
            label="Duración (minutos)"
            type="number"
            min={1}
            value={form.duracionMinutos}
            onChange={(e) => setForm({ ...form, duracionMinutos: Number(e.target.value) })}
          />
          <FormInput
            label="Descripción"
            value={form.descripcion}
            onChange={(e) => setForm({ ...form, descripcion: e.target.value })}
            placeholder="Descripción del ejercicio"
          />
          <FormInput
            label="URL del video"
            value={form.urlVideo}
            onChange={(e) => setForm({ ...form, urlVideo: e.target.value })}
            placeholder="https://..."
          />
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
        message={`¿Eliminar el ejercicio "${selected?.nombre}"? Esta acción no se puede deshacer.`}
        onConfirm={() => void handleDelete()}
        onCancel={() => setConfirmOpen(false)}
      />
    </div>
  )
}

export default Ejercicios
