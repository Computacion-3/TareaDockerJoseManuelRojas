import apiClient from './apiClient'
import type { Usuario, UsuarioRequest } from '../types'

export const getUsuarios = async (): Promise<Usuario[]> => {
  const res = await apiClient.get<Usuario[]>('/usuarios')
  return res.data
}

export const getUsuarioById = async (id: number): Promise<Usuario> => {
  const res = await apiClient.get<Usuario>(`/usuarios/${id}`)
  return res.data
}

export const createUsuario = async (data: UsuarioRequest): Promise<Usuario> => {
  const res = await apiClient.post<Usuario>('/usuarios', data)
  return res.data
}

export const updateUsuario = async (id: number, data: Partial<UsuarioRequest>): Promise<Usuario> => {
  const res = await apiClient.put<Usuario>(`/usuarios/${id}`, data)
  return res.data
}

export const deleteUsuario = async (id: number): Promise<void> => {
  await apiClient.delete(`/usuarios/${id}`)
}

export const asignarEntrenador = async (usuarioId: number, entrenadorId: number): Promise<Usuario> => {
  const res = await apiClient.put<Usuario>(`/usuarios/${usuarioId}/entrenador/${entrenadorId}`)
  return res.data
}
