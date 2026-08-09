package com.icesi.fit.controller;

import com.icesi.fit.dto.RutinaDTO;
import com.icesi.fit.dto.RutinaRequestDTO;
import com.icesi.fit.exception.ResourceNotFoundException;
import com.icesi.fit.mapper.RutinaMapper;
import com.icesi.fit.model.Rutina;
import com.icesi.fit.service.RutinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Rutinas", description = "CRUD de rutinas de entrenamiento")
@RestController
@RequestMapping("/api/rutinas")
@RequiredArgsConstructor
public class RutinaController {

    private final RutinaService rutinaService;
    private final RutinaMapper rutinaMapper;

    @Operation(summary = "Listar todas las rutinas")
    @GetMapping
    public ResponseEntity<List<RutinaDTO>> getAllRutinas() {
        List<RutinaDTO> dtos = rutinaService.findAllRutinas().stream()
                .map(rutinaMapper::entityToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener rutina por ID")
    @GetMapping("/{id}")
    public ResponseEntity<RutinaDTO> getRutinaById(@PathVariable Long id) {
        Rutina rutina = rutinaService.findRutinaById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rutina no encontrada con id: " + id));
        return ResponseEntity.ok(rutinaMapper.entityToDto(rutina));
    }

    @Operation(summary = "Crear rutina")
    @PostMapping
    public ResponseEntity<RutinaDTO> createRutina(@RequestBody RutinaRequestDTO dto) {
        Rutina saved = rutinaService.saveRutina(rutinaMapper.requestDtoToEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(rutinaMapper.entityToDto(saved));
    }

    @Operation(summary = "Actualizar rutina")
    @PutMapping("/{id}")
    public ResponseEntity<RutinaDTO> updateRutina(@PathVariable Long id, @RequestBody RutinaRequestDTO dto) {
        rutinaService.findRutinaById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rutina no encontrada con id: " + id));
        Rutina entity = rutinaMapper.requestDtoToEntity(dto);
        entity.setId(id);
        return ResponseEntity.ok(rutinaMapper.entityToDto(rutinaService.saveRutina(entity)));
    }

    @Operation(summary = "Eliminar rutina")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRutina(@PathVariable Long id) {
        rutinaService.findRutinaById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rutina no encontrada con id: " + id));
        rutinaService.deleteRutina(id);
        return ResponseEntity.noContent().build();
    }
}
