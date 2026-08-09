package com.icesi.fit.dto;

import com.icesi.fit.model.TipoNotificacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionRequestDTO {
    private Long emisorId;
    private Long receptorId;
    private TipoNotificacion tipo;
    private String contenido;
}
