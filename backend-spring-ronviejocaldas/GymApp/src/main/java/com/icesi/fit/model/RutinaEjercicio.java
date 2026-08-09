package com.icesi.fit.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rutina_ejercicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RutinaEjercicio {

    @EmbeddedId
    private RutinaEjercicioId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rutinaId")
    @JoinColumn(name = "id_rutina")
    private Rutina rutina;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ejercicioId")
    @JoinColumn(name = "id_ejercicio")
    private Ejercicio ejercicio;

    @Column(nullable = false)
    private Integer series;

    @Column(name = "repeticiones_sugeridas", nullable = false)
    private Integer repeticionesSugeridas;
}
