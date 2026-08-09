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
public class ProgresoDTO {
    private Long id;
    private Long usuarioId;
    private LocalDate fecha;
    private Integer repeticionesRealizadas;
    private Integer tiempoRealizadoMinutos;
    private Integer nivelEsfuerzo;
}
