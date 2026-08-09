-- =============================================
-- Datos iniciales para Icesi Fit Application
-- =============================================

-- Permisos (id=1..9)
INSERT INTO permiso (nombre, descripcion) VALUES ('GESTIONAR_ENTRENADORES', 'Permite gestionar la lista de entrenadores');
INSERT INTO permiso (nombre, descripcion) VALUES ('GESTIONAR_BASE_DATOS', 'Permite gestionar la base de datos del sistema');
INSERT INTO permiso (nombre, descripcion) VALUES ('GESTIONAR_EVENTOS', 'Permite crear y administrar eventos deportivos');
INSERT INTO permiso (nombre, descripcion) VALUES ('VER_PROGRESO_ASIGNADOS', 'Permite ver el progreso de los usuarios asignados');
INSERT INTO permiso (nombre, descripcion) VALUES ('CREAR_RUTINA', 'Permite crear rutinas de ejercicios');
INSERT INTO permiso (nombre, descripcion) VALUES ('ENVIAR_RECOMENDACION', 'Permite enviar recomendaciones a usuarios asignados');
INSERT INTO permiso (nombre, descripcion) VALUES ('VER_PROGRESO_PROPIO', 'Permite ver el progreso propio del estudiante');
INSERT INTO permiso (nombre, descripcion) VALUES ('REGISTRAR_PROGRESO', 'Permite registrar progreso de ejercicios');
INSERT INTO permiso (nombre, descripcion) VALUES ('INSCRIBIRSE_EVENTO', 'Permite inscribirse en eventos deportivos');

-- Roles (id=1..3)
INSERT INTO rol (nombre, descripcion) VALUES ('ADMIN', 'Administrador del sistema con acceso completo');
INSERT INTO rol (nombre, descripcion) VALUES ('ENTRENADOR', 'Entrenador asignado a estudiantes');
INSERT INTO rol (nombre, descripcion) VALUES ('ESTUDIANTE', 'Estudiante que realiza actividades fisicas');

-- Rol-Permiso (many-to-many)
-- ADMIN: permisos 1,2,3
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (1, 1);
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (1, 2);
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (1, 3);
-- ENTRENADOR: permisos 4,5,6
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (2, 4);
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (2, 5);
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (2, 6);
-- ESTUDIANTE: permisos 7,8,9
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (3, 7);
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (3, 8);
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (3, 9);

-- Usuarios: 1 Admin (id=1)
INSERT INTO usuario (correo_institucional, nombre, password, enabled, id_rol) VALUES ('admin@icesi.edu.co', 'Carlos Admin', '$2a$10$np6D4CzqAVuvwtR6EPAxsubhh5rhGw41azhaqV44rKQWtEKSPuD9i', true, 1);

-- Usuarios: 2 Entrenadores (id=2, id=3)
INSERT INTO usuario (correo_institucional, nombre, password, enabled, id_rol) VALUES ('entrenador1@icesi.edu.co', 'Laura Entrenadora', '$2a$10$np6D4CzqAVuvwtR6EPAxsubhh5rhGw41azhaqV44rKQWtEKSPuD9i', true, 2);

INSERT INTO usuario (correo_institucional, nombre, password, enabled, id_rol) VALUES ('entrenador2@icesi.edu.co', 'Miguel Entrenador', '$2a$10$np6D4CzqAVuvwtR6EPAxsubhh5rhGw41azhaqV44rKQWtEKSPuD9i', true, 2);

-- Usuarios: 3 Estudiantes (id=4, id=5, id=6) con entrenador asignado
INSERT INTO usuario (correo_institucional, nombre, password, enabled, id_rol, id_entrenador_asignado) VALUES ('estudiante1@icesi.edu.co', 'Ana Estudiante', '$2a$10$np6D4CzqAVuvwtR6EPAxsubhh5rhGw41azhaqV44rKQWtEKSPuD9i', true, 3, 2);

INSERT INTO usuario (correo_institucional, nombre, password, enabled, id_rol, id_entrenador_asignado) VALUES ('estudiante2@icesi.edu.co', 'Pedro Estudiante', '$2a$10$np6D4CzqAVuvwtR6EPAxsubhh5rhGw41azhaqV44rKQWtEKSPuD9i', true, 3, 2);

INSERT INTO usuario (correo_institucional, nombre, password, enabled, id_rol, id_entrenador_asignado) VALUES ('estudiante3@icesi.edu.co', 'Maria Estudiante', '$2a$10$np6D4CzqAVuvwtR6EPAxsubhh5rhGw41azhaqV44rKQWtEKSPuD9i', true, 3, 3);

