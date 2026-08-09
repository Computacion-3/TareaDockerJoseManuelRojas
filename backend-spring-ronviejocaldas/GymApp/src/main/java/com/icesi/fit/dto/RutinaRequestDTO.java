package com.icesi.fit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RutinaRequestDTO {
    private String nombre;
    private Boolean esPredisenada;
    private Long creadorId;
}
