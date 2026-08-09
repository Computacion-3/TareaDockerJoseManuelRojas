package com.icesi.fit.controller;

import com.icesi.fit.dto.ProgresoDTO;
import com.icesi.fit.dto.UsuarioCreateDTO;
import com.icesi.fit.dto.UsuarioDTO;
import com.icesi.fit.exception.ResourceNotFoundException;
import com.icesi.fit.mapper.ProgresoMapper;
import com.icesi.fit.mapper.UsuarioMapper;
import com.icesi.fit.model.Usuario;
import com.icesi.fit.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios del gimnasio")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    private final ProgresoMapper progresoMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<UsuarioDTO> dtos = usuarioService.findAllUsuarios().stream()
                .map(usuarioMapper::entityToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioService.findUsuarioById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return ResponseEntity.ok(usuarioMapper.entityToDto(usuario));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario")
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioCreateDTO dto) {
        Usuario entity = usuarioMapper.createDtoToEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        Usuario saved = usuarioService.saveUsuario(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioMapper.entityToDto(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario existente")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long id, @RequestBody UsuarioCreateDTO dto) {
        Usuario entity = usuarioMapper.createDtoToEntity(dto);
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        Usuario updated = usuarioService.updateUsuario(id, entity);
        return ResponseEntity.ok(usuarioMapper.entityToDto(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario por ID")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/entrenador/{entrenadorId}")
    @Operation(summary = "Asignar entrenador a un usuario")
    public ResponseEntity<UsuarioDTO> assignEntrenador(@PathVariable Long id, @PathVariable Long entrenadorId) {
        return ResponseEntity.ok(usuarioMapper.entityToDto(usuarioService.assignEntrenador(id, entrenadorId)));
    }

    @GetMapping("/{entrenadorId}/asignados/progreso")
    @Operation(summary = "Obtener progreso de usuarios asignados a un entrenador")
    public ResponseEntity<List<ProgresoDTO>> getProgresoDeAsignados(@PathVariable Long entrenadorId) {
        List<ProgresoDTO> dtos = usuarioService.getProgresoDeAsignados(entrenadorId).stream()
                .map(progresoMapper::entityToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
