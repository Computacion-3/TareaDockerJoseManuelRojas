export interface AuthUser {
  id: number
  correoInstitucional: string
  rol: string
}

export interface AuthResponse {
  id: number
  token: string
  correoInstitucional: string
  rol: string
}

export interface Usuario {
  id: number
  nombre: string
  correoInstitucional: string
  rolId: number
  rolNombre?: string
  entrenadorAsignadoId?: number
}

export interface UsuarioRequest {
  nombre: string
  correoInstitucional: string
  password: string
  rolId: number
}

export interface Rutina {
  id: number
  nombre: string
  esPredisenada: boolean
  creadorId: number
  creadorNombre?: string
}

export interface RutinaRequest {
  nombre: string
  esPredisenada: boolean
  creadorId: number
}

export interface Ejercicio {
  id: number
  nombre: string
  tipo: string
  descripcion: string
  duracionMinutos: number
  dificultad: string
  urlVideo: string
}

export interface EjercicioRequest {
  nombre: string
  tipo: string
  descripcion: string
  duracionMinutos: number
  dificultad: string
  urlVideo: string
}

export interface Progreso {
  id: number
  usuarioId: number
  fecha: string
  repeticionesRealizadas: number
  tiempoRealizadoMinutos: number
  nivelEsfuerzo: number
}

export interface ProgresoRequest {
  usuarioId: number
  fecha: string
  repeticionesRealizadas: number
  tiempoRealizadoMinutos: number
  nivelEsfuerzo: number
}

export interface Notificacion {
  id: number
  emisorId: number
  receptorId: number
  tipo: string
  contenido: string
  leido: boolean
  fechaEnvio: string
}

export interface NotificacionRequest {
  emisorId: number
  receptorId: number
  tipo: string
  contenido: string
  fechaEnvio: string
}

export interface Evento {
  id: number
  nombre: string
  descripcion: string
  fechaHora: string
  lugar: string
  administradorId: number
}

export interface EventoRequest {
  nombre: string
  descripcion: string
  fechaHora: string
  lugar: string
  administradorId: number
}

export interface Recomendacion {
  id: number
  contenido: string
  entrenadorId: number
  estudianteId: number
}

export interface RecomendacionRequest {
  contenido: string
  entrenadorId: number
  estudianteId: number
}

export interface Rol {
  id: number
  nombre: string
  descripcion: string
}
