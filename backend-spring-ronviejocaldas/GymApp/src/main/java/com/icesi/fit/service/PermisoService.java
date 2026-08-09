package com.icesi.fit.service;

import com.icesi.fit.model.Permiso;
import com.icesi.fit.repository.PermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PermisoService {

    private final PermisoRepository permisoRepository;

    /**
     * Saves a permiso.
     */
    public Permiso savePermiso(Permiso permiso) {
        if (permiso.getNombre() == null || permiso.getNombre().isBlank()) {
            throw new IllegalArgumentException("El permiso debe tener un nombre.");
        }
        return permisoRepository.save(permiso);
    }

    /**
     * Finds a permiso by ID.
     */
    @Transactional(readOnly = true)
    public Optional<Permiso> findPermisoById(Long id) {
        return permisoRepository.findById(id);
    }

    /**
     * Finds a permiso by nombre.
     */
    @Transactional(readOnly = true)
    public Optional<Permiso> findPermisoByNombre(String nombre) {
        return permisoRepository.findByNombre(nombre);
    }

    /**
     * Returns all permisos.
     */
    @Transactional(readOnly = true)
    public List<Permiso> findAllPermisos() {
        return permisoRepository.findAll();
    }
    /**
        * Updates an existing permiso by ID.
        */
    public Permiso updatePermiso(Long id, Permiso permisoActualizado) {
        Permiso permisoExistente = permisoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Permiso no encontrado con id: " + id));

        if (permisoActualizado.getNombre() == null || permisoActualizado.getNombre().isBlank()) {
            throw new IllegalArgumentException("El permiso debe tener un nombre.");
        }

        permisoExistente.setNombre(permisoActualizado.getNombre());
        permisoExistente.setDescripcion(permisoActualizado.getDescripcion());

        return permisoRepository.save(permisoExistente);
    }

    /**
     * Deletes a permiso by ID.
     */
    public void deletePermiso(Long id) {
        if (!permisoRepository.existsById(id)) {
            throw new IllegalArgumentException("Permiso no encontrado con id: " + id);
        }
        permisoRepository.deleteById(id);
    }
}
