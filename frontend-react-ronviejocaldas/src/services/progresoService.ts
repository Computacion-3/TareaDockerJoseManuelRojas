import apiClient from './apiClient'
import type { Progreso, ProgresoRequest } from '../types'

export const getProgresos = async (): Promise<Progreso[]> => {
  const res = await apiClient.get<Progreso[]>('/progresos')
  return res.data
}

export const getProgresosByUsuario = async (usuarioId: number): Promise<Progreso[]> => {
  const res = await apiClient.get<Progreso[]>(`/progresos/usuario/${usuarioId}`)
  return res.data
}

export const createProgreso = async (data: ProgresoRequest): Promise<Progreso> => {
  const res = await apiClient.post<Progreso>('/progresos', data)
  return res.data
}

export const updateProgreso = async (id: number, data: ProgresoRequest): Promise<Progreso> => {
  const res = await apiClient.put<Progreso>(`/progresos/${id}`, data)
  return res.data
}

export const deleteProgreso = async (id: number): Promise<void> => {
  await apiClient.delete(`/progresos/${id}`)
}
