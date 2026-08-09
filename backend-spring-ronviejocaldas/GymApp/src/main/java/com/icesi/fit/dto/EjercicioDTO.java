package com.icesi.fit.dto;

import com.icesi.fit.model.TipoEjercicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EjercicioDTO {
    private Long id;
    private String nombre;
    private TipoEjercicio tipo;
    private String descripcion;
    private Integer duracionMinutos;
    private String dificultad;
    private String urlVideo;
}
