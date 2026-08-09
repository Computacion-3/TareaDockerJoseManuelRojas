package com.icesi.fit.controller;

import com.icesi.fit.dto.RolDTO;
import com.icesi.fit.dto.RolRequestDTO;
import com.icesi.fit.exception.ResourceNotFoundException;
import com.icesi.fit.mapper.RolMapper;
import com.icesi.fit.model.Permiso;
import com.icesi.fit.model.Rol;
import com.icesi.fit.service.PermisoService;
import com.icesi.fit.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Roles", description = "CRUD de roles (solo ADMIN)")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolRestController {

    private final RolService rolService;
    private final RolMapper rolMapper;
    private final PermisoService permisoService;

    @Operation(summary = "Listar todos los roles")
    @GetMapping
    public ResponseEntity<List<RolDTO>> getAll() {
        List<RolDTO> dtos = rolService.findAllRoles().stream()
                .map(rolMapper::entityToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener rol por ID")
    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> getById(@PathVariable Long id) {
        Rol rol = rolService.findRolById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
        return ResponseEntity.ok(rolMapper.entityToDto(rol));
    }

    @Operation(summary = "Crear rol")
    @PostMapping
    public ResponseEntity<RolDTO> create(@RequestBody RolRequestDTO dto) {
        Rol entity = rolMapper.requestDtoToEntity(dto);
        entity.setPermisos(resolvePermisos(dto.getPermisoIds()));
        Rol saved = rolService.saveRol(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(rolMapper.entityToDto(saved));
    }

    @Operation(summary = "Actualizar rol")
    @PutMapping("/{id}")
    public ResponseEntity<RolDTO> update(@PathVariable Long id, @RequestBody RolRequestDTO dto) {
        rolService.findRolById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
        Rol entity = rolMapper.requestDtoToEntity(dto);
        entity.setPermisos(resolvePermisos(dto.getPermisoIds()));
        Rol updated = rolService.updateRol(id, entity);
        return ResponseEntity.ok(rolMapper.entityToDto(updated));
    }

    @Operation(summary = "Eliminar rol")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rolService.findRolById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
        rolService.deleteRol(id);
        return ResponseEntity.noContent().build();
    }

    private List<Permiso> resolvePermisos(List<Long> permisoIds) {
        if (permisoIds == null || permisoIds.isEmpty()) {
            return List.of();
        }
        return permisoIds.stream()
                .map(pid -> permisoService.findPermisoById(pid)
                        .orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado con id: " + pid)))
                .toList();
    }
}
