package com.icesi.fit.controller;

import com.icesi.fit.dto.PermisoDTO;
import com.icesi.fit.dto.PermisoRequestDTO;
import com.icesi.fit.exception.ResourceNotFoundException;
import com.icesi.fit.mapper.PermisoMapper;
import com.icesi.fit.model.Permiso;
import com.icesi.fit.service.PermisoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Permisos", description = "CRUD de permisos (solo ADMIN)")
@RestController
@RequestMapping("/api/permisos")
@RequiredArgsConstructor
public class PermisoRestController {

    private final PermisoService permisoService;
    private final PermisoMapper permisoMapper;

    @Operation(summary = "Listar todos los permisos")
    @GetMapping
    public ResponseEntity<List<PermisoDTO>> getAll() {
        List<PermisoDTO> dtos = permisoService.findAllPermisos().stream()
                .map(permisoMapper::entityToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener permiso por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PermisoDTO> getById(@PathVariable Long id) {
        Permiso permiso = permisoService.findPermisoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado con id: " + id));
        return ResponseEntity.ok(permisoMapper.entityToDto(permiso));
    }

    @Operation(summary = "Crear permiso")
    @PostMapping
    public ResponseEntity<PermisoDTO> create(@RequestBody PermisoRequestDTO dto) {
        Permiso saved = permisoService.savePermiso(permisoMapper.requestDtoToEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(permisoMapper.entityToDto(saved));
    }

    @Operation(summary = "Actualizar permiso")
    @PutMapping("/{id}")
    public ResponseEntity<PermisoDTO> update(@PathVariable Long id, @RequestBody PermisoRequestDTO dto) {
        permisoService.findPermisoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado con id: " + id));
        Permiso updated = permisoService.updatePermiso(id, permisoMapper.requestDtoToEntity(dto));
        return ResponseEntity.ok(permisoMapper.entityToDto(updated));
    }

    @Operation(summary = "Eliminar permiso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        permisoService.findPermisoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado con id: " + id));
        permisoService.deletePermiso(id);
        return ResponseEntity.noContent().build();
    }
}
