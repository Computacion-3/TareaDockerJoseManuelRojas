import { useAppSelector, useAppDispatch } from './reduxHooks'
import { setCredentials, logout as logoutAction } from '../store/slices/authSlice'
import { login as loginService } from '../services/authService'
import type { AuthUser } from '../types'

const useAuth = () => {
  const dispatch = useAppDispatch()
  const token = useAppSelector((state) => state.auth.token)
  const user = useAppSelector((state) => state.auth.user)

  const login = async (correo: string, password: string): Promise<AuthUser> => {
    const data = await loginService(correo, password)
    const userData: AuthUser = {
      id: data.id,
      correoInstitucional: data.correoInstitucional,
      rol: data.rol,
    }
    dispatch(setCredentials({ token: data.token, user: userData }))
    return userData
  }

  const logout = () => {
    dispatch(logoutAction())
  }

  return { token, user, login, logout }
}

export default useAuth
