import apiClient from './apiClient'
import type { Ejercicio, EjercicioRequest } from '../types'

export const getEjercicios = async (): Promise<Ejercicio[]> => {
  const res = await apiClient.get<Ejercicio[]>('/ejercicios')
  return res.data
}

export const getEjercicioById = async (id: number): Promise<Ejercicio> => {
  const res = await apiClient.get<Ejercicio>(`/ejercicios/${id}`)
  return res.data
}

export const createEjercicio = async (data: EjercicioRequest): Promise<Ejercicio> => {
  const res = await apiClient.post<Ejercicio>('/ejercicios', data)
  return res.data
}

export const updateEjercicio = async (id: number, data: EjercicioRequest): Promise<Ejercicio> => {
  const res = await apiClient.put<Ejercicio>(`/ejercicios/${id}`, data)
  return res.data
}

export const deleteEjercicio = async (id: number): Promise<void> => {
  await apiClient.delete(`/ejercicios/${id}`)
}
