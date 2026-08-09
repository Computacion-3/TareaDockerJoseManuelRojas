import { useState, useEffect } from 'react'
import type { Usuario, UsuarioRequest } from '../types'
import { getUsuarios, createUsuario, deleteUsuario } from '../services/usuarioService'
import DataTable, { type Column } from '../components/DataTable'
import Modal from '../components/Modal'
import FormInput from '../components/FormInput'
import ConfirmDialog from '../components/ConfirmDialog'
import Badge from '../components/Badge'

const ROLES = [
  { id: 1, nombre: 'ROLE_ADMIN' },
  { id: 2, nombre: 'ROLE_ENTRENADOR' },
  { id: 3, nombre: 'ROLE_ESTUDIANTE' },
]

const EMPTY_FORM: UsuarioRequest = {
  nombre: '',
  correoInstitucional: '',
  password: '',
  rolId: 3,
}

const Usuarios = () => {
  const [usuarios, setUsuarios] = useState<Usuario[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [refreshKey, setRefreshKey] = useState(0)
  const [modalOpen, setModalOpen] = useState(false)
  const [confirmOpen, setConfirmOpen] = useState(false)
  const [selected, setSelected] = useState<Usuario | null>(null)
  const [form, setForm] = useState<UsuarioRequest>(EMPTY_FORM)
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    let cancelled = false
    const fetchData = async () => {
      setLoading(true)
      try {
        const data = await getUsuarios()
        if (!cancelled) setUsuarios(data)
      } catch {
        if (!cancelled) setError('Error al cargar los usuarios.')
      } finally {
        if (!cancelled) setLoading(false)
      }
    }
    void fetchData()
    return () => { cancelled = true }
  }, [refreshKey])

  const openCreate = () => {
    setForm(EMPTY_FORM)
    setModalOpen(true)
  }

  const openDelete = (u: Usuario) => {
    setSelected(u)
    setConfirmOpen(true)
  }

  const handleSave = async () => {
    if (!form.nombre.trim() || !form.correoInstitucional.trim() || !form.password.trim()) return
    setSaving(true)
    try {
      await createUsuario(form)
      setModalOpen(false)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al crear el usuario.')
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async () => {
    if (!selected) return
    try {
      await deleteUsuario(selected.id)
      setConfirmOpen(false)
      setRefreshKey((k) => k + 1)
    } catch {
      setError('Error al eliminar el usuario.')
    }
  }

  const columns: Column<Record<string, unknown>>[] = [
    { key: 'nombre', label: 'Nombre' },
    { key: 'correoInstitucional', label: 'Correo' },
    {
      key: 'rolNombre',
      label: 'Rol',
      render: (val) => <Badge value={String(val ?? 'ROLE_ESTUDIANTE')} />,
    },
  ]

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Usuarios</h1>
          <p className="text-gray-500 text-sm mt-0.5">Administración de cuentas</p>
        </div>
        <button
          onClick={openCreate}
          className="px-4 py-2 bg-primary-600 hover:bg-primary-700 text-white text-sm font-medium rounded-lg"
        >
          + Nuevo usuario
        </button>
      </div>

      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">{error}</div>
      )}

      <DataTable
        columns={columns}
        data={usuarios as unknown as Record<string, unknown>[]}
        loading={loading}
        onDelete={(row) => openDelete(row as unknown as Usuario)}
      />

      <Modal isOpen={modalOpen} title="Nuevo usuario" onClose={() => setModalOpen(false)}>
        <div className="space-y-4">
          <FormInput
            label="Nombre completo"
            value={form.nombre}
            onChange={(e) => setForm({ ...form, nombre: e.target.value })}
            placeholder="Ej: Juan Pérez"
          />
          <FormInput
            label="Correo institucional"
            type="email"
            value={form.correoInstitucional}
            onChange={(e) => setForm({ ...form, correoInstitucional: e.target.value })}
            placeholder="usuario@icesi.edu.co"
          />
          <FormInput
            label="Contraseña"
            type="password"
            value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })}
            placeholder="Mínimo 6 caracteres"
          />
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Rol</label>
            <select
              value={form.rolId}
              onChange={(e) => setForm({ ...form, rolId: Number(e.target.value) })}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
              {ROLES.map((r) => (
                <option key={r.id} value={r.id}>
                  {r.nombre.replace('ROLE_', '')}
                </option>
              ))}
            </select>
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
              {saving ? 'Creando...' : 'Crear usuario'}
            </button>
          </div>
        </div>
      </Modal>

      <ConfirmDialog
        isOpen={confirmOpen}
        message={`¿Eliminar al usuario "${selected?.nombre}"? Esta acción no se puede deshacer.`}
        onConfirm={() => void handleDelete()}
        onCancel={() => setConfirmOpen(false)}
      />
    </div>
  )
}

export default Usuarios
