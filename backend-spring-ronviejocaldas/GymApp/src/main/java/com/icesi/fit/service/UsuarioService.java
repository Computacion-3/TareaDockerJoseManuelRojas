package com.icesi.fit.service;

import com.icesi.fit.model.Progreso;
import com.icesi.fit.model.Rol;
import com.icesi.fit.model.Usuario;
import com.icesi.fit.repository.ProgresoRepository;
import com.icesi.fit.repository.RolRepository;
import com.icesi.fit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ProgresoRepository progresoRepository;
    private final RolRepository rolRepository;

    /**
     * Saves a usuario. Validates that a Rol is always assigned.
     * Business rule: "Evite usuarios sin Rol".
     */
    public Usuario saveUsuario(Usuario usuario) {
        if (usuario.getRol() == null) {
            throw new IllegalArgumentException("El usuario debe tener un rol asignado.");
        }

        Rol rol = rolRepository.findById(usuario.getRol().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Rol no encontrado con id: " + usuario.getRol().getId()));

        usuario.setRol(rol);
        return usuarioRepository.save(usuario);
    }

    /**
     * Finds a usuario by ID.
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> findUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Returns all usuarios.
     */
    @Transactional(readOnly = true)
    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Updates an existing usuario by ID.
     * Validates that the usuario exists and that the assigned Rol also exists.
     */
    public Usuario updateUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));

        if (usuarioActualizado.getNombre() != null && !usuarioActualizado.getNombre().isBlank()) {
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
        }

        if (usuarioActualizado.getCorreoInstitucional() != null
                && !usuarioActualizado.getCorreoInstitucional().isBlank()) {
            usuarioExistente.setCorreoInstitucional(usuarioActualizado.getCorreoInstitucional());
        }

        if (usuarioActualizado.getPassword() != null
                && !usuarioActualizado.getPassword().isBlank()) {
            usuarioExistente.setPassword(usuarioActualizado.getPassword());
        }

        usuarioExistente.setEnabled(usuarioActualizado.isEnabled());

        if (usuarioActualizado.getRol() != null) {
            Rol rol = rolRepository.findById(usuarioActualizado.getRol().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Rol no encontrado con id: " + usuarioActualizado.getRol().getId()));
            usuarioExistente.setRol(rol);
        }

        if (usuarioActualizado.getEntrenadorAsignado() != null) {
            Usuario entrenador = usuarioRepository.findById(usuarioActualizado.getEntrenadorAsignado().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Entrenador no encontrado con id: " + usuarioActualizado.getEntrenadorAsignado().getId()));

            if (entrenador.getRol() == null ||
                    !"ENTRENADOR".equalsIgnoreCase(entrenador.getRol().getNombre())) {
                throw new IllegalArgumentException(
                        "El usuario con id " + entrenador.getId() + " no tiene rol de ENTRENADOR.");
            }

            usuarioExistente.setEntrenadorAsignado(entrenador);
        }

        return usuarioRepository.save(usuarioExistente);
    }

    /**
     * Assigns a trainer to a user.
     * Validates the trainer exists and has the ENTRENADOR role.
     */
    public Usuario assignEntrenador(Long usuarioId, Long entrenadorId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + usuarioId));

        Usuario entrenador = usuarioRepository.findById(entrenadorId)
                .orElseThrow(() -> new IllegalArgumentException("Entrenador no encontrado con id: " + entrenadorId));

        if (!"ENTRENADOR".equalsIgnoreCase(entrenador.getRol().getNombre())) {
            throw new IllegalArgumentException("El usuario con id " + entrenadorId + " no tiene rol de ENTRENADOR.");
        }

        usuario.setEntrenadorAsignado(entrenador);
        return usuarioRepository.save(usuario);
    }

    /**
     * Gets the progress of all users assigned to a given trainer.
     * Validates the trainer has the ENTRENADOR role.
     */
    @Transactional(readOnly = true)
    public List<Progreso> getProgresoDeAsignados(Long entrenadorId) {
        Usuario entrenador = usuarioRepository.findById(entrenadorId)
                .orElseThrow(() -> new IllegalArgumentException("Entrenador no encontrado con id: " + entrenadorId));

        if (!"ENTRENADOR".equalsIgnoreCase(entrenador.getRol().getNombre())) {
            throw new IllegalArgumentException("El usuario con id " + entrenadorId + " no tiene rol de ENTRENADOR.");
        }

        List<Usuario> asignados = usuarioRepository.findByEntrenadorAsignadoId(entrenadorId);
        return asignados.stream()
                .flatMap(u -> progresoRepository.findByUsuarioId(u.getId()).stream())
                .toList();
    }

    /**
     * Deletes a usuario by ID.
     */
    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}