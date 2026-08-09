import { Client, type IMessage } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

type MessageCallback = (message: string) => void

let stompClient: Client | null = null

export const connectWebSocket = (userId: number, onMessage: MessageCallback): void => {
  stompClient = new Client({
    webSocketFactory: () => new SockJS((import.meta.env.VITE_WS_URL || 'http://localhost:8080/ws')),
    reconnectDelay: 5000,
    onConnect: () => {
      stompClient?.subscribe(`/topic/notificaciones/${userId}`, (msg: IMessage) => {
        onMessage(msg.body)
      })
    },
  })
  stompClient.activate()
}

export const disconnectWebSocket = (): void => {
  if (stompClient?.active) {
    stompClient.deactivate()
    stompClient = null
  }
}
