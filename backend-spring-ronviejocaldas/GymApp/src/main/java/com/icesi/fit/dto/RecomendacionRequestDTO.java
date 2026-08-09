package com.icesi.fit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacionRequestDTO {
    private Long entrenadorId;
    private Long usuarioAsignadoId;
    private String mensaje;
}
