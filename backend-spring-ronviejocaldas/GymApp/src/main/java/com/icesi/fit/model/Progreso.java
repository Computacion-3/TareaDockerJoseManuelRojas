package com.icesi.fit.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "progreso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Progreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_progreso")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "repeticiones_realizadas")
    private Integer repeticionesRealizadas;

    @Column(name = "tiempo_realizado_minutos")
    private Integer tiempoRealizadoMinutos;

    @Column(name = "nivel_esfuerzo")
    private Integer nivelEsfuerzo;
}
