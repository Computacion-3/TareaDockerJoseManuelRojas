import { useEffect } from 'react'
import { useAppDispatch, useAppSelector } from './reduxHooks'
import { addMensajeWS } from '../store/slices/notificacionesSlice'
import { connectWebSocket, disconnectWebSocket } from '../services/websocketService'

const useWebSocket = (userId: number | null) => {
  const dispatch = useAppDispatch()
  const mensajesWS = useAppSelector((state) => state.notificaciones.mensajesWS)

  useEffect(() => {
    if (!userId) return

    connectWebSocket(userId, (mensaje: string) => {
      dispatch(addMensajeWS(mensaje))
    })

    return () => {
      disconnectWebSocket()
    }
  }, [userId, dispatch])

  return { mensajesWS }
}

export default useWebSocket
