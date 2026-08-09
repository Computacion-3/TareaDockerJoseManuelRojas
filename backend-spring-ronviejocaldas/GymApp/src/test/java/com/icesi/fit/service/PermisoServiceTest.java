package com.icesi.fit.service;

import com.icesi.fit.model.Permiso;
import com.icesi.fit.repository.PermisoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermisoServiceTest {

    @Mock
    private PermisoRepository permisoRepository;

    @InjectMocks
    private PermisoService permisoService;

    private Permiso permisoGestionar;
    private Permiso permisoCrear;

    @BeforeEach
    void setUp() {
        permisoGestionar = Permiso.builder()
                .id(1L).nombre("GESTIONAR_ENTRENADORES").descripcion("Gestionar entrenadores")
                .build();
        permisoCrear = Permiso.builder()
                .id(5L).nombre("CREAR_RUTINA").descripcion("Crear rutinas")
                .build();
    }

    // ========== savePermiso ==========

    @Test
    void savePermiso_withValidNombre_shouldSaveSuccessfully() {
        when(permisoRepository.save(any(Permiso.class))).thenReturn(permisoGestionar);

        Permiso result = permisoService.savePermiso(permisoGestionar);

        assertNotNull(result);
        assertEquals("GESTIONAR_ENTRENADORES", result.getNombre());
        verify(permisoRepository, times(1)).save(permisoGestionar);
    }

    @Test
    void savePermiso_withNullNombre_shouldThrowException() {
        Permiso sinNombre = Permiso.builder().descripcion("Sin nombre").build();

        assertThrows(
                IllegalArgumentException.class,
                () -> permisoService.savePermiso(sinNombre));
        verify(permisoRepository, never()).save(any());
    }

    @Test
    void savePermiso_withBlankNombre_shouldThrowException() {
        Permiso blancoNombre = Permiso.builder().nombre("  ").descripcion("Nombre en blanco").build();

        assertThrows(
                IllegalArgumentException.class,
                () -> permisoService.savePermiso(blancoNombre));
        verify(permisoRepository, never()).save(any());
    }

    // ========== findPermisoById ==========

    @Test
    void findPermisoById_existing_shouldReturnPermiso() {
        when(permisoRepository.findById(1L)).thenReturn(Optional.of(permisoGestionar));

        Optional<Permiso> result = permisoService.findPermisoById(1L);

        assertTrue(result.isPresent());
        assertEquals("GESTIONAR_ENTRENADORES", result.get().getNombre());
    }

    @Test
    void findPermisoById_notExisting_shouldReturnEmpty() {
        when(permisoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Permiso> result = permisoService.findPermisoById(99L);

        assertFalse(result.isPresent());
    }

    // ========== findPermisoByNombre ==========

    @Test
    void findPermisoByNombre_existing_shouldReturnPermiso() {
        when(permisoRepository.findByNombre("CREAR_RUTINA")).thenReturn(Optional.of(permisoCrear));

        Optional<Permiso> result = permisoService.findPermisoByNombre("CREAR_RUTINA");

        assertTrue(result.isPresent());
        assertEquals(5L, result.get().getId());
    }

    // ========== findAllPermisos ==========

    @Test
    void findAllPermisos_shouldReturnAll() {
        when(permisoRepository.findAll()).thenReturn(Arrays.asList(permisoGestionar, permisoCrear));

        List<Permiso> result = permisoService.findAllPermisos();

        assertEquals(2, result.size());
    }

    // ========== deletePermiso ==========

    @Test
    void deletePermiso_existing_shouldDeleteSuccessfully() {
        when(permisoRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> permisoService.deletePermiso(1L));

        verify(permisoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePermiso_notExisting_shouldThrowException() {
        when(permisoRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> permisoService.deletePermiso(99L));
        verify(permisoRepository, never()).deleteById(anyLong());
    }
}
