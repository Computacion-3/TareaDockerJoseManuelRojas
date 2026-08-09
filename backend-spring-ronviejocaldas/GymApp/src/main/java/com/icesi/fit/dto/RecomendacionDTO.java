package com.icesi.fit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacionDTO {
    private Long id;
    private Long entrenadorId;
    private Long usuarioAsignadoId;
    private String mensaje;
    private LocalDate fecha;
}
