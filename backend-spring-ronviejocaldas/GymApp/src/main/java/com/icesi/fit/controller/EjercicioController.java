package com.icesi.fit.controller;

import com.icesi.fit.dto.EjercicioDTO;
import com.icesi.fit.dto.EjercicioRequestDTO;
import com.icesi.fit.exception.ResourceNotFoundException;
import com.icesi.fit.mapper.EjercicioMapper;
import com.icesi.fit.model.Ejercicio;
import com.icesi.fit.service.EjercicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Ejercicios", description = "CRUD de ejercicios")
@RestController
@RequestMapping("/api/ejercicios")
@RequiredArgsConstructor
public class EjercicioController {

    private final EjercicioService ejercicioService;
    private final EjercicioMapper ejercicioMapper;

    @Operation(summary = "Listar todos los ejercicios")
    @GetMapping
    public ResponseEntity<List<EjercicioDTO>> getAllEjercicios() {
        List<EjercicioDTO> dtos = ejercicioService.findAllEjercicios().stream()
                .map(ejercicioMapper::entityToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener ejercicio por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EjercicioDTO> getEjercicioById(@PathVariable Long id) {
        Ejercicio ejercicio = ejercicioService.findEjercicioById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado con id: " + id));
        return ResponseEntity.ok(ejercicioMapper.entityToDto(ejercicio));
    }

    @Operation(summary = "Crear ejercicio")
    @PostMapping
    public ResponseEntity<EjercicioDTO> createEjercicio(@RequestBody EjercicioRequestDTO dto) {
        Ejercicio saved = ejercicioService.saveEjercicio(ejercicioMapper.requestDtoToEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(ejercicioMapper.entityToDto(saved));
    }

    @Operation(summary = "Actualizar ejercicio")
    @PutMapping("/{id}")
    public ResponseEntity<EjercicioDTO> updateEjercicio(@PathVariable Long id, @RequestBody EjercicioRequestDTO dto) {
        ejercicioService.findEjercicioById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado con id: " + id));
        Ejercicio entity = ejercicioMapper.requestDtoToEntity(dto);
        entity.setId(id);
        return ResponseEntity.ok(ejercicioMapper.entityToDto(ejercicioService.saveEjercicio(entity)));
    }

    @Operation(summary = "Eliminar ejercicio")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEjercicio(@PathVariable Long id) {
        ejercicioService.findEjercicioById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado con id: " + id));
        ejercicioService.deleteEjercicio(id);
        return ResponseEntity.noContent().build();
    }
}
