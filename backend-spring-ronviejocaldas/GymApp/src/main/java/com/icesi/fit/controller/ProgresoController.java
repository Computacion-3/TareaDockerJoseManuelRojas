package com.icesi.fit.controller;

import com.icesi.fit.dto.ProgresoDTO;
import com.icesi.fit.dto.ProgresoRequestDTO;
import com.icesi.fit.exception.ResourceNotFoundException;
import com.icesi.fit.mapper.ProgresoMapper;
import com.icesi.fit.model.Progreso;
import com.icesi.fit.service.ProgresoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Progresos", description = "CRUD de progresos de usuarios")
@RestController
@RequestMapping("/api/progresos")
@RequiredArgsConstructor
public class ProgresoController {

    private final ProgresoService progresoService;
    private final ProgresoMapper progresoMapper;

    @Operation(summary = "Listar todos los progresos")
    @GetMapping
    public ResponseEntity<List<ProgresoDTO>> getAllProgresos() {
        List<ProgresoDTO> dtos = progresoService.findAllProgresos().stream()
                .map(progresoMapper::entityToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener progreso por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProgresoDTO> getProgresoById(@PathVariable Long id) {
        Progreso progreso = progresoService.findProgresoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progreso no encontrado con id: " + id));
        return ResponseEntity.ok(progresoMapper.entityToDto(progreso));
    }

    @Operation(summary = "Listar progresos de un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ProgresoDTO>> getProgresosByUsuario(@PathVariable Long usuarioId) {
        List<ProgresoDTO> dtos = progresoService.findByUsuarioId(usuarioId).stream()
                .map(progresoMapper::entityToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Registrar progreso")
    @PostMapping
    public ResponseEntity<ProgresoDTO> createProgreso(@RequestBody ProgresoRequestDTO dto) {
        Progreso saved = progresoService.saveProgreso(progresoMapper.requestDtoToEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(progresoMapper.entityToDto(saved));
    }

    @Operation(summary = "Actualizar progreso")
    @PutMapping("/{id}")
    public ResponseEntity<ProgresoDTO> updateProgreso(@PathVariable Long id, @RequestBody ProgresoRequestDTO dto) {
        progresoService.findProgresoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progreso no encontrado con id: " + id));
        Progreso entity = progresoMapper.requestDtoToEntity(dto);
        entity.setId(id);
        return ResponseEntity.ok(progresoMapper.entityToDto(progresoService.saveProgreso(entity)));
    }

    @Operation(summary = "Eliminar progreso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgreso(@PathVariable Long id) {
        progresoService.findProgresoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progreso no encontrado con id: " + id));
        progresoService.deleteProgreso(id);
        return ResponseEntity.noContent().build();
    }
}
