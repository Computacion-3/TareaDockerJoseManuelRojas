package com.icesi.fit.dto;

import com.icesi.fit.model.TipoNotificacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDTO {
    private Long id;
    private Long emisorId;
    private Long receptorId;
    private TipoNotificacion tipo;
    private String contenido;
    private Boolean leido;
    private LocalDateTime fechaEnvio;
}
