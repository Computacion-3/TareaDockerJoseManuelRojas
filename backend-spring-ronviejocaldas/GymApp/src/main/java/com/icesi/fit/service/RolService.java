package com.icesi.fit.service;

import com.icesi.fit.model.Permiso;
import com.icesi.fit.model.Rol;
import com.icesi.fit.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RolService {

    private final RolRepository rolRepository;

    /**
     * Saves a rol. Validates that it has at least one permiso assigned.
     * Business rule: "Evite Roles sin permisos".
     */
    public Rol saveRol(Rol rol) {
        if (rol.getPermisos() == null || rol.getPermisos().isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener al menos un permiso asignado.");
        }
        return rolRepository.save(rol);
    }

    /**
     * Finds a rol by ID.
     */
    @Transactional(readOnly = true)
    public Optional<Rol> findRolById(Long id) {
        return rolRepository.findById(id);
    }

    /**
     * Finds a rol by nombre.
     */
    @Transactional(readOnly = true)
    public Optional<Rol> findRolByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    /**
     * Returns all roles.
     */
    @Transactional(readOnly = true)
    public List<Rol> findAllRoles() {
        return rolRepository.findAll();
    }

    /**
     * Returns the permissions associated with a given rol.
     */
    @Transactional(readOnly = true)
    public List<Permiso> getPermisosByRolId(Long rolId) {
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con id: " + rolId));
        return rol.getPermisos();
    }
    /**
     * Updates an existing rol by ID.
        * Validates that the rol exists and has at least one permiso assigned.
        */
    public Rol updateRol(Long id, Rol rolActualizado) {
        Rol rolExistente = rolRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con id: " + id));

        if (rolActualizado.getNombre() != null && !rolActualizado.getNombre().isBlank()) {
            rolExistente.setNombre(rolActualizado.getNombre());
            }

        rolExistente.setDescripcion(rolActualizado.getDescripcion());

        if (rolActualizado.getPermisos() == null || rolActualizado.getPermisos().isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener al menos un permiso asignado.");
        }

        rolExistente.setPermisos(rolActualizado.getPermisos());

        return rolRepository.save(rolExistente);
    }

    /**
     * Deletes a rol by ID.
     */
    public void deleteRol(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new IllegalArgumentException("Rol no encontrado con id: " + id);
        }
        rolRepository.deleteById(id);
    }
}
