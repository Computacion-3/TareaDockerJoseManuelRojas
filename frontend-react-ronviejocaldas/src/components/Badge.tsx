const colorMap: Record<string, string> = {
  FÁCIL: 'bg-green-100 text-green-800',
  MEDIO: 'bg-yellow-100 text-yellow-800',
  DIFÍCIL: 'bg-red-100 text-red-800',
  CARDIO: 'bg-blue-100 text-blue-800',
  FUERZA: 'bg-purple-100 text-purple-800',
  FLEXIBILIDAD: 'bg-pink-100 text-pink-800',
  ROLE_ADMIN: 'bg-red-100 text-red-800',
  ROLE_ENTRENADOR: 'bg-blue-100 text-blue-800',
  ROLE_ESTUDIANTE: 'bg-green-100 text-green-800',
}

interface BadgeProps {
  value: string
  label?: string
}

const Badge = ({ value, label }: BadgeProps) => {
  const colorClass = colorMap[value] ?? 'bg-gray-100 text-gray-700'
  const displayText = label ?? value.replace('ROLE_', '')

  return (
    <span className={`inline-block text-xs font-medium px-2.5 py-0.5 rounded-full ${colorClass}`}>
      {displayText}
    </span>
  )
}

export default Badge
