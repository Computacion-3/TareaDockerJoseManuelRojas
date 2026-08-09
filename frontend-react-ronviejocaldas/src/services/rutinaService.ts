import apiClient from './apiClient'
import type { Rutina, RutinaRequest } from '../types'

export const getRutinas = async (): Promise<Rutina[]> => {
  const res = await apiClient.get<Rutina[]>('/rutinas')
  return res.data
}

export const getRutinaById = async (id: number): Promise<Rutina> => {
  const res = await apiClient.get<Rutina>(`/rutinas/${id}`)
  return res.data
}

export const createRutina = async (data: RutinaRequest): Promise<Rutina> => {
  const res = await apiClient.post<Rutina>('/rutinas', data)
  return res.data
}

export const updateRutina = async (id: number, data: RutinaRequest): Promise<Rutina> => {
  const res = await apiClient.put<Rutina>(`/rutinas/${id}`, data)
  return res.data
}

export const deleteRutina = async (id: number): Promise<void> => {
  await apiClient.delete(`/rutinas/${id}`)
}
