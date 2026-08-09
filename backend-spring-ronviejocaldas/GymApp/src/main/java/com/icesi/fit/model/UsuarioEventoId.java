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
public class UsuarioEventoId implements Serializable {

    @Column(name = "id_usuario")
    private Long usuarioId;

    @Column(name = "id_evento")
    private Long eventoId;
}
