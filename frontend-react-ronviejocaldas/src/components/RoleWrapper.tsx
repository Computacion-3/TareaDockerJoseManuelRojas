import { Navigate, Outlet } from 'react-router-dom'
import useAuth from '../hooks/useAuth'

interface RoleWrapperProps {
  allowedRoles: string[]
}

const RoleWrapper = ({ allowedRoles }: RoleWrapperProps) => {
  const { user, token } = useAuth()

  if (!token) {
    return <Navigate to="/login" replace />
  }

  if (!user || !allowedRoles.includes(user.rol)) {
    return <Navigate to="/unauthorized" replace />
  }

  return <Outlet />
}

export default RoleWrapper
