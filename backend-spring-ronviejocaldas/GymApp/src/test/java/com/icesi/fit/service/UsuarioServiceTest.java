package com.icesi.fit.service;

import com.icesi.fit.model.Permiso;
import com.icesi.fit.model.Progreso;
import com.icesi.fit.model.Rol;
import com.icesi.fit.model.Usuario;
import com.icesi.fit.repository.ProgresoRepository;
import com.icesi.fit.repository.RolRepository;
import com.icesi.fit.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

        @Mock
        private UsuarioRepository usuarioRepository;

        @Mock
        private ProgresoRepository progresoRepository;

        @Mock
        private RolRepository rolRepository;

        @InjectMocks
        private UsuarioService usuarioService;

        private Rol rolEntrenador;
        private Rol rolEstudiante;
        private Rol rolAdmin;
        private Usuario estudiante;
        private Usuario entrenador;

        @BeforeEach
        void setUp() {
                Permiso p1 = Permiso.builder().id(1L).nombre("GESTIONAR_ENTRENADORES").build();
                Permiso p2 = Permiso.builder().id(4L).nombre("VER_PROGRESO_ASIGNADOS").build();
                Permiso p3 = Permiso.builder().id(7L).nombre("VER_PROGRESO_PROPIO").build();

                rolAdmin = Rol.builder()
                                .id(1L).nombre("ADMIN").descripcion("Administrador")
                                .permisos(List.of(p1))
                                .build();

                rolEntrenador = Rol.builder()
                                .id(2L).nombre("ENTRENADOR").descripcion("Entrenador")
                                .permisos(List.of(p2))
                                .build();

                rolEstudiante = Rol.builder()
                                .id(3L).nombre("ESTUDIANTE").descripcion("Estudiante")
                                .permisos(List.of(p3))
                                .build();

                entrenador = Usuario.builder()
                                .id(1L)
                                .nombre("Laura Entrenadora")
                                .correoInstitucional("entrenador1@icesi.edu.co")
                                .rol(rolEntrenador)
                                .build();

                estudiante = Usuario.builder()
                                .id(2L)
                                .nombre("Ana Estudiante")
                                .correoInstitucional("estudiante1@icesi.edu.co")
                                .rol(rolEstudiante)
                                .build();
        }

        // ========== saveUsuario ==========

        @Test
        void saveUsuario_withValidRol_shouldSaveSuccessfully() {
                when(rolRepository.findById(3L)).thenReturn(Optional.of(rolEstudiante));
                when(usuarioRepository.save(any(Usuario.class))).thenReturn(estudiante);

                Usuario result = usuarioService.saveUsuario(estudiante);

                assertNotNull(result);
                assertEquals("Ana Estudiante", result.getNombre());
                assertEquals("ESTUDIANTE", result.getRol().getNombre());
                verify(usuarioRepository, times(1)).save(estudiante);
        }

        @Test
        void saveUsuario_withoutRol_shouldThrowException() {
                Usuario sinRol = Usuario.builder()
                                .nombre("Sin Rol")
                                .correoInstitucional("sinrol@icesi.edu.co")
                                .build();

                IllegalArgumentException ex = assertThrows(
                                IllegalArgumentException.class,
                                () -> usuarioService.saveUsuario(sinRol));

                assertEquals("El usuario debe tener un rol asignado.", ex.getMessage());
                verify(usuarioRepository, never()).save(any());
        }

        @Test
        void saveUsuario_withNonExistentRol_shouldThrowException() {
                Rol rolInvalido = Rol.builder().id(99L).nombre("INEXISTENTE").build();
                Usuario conRolInvalido = Usuario.builder()
                                .nombre("Usuario")
                                .correoInstitucional("usr@icesi.edu.co")
                                .rol(rolInvalido)
                                .build();

                when(rolRepository.findById(99L)).thenReturn(Optional.empty());

                assertThrows(
                                IllegalArgumentException.class,
                                () -> usuarioService.saveUsuario(conRolInvalido));
                verify(usuarioRepository, never()).save(any());
        }

        // ========== findUsuarioById ==========

        @Test
        void findUsuarioById_existing_shouldReturnUsuario() {
                when(usuarioRepository.findById(2L)).thenReturn(Optional.of(estudiante));

                Optional<Usuario> result = usuarioService.findUsuarioById(2L);

                assertTrue(result.isPresent());
                assertEquals("Ana Estudiante", result.get().getNombre());
        }

        @Test
        void findUsuarioById_notExisting_shouldReturnEmpty() {
                when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

                Optional<Usuario> result = usuarioService.findUsuarioById(99L);

                assertFalse(result.isPresent());
        }

        // ========== findAllUsuarios ==========

        @Test
        void findAllUsuarios_shouldReturnList() {
                when(usuarioRepository.findAll()).thenReturn(Arrays.asList(entrenador, estudiante));

                List<Usuario> result = usuarioService.findAllUsuarios();

                assertEquals(2, result.size());
                verify(usuarioRepository, times(1)).findAll();
        }

        // ========== deleteUsuario ==========

        @Test
        void deleteUsuario_existing_shouldDeleteSuccessfully() {
                when(usuarioRepository.existsById(2L)).thenReturn(true);

                assertDoesNotThrow(() -> usuarioService.deleteUsuario(2L));

                verify(usuarioRepository, times(1)).deleteById(2L);
        }

        @Test
        void deleteUsuario_notExisting_shouldThrowException() {
                when(usuarioRepository.existsById(99L)).thenReturn(false);

                IllegalArgumentException ex = assertThrows(
                                IllegalArgumentException.class,
                                () -> usuarioService.deleteUsuario(99L));

                assertTrue(ex.getMessage().contains("99"));
                verify(usuarioRepository, never()).deleteById(anyLong());
        }

        // ========== assignEntrenador ==========

        @Test
        void assignEntrenador_validEntrenador_shouldAssignSuccessfully() {
                when(usuarioRepository.findById(2L)).thenReturn(Optional.of(estudiante));
                when(usuarioRepository.findById(1L)).thenReturn(Optional.of(entrenador));
                when(usuarioRepository.save(any(Usuario.class))).thenReturn(estudiante);

                Usuario result = usuarioService.assignEntrenador(2L, 1L);

                assertNotNull(result);
                verify(usuarioRepository, times(1)).save(estudiante);
        }

        @Test
        void assignEntrenador_notEntrenadorRol_shouldThrowException() {
                Usuario admin = Usuario.builder()
                                .id(3L)
                                .nombre("Admin")
                                .correoInstitucional("admin@icesi.edu.co")
                                .rol(rolAdmin)
                                .build();

                when(usuarioRepository.findById(2L)).thenReturn(Optional.of(estudiante));
                when(usuarioRepository.findById(3L)).thenReturn(Optional.of(admin));

                IllegalArgumentException ex = assertThrows(
                                IllegalArgumentException.class,
                                () -> usuarioService.assignEntrenador(2L, 3L));

                assertTrue(ex.getMessage().contains("ENTRENADOR"));
                verify(usuarioRepository, never()).save(any());
        }

        @Test
        void assignEntrenador_usuarioNotFound_shouldThrowException() {
                when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

                assertThrows(
                                IllegalArgumentException.class,
                                () -> usuarioService.assignEntrenador(99L, 1L));
        }

        @Test
        void assignEntrenador_entrenadorNotFound_shouldThrowException() {
                when(usuarioRepository.findById(2L)).thenReturn(Optional.of(estudiante));
                when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

                assertThrows(
                                IllegalArgumentException.class,
                                () -> usuarioService.assignEntrenador(2L, 99L));
        }

        // ========== getProgresoDeAsignados ==========

        @Test
        void getProgresoDeAsignados_validEntrenador_shouldReturnProgresos() {
                Progreso progreso1 = Progreso.builder()
                                .id(1L)
                                .usuario(estudiante)
                                .fecha(LocalDate.of(2026, 3, 20))
                                .repeticionesRealizadas(40)
                                .build();

                when(usuarioRepository.findById(1L)).thenReturn(Optional.of(entrenador));
                when(usuarioRepository.findByEntrenadorAsignadoId(1L))
                                .thenReturn(Collections.singletonList(estudiante));
                when(progresoRepository.findByUsuarioId(2L)).thenReturn(Collections.singletonList(progreso1));

                List<Progreso> result = usuarioService.getProgresoDeAsignados(1L);

                assertEquals(1, result.size());
                assertEquals(40, result.get(0).getRepeticionesRealizadas());
        }

        @Test
        void getProgresoDeAsignados_notEntrenador_shouldThrowException() {
                when(usuarioRepository.findById(2L)).thenReturn(Optional.of(estudiante));

                assertThrows(
                                IllegalArgumentException.class,
                                () -> usuarioService.getProgresoDeAsignados(2L));
        }

        @Test
        void getProgresoDeAsignados_entrenadorNotFound_shouldThrowException() {
                when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

                assertThrows(
                                IllegalArgumentException.class,
                                () -> usuarioService.getProgresoDeAsignados(99L));
        }
}
