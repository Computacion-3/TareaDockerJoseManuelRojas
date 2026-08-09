package com.icesi.fit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolRequestDTO {
    private String nombre;
    private String descripcion;
    private List<Long> permisoIds;
}
