package com.icesi.fit.controller;

import com.icesi.fit.dto.EventoDTO;
import com.icesi.fit.dto.EventoRequestDTO;
import com.icesi.fit.exception.ResourceNotFoundException;
import com.icesi.fit.mapper.EventoMapper;
import com.icesi.fit.model.Evento;
import com.icesi.fit.model.UsuarioEvento;
import com.icesi.fit.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "Gestión de eventos del gimnasio")
public class EventoController {

    private final EventoService eventoService;
    private final EventoMapper eventoMapper;

    @GetMapping
    @Operation(summary = "Listar todos los eventos")
    public ResponseEntity<List<EventoDTO>> getAllEventos() {
        List<EventoDTO> dtos = eventoService.findAllEventos().stream()
                .map(eventoMapper::entityToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener evento por ID")
    public ResponseEntity<EventoDTO> getEventoById(@PathVariable Long id) {
        Evento evento = eventoService.findEventoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + id));
        return ResponseEntity.ok(eventoMapper.entityToDto(evento));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo evento")
    public ResponseEntity<EventoDTO> createEvento(@RequestBody EventoRequestDTO dto) {
        Evento saved = eventoService.saveEvento(eventoMapper.requestDtoToEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoMapper.entityToDto(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar evento existente")
    public ResponseEntity<EventoDTO> updateEvento(@PathVariable Long id, @RequestBody EventoRequestDTO dto) {
        eventoService.findEventoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + id));
        Evento entity = eventoMapper.requestDtoToEntity(dto);
        entity.setId(id);
        return ResponseEntity.ok(eventoMapper.entityToDto(eventoService.saveEvento(entity)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar evento por ID")
    public ResponseEntity<Void> deleteEvento(@PathVariable Long id) {
        eventoService.deleteEvento(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{eventoId}/inscribir/{usuarioId}")
    @Operation(summary = "Inscribir usuario en un evento")
    public ResponseEntity<UsuarioEvento> inscribirUsuario(@PathVariable Long eventoId, @PathVariable Long usuarioId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.inscribirUsuario(eventoId, usuarioId));
    }
}
