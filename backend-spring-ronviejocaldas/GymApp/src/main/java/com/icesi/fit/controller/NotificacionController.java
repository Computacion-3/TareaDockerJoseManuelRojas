package com.icesi.fit.controller;

import com.icesi.fit.dto.NotificacionDTO;
import com.icesi.fit.dto.NotificacionRequestDTO;
import com.icesi.fit.exception.ResourceNotFoundException;
import com.icesi.fit.mapper.NotificacionMapper;
import com.icesi.fit.model.Notificacion;
import com.icesi.fit.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Gestión de notificaciones entre usuarios")
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final NotificacionMapper notificacionMapper;

    @GetMapping
    @Operation(summary = "Listar todas las notificaciones")
    public ResponseEntity<List<NotificacionDTO>> getAllNotificaciones() {
        List<NotificacionDTO> dtos = notificacionService.findAllNotificaciones().stream()
                .map(notificacionMapper::entityToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener notificación por ID")
    public ResponseEntity<NotificacionDTO> getNotificacionById(@PathVariable Long id) {
        Notificacion notificacion = notificacionService.findNotificacionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id: " + id));
        return ResponseEntity.ok(notificacionMapper.entityToDto(notificacion));
    }

    @GetMapping("/receptor/{receptorId}")
    @Operation(summary = "Listar notificaciones por receptor")
    public ResponseEntity<List<NotificacionDTO>> getNotificacionesByReceptor(@PathVariable Long receptorId) {
        List<NotificacionDTO> dtos = notificacionService.findByReceptorId(receptorId).stream()
                .map(notificacionMapper::entityToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    @Operation(summary = "Crear nueva notificación")
    public ResponseEntity<NotificacionDTO> createNotificacion(@RequestBody NotificacionRequestDTO dto) {
        Notificacion entity = notificacionMapper.requestDtoToEntity(dto);
        Notificacion saved = notificacionService.saveNotificacion(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacionMapper.entityToDto(saved));
    }

    @PutMapping("/{id}/leer")
    @Operation(summary = "Marcar notificación como leída")
    public ResponseEntity<NotificacionDTO> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionMapper.entityToDto(notificacionService.markAsRead(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar notificación por ID")
    public ResponseEntity<Void> deleteNotificacion(@PathVariable Long id) {
        notificacionService.deleteNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