-- 5 Ejercicios base (id=1..5)
INSERT INTO ejercicio (nombre, tipo, descripcion, duracion_minutos, dificultad, url_video) VALUES ('Sentadillas', 'FUERZA', 'Ejercicio de fuerza para piernas y gluteos', 15, 'MEDIO', 'https://example.com/sentadillas');
INSERT INTO ejercicio (nombre, tipo, descripcion, duracion_minutos, dificultad, url_video) VALUES ('Correr en cinta', 'CARDIO', 'Ejercicio cardiovascular en cinta de correr', 30, 'FACIL', 'https://example.com/cinta');
INSERT INTO ejercicio (nombre, tipo, descripcion, duracion_minutos, dificultad, url_video) VALUES ('Peso muerto', 'FUERZA', 'Ejercicio compuesto de fuerza para espalda y piernas', 20, 'DIFICIL', 'https://example.com/peso-muerto');
INSERT INTO ejercicio (nombre, tipo, descripcion, duracion_minutos, dificultad, url_video) VALUES ('Yoga basico', 'MOVILIDAD', 'Rutina de yoga para movilidad y flexibilidad', 45, 'FACIL', 'https://example.com/yoga');
INSERT INTO ejercicio (nombre, tipo, descripcion, duracion_minutos, dificultad, url_video) VALUES ('Burpees', 'CARDIO', 'Ejercicio de alta intensidad para todo el cuerpo', 10, 'DIFICIL', 'https://example.com/burpees');

-- 2 Rutinas (id=1, id=2)
INSERT INTO rutina (id_creador, nombre, es_predisenada) VALUES (2, 'Rutina Fuerza Basica', true);
INSERT INTO rutina (id_creador, nombre, es_predisenada) VALUES (3, 'Rutina Cardio Intenso', true);

-- Rutina-Ejercicio
INSERT INTO rutina_ejercicio (id_rutina, id_ejercicio, series, repeticiones_sugeridas) VALUES (1, 1, 4, 12);
INSERT INTO rutina_ejercicio (id_rutina, id_ejercicio, series, repeticiones_sugeridas) VALUES (1, 3, 3, 8);
INSERT INTO rutina_ejercicio (id_rutina, id_ejercicio, series, repeticiones_sugeridas) VALUES (2, 2, 1, 1);
INSERT INTO rutina_ejercicio (id_rutina, id_ejercicio, series, repeticiones_sugeridas) VALUES (2, 5, 5, 10);

-- Progreso de estudiantes
INSERT INTO progreso (id_usuario, fecha, repeticiones_realizadas, tiempo_realizado_minutos, nivel_esfuerzo) VALUES (4, '2026-03-20', 40, 60, 7);
INSERT INTO progreso (id_usuario, fecha, repeticiones_realizadas, tiempo_realizado_minutos, nivel_esfuerzo) VALUES (4, '2026-03-21', 45, 55, 8);
INSERT INTO progreso (id_usuario, fecha, repeticiones_realizadas, tiempo_realizado_minutos, nivel_esfuerzo) VALUES (5, '2026-03-20', 30, 45, 6);
INSERT INTO progreso (id_usuario, fecha, repeticiones_realizadas, tiempo_realizado_minutos, nivel_esfuerzo) VALUES (6, '2026-03-21', 50, 70, 9);

-- 1 Evento (id=1)
INSERT INTO evento (nombre, descripcion, fecha_hora, lugar, id_administrador) VALUES ('Jornada Fitness Icesi', 'Gran jornada deportiva abierta a toda la comunidad universitaria', '2026-04-15 08:00:00', 'Gimnasio Principal - Universidad Icesi', 1);

-- Inscripciones a evento
INSERT INTO usuario_evento (id_usuario, id_evento, fecha_inscripcion) VALUES (4, 1, '2026-03-18 10:00:00');
INSERT INTO usuario_evento (id_usuario, id_evento, fecha_inscripcion) VALUES (5, 1, '2026-03-19 14:30:00');

-- Notificaciones
INSERT INTO notificacion (id_emisor, id_receptor, tipo, contenido, leido, fecha_envio) VALUES (2, 4, 'RECORDATORIO', 'Recuerda completar tu rutina de hoy', false, '2026-03-22 08:00:00');
INSERT INTO notificacion (id_emisor, id_receptor, tipo, contenido, leido, fecha_envio) VALUES (1, 2, 'EVENTO', 'Nuevo evento creado: Jornada Fitness Icesi', true, '2026-03-18 09:00:00');
INSERT INTO notificacion (id_emisor, id_receptor, tipo, contenido, leido, fecha_envio) VALUES (3, 6, 'PROGRESO', 'Tu progreso de esta semana ha sido excelente', false, '2026-03-22 10:00:00');

-- Recomendaciones
INSERT INTO recomendacion (id_entrenador, id_usuario_asignado, mensaje, fecha) VALUES (2, 4, 'Te recomiendo aumentar las repeticiones de sentadillas gradualmente para mejorar la resistencia', '2026-03-21');
INSERT INTO recomendacion (id_entrenador, id_usuario_asignado, mensaje, fecha) VALUES (3, 6, 'Incorpora ejercicios de estiramiento antes y despues de cada sesion de cardio', '2026-03-22');
