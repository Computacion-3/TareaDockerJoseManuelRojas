package com.icesi.fit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RutinaDTO {
    private Long id;
    private String nombre;
    private Boolean esPredisenada;
    private Long creadorId;
}
