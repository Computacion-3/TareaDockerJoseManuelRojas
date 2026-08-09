package com.icesi.fit.controller;

import com.icesi.fit.dto.RecomendacionDTO;
import com.icesi.fit.dto.RecomendacionRequestDTO;
import com.icesi.fit.exception.ResourceNotFoundException;
import com.icesi.fit.mapper.RecomendacionMapper;
import com.icesi.fit.model.Recomendacion;
import com.icesi.fit.service.RecomendacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
@RequiredArgsConstructor
@Tag(name = "Recomendaciones", description = "Gestión de recomendaciones de entrenadores")
public class RecomendacionController {

    private final RecomendacionService recomendacionService;
    private final RecomendacionMapper recomendacionMapper;

    @GetMapping
    @Operation(summary = "Listar todas las recomendaciones")
    public ResponseEntity<List<RecomendacionDTO>> getAllRecomendaciones() {
        List<RecomendacionDTO> dtos = recomendacionService.findAllRecomendaciones().stream()
                .map(recomendacionMapper::entityToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener recomendación por ID")
    public ResponseEntity<RecomendacionDTO> getRecomendacionById(@PathVariable Long id) {
        Recomendacion recomendacion = recomendacionService.findRecomendacionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recomendación no encontrada con id: " + id));
        return ResponseEntity.ok(recomendacionMapper.entityToDto(recomendacion));
    }

    @PostMapping
    @Operation(summary = "Crear nueva recomendación")
    public ResponseEntity<RecomendacionDTO> createRecomendacion(@RequestBody RecomendacionRequestDTO dto) {
        Recomendacion entity = recomendacionMapper.requestDtoToEntity(dto);
        Recomendacion saved = recomendacionService.saveRecomendacion(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(recomendacionMapper.entityToDto(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar recomendación existente")
    public ResponseEntity<RecomendacionDTO> updateRecomendacion(@PathVariable Long id, @RequestBody RecomendacionRequestDTO dto) {
        Recomendacion entity = recomendacionMapper.requestDtoToEntity(dto);
        Recomendacion updated = recomendacionService.updateRecomendacion(id, entity);
        return ResponseEntity.ok(recomendacionMapper.entityToDto(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar recomendación por ID")
    public ResponseEntity<Void> deleteRecomendacion(@PathVariable Long id) {
        recomendacionService.deleteRecomendacion(id);
        return ResponseEntity.noContent().build();
    }
}
