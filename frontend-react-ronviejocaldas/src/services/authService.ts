import apiClient from './apiClient'
import type { AuthResponse } from '../types'

export const login = async (correoInstitucional: string, password: string): Promise<AuthResponse> => {
  const response = await apiClient.post<AuthResponse>('/auth/login', {
    correoInstitucional,
    password,
  })
  return response.data
}
