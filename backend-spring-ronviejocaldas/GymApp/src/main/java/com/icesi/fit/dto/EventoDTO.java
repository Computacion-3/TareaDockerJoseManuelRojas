package com.icesi.fit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaHora;
    private String lugar;
    private Long administradorId;
}
