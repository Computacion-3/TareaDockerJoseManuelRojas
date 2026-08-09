import apiClient from './apiClient'
import type { Notificacion, NotificacionRequest } from '../types'

export const getNotificaciones = async (): Promise<Notificacion[]> => {
  const res = await apiClient.get<Notificacion[]>('/notificaciones')
  return res.data
}

export const getNotificacionesByReceptor = async (receptorId: number): Promise<Notificacion[]> => {
  const res = await apiClient.get<Notificacion[]>(`/notificaciones/receptor/${receptorId}`)
  return res.data
}

export const createNotificacion = async (data: NotificacionRequest): Promise<Notificacion> => {
  const res = await apiClient.post<Notificacion>('/notificaciones', data)
  return res.data
}

export const marcarLeida = async (id: number): Promise<Notificacion> => {
  const res = await apiClient.put<Notificacion>(`/notificaciones/${id}/leer`)
  return res.data
}

export const deleteNotificacion = async (id: number): Promise<void> => {
  await apiClient.delete(`/notificaciones/${id}`)
}
