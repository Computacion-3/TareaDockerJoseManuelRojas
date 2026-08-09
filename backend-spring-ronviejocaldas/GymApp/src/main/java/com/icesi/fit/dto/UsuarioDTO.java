package com.icesi.fit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String correoInstitucional;
    private String nombre;
    private Long rolId;
    private String rolNombre;
    private Long entrenadorAsignadoId;
}
