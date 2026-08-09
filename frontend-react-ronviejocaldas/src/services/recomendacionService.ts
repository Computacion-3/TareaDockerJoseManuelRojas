import apiClient from './apiClient'
import type { Recomendacion, RecomendacionRequest } from '../types'

export const getRecomendaciones = async (): Promise<Recomendacion[]> => {
  const res = await apiClient.get<Recomendacion[]>('/recomendaciones')
  return res.data
}

export const createRecomendacion = async (data: RecomendacionRequest): Promise<Recomendacion> => {
  const res = await apiClient.post<Recomendacion>('/recomendaciones', data)
  return res.data
}

export const updateRecomendacion = async (id: number, data: RecomendacionRequest): Promise<Recomendacion> => {
  const res = await apiClient.put<Recomendacion>(`/recomendaciones/${id}`, data)
  return res.data
}

export const deleteRecomendacion = async (id: number): Promise<void> => {
  await apiClient.delete(`/recomendaciones/${id}`)
}
