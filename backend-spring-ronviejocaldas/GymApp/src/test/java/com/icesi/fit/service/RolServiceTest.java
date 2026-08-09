package com.icesi.fit.service;

import com.icesi.fit.model.Permiso;
import com.icesi.fit.model.Rol;
import com.icesi.fit.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    private Permiso permisoGestionar;
    private Permiso permisoCrear;
    private Rol rolAdmin;
    private Rol rolEntrenador;

    @BeforeEach
    void setUp() {
        permisoGestionar = Permiso.builder()
                .id(1L).nombre("GESTIONAR_ENTRENADORES").descripcion("Gestionar entrenadores")
                .build();
        permisoCrear = Permiso.builder()
                .id(5L).nombre("CREAR_RUTINA").descripcion("Crear rutinas")
                .build();

        rolAdmin = Rol.builder()
                .id(1L).nombre("ADMIN").descripcion("Administrador")
                .permisos(List.of(permisoGestionar))
                .build();

        rolEntrenador = Rol.builder()
                .id(2L).nombre("ENTRENADOR").descripcion("Entrenador")
                .permisos(List.of(permisoCrear))
                .build();
    }

    // ========== saveRol ==========

    @Test
    void saveRol_withPermisos_shouldSaveSuccessfully() {
        when(rolRepository.save(any(Rol.class))).thenReturn(rolAdmin);

        Rol result = rolService.saveRol(rolAdmin);

        assertNotNull(result);
        assertEquals("ADMIN", result.getNombre());
        assertFalse(result.getPermisos().isEmpty());
        verify(rolRepository, times(1)).save(rolAdmin);
    }

    @Test
    void saveRol_withoutPermisos_shouldThrowException() {
        Rol sinPermisos = Rol.builder()
                .nombre("SIN_PERMISOS")
                .descripcion("Rol sin permisos")
                .permisos(new ArrayList<>())
                .build();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> rolService.saveRol(sinPermisos));

        assertEquals("El rol debe tener al menos un permiso asignado.", ex.getMessage());
        verify(rolRepository, never()).save(any());
    }

    @Test
    void saveRol_withNullPermisos_shouldThrowException() {
        Rol nullPermisos = Rol.builder()
                .nombre("NULL_PERMISOS")
                .descripcion("Rol con permisos null")
                .build();
        nullPermisos.setPermisos(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> rolService.saveRol(nullPermisos));
        verify(rolRepository, never()).save(any());
    }

    // ========== findRolById ==========

    @Test
    void findRolById_existing_shouldReturnRol() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolAdmin));

        Optional<Rol> result = rolService.findRolById(1L);

        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getNombre());
    }

    @Test
    void findRolById_notExisting_shouldReturnEmpty() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Rol> result = rolService.findRolById(99L);

        assertFalse(result.isPresent());
    }

    // ========== findRolByNombre ==========

    @Test
    void findRolByNombre_existing_shouldReturnRol() {
        when(rolRepository.findByNombre("ADMIN")).thenReturn(Optional.of(rolAdmin));

        Optional<Rol> result = rolService.findRolByNombre("ADMIN");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    // ========== findAllRoles ==========

    @Test
    void findAllRoles_shouldReturnAll() {
        when(rolRepository.findAll()).thenReturn(Arrays.asList(rolAdmin, rolEntrenador));

        List<Rol> result = rolService.findAllRoles();

        assertEquals(2, result.size());
    }

    // ========== getPermisosByRolId ==========

    @Test
    void getPermisosByRolId_existing_shouldReturnPermisos() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolAdmin));

        List<Permiso> result = rolService.getPermisosByRolId(1L);

        assertEquals(1, result.size());
        assertEquals("GESTIONAR_ENTRENADORES", result.get(0).getNombre());
    }

    @Test
    void getPermisosByRolId_notExisting_shouldThrowException() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> rolService.getPermisosByRolId(99L));
    }

    // ========== deleteRol ==========

    @Test
    void deleteRol_existing_shouldDeleteSuccessfully() {
        when(rolRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> rolService.deleteRol(1L));

        verify(rolRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRol_notExisting_shouldThrowException() {
        when(rolRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> rolService.deleteRol(99L));
        verify(rolRepository, never()).deleteById(anyLong());
    }
}
