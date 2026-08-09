import apiClient from './apiClient'
import type { Evento, EventoRequest } from '../types'

export const getEventos = async (): Promise<Evento[]> => {
  const res = await apiClient.get<Evento[]>('/eventos')
  return res.data
}

export const getEventoById = async (id: number): Promise<Evento> => {
  const res = await apiClient.get<Evento>(`/eventos/${id}`)
  return res.data
}

export const createEvento = async (data: EventoRequest): Promise<Evento> => {
  const res = await apiClient.post<Evento>('/eventos', data)
  return res.data
}

export const updateEvento = async (id: number, data: EventoRequest): Promise<Evento> => {
  const res = await apiClient.put<Evento>(`/eventos/${id}`, data)
  return res.data
}

export const deleteEvento = async (id: number): Promise<void> => {
  await apiClient.delete(`/eventos/${id}`)
}

export const inscribirseEvento = async (eventoId: number, usuarioId: number): Promise<void> => {
  await apiClient.post(`/eventos/${eventoId}/inscribir/${usuarioId}`)
}
