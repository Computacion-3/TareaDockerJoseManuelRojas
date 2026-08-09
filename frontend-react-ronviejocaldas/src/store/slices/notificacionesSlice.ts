import { createSlice, PayloadAction } from '@reduxjs/toolkit'

interface NotificacionesState {
  noLeidas: number
  mensajesWS: string[]
}

const initialState: NotificacionesState = {
  noLeidas: 0,
  mensajesWS: [],
}

const notificacionesSlice = createSlice({
  name: 'notificaciones',
  initialState,
  reducers: {
    setNoLeidas: (state, action: PayloadAction<number>) => {
      state.noLeidas = action.payload
    },
    incrementNoLeidas: (state) => {
      state.noLeidas += 1
    },
    resetNoLeidas: (state) => {
      state.noLeidas = 0
    },
    addMensajeWS: (state, action: PayloadAction<string>) => {
      state.mensajesWS.unshift(action.payload)
      state.noLeidas += 1
    },
    clearMensajesWS: (state) => {
      state.mensajesWS = []
    },
  },
})

export const { setNoLeidas, incrementNoLeidas, resetNoLeidas, addMensajeWS, clearMensajesWS } =
  notificacionesSlice.actions
export default notificacionesSlice.reducer
