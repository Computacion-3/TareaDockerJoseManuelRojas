package com.icesi.fit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RutinaEjercicioId implements Serializable {

    @Column(name = "id_rutina")
    private Long rutinaId;

    @Column(name = "id_ejercicio")
    private Long ejercicioId;
}
