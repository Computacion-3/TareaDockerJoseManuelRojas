package com.icesi.fit.mapper;

import com.icesi.fit.dto.NotificacionDTO;
import com.icesi.fit.dto.NotificacionRequestDTO;
import com.icesi.fit.model.Notificacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificacionMapper {

    @Mapping(source = "emisor.id", target = "emisorId")
    @Mapping(source = "receptor.id", target = "receptorId")
    NotificacionDTO entityToDto(Notificacion notificacion);

    @Mapping(source = "emisorId", target = "emisor.id")
    @Mapping(source = "receptorId", target = "receptor.id")
    Notificacion dtoToEntity(NotificacionDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "emisorId", target = "emisor.id")
    @Mapping(source = "receptorId", target = "receptor.id")
    @Mapping(target = "leido", ignore = true)
    @Mapping(target = "fechaEnvio", ignore = true)
    Notificacion requestDtoToEntity(NotificacionRequestDTO dto);
}
