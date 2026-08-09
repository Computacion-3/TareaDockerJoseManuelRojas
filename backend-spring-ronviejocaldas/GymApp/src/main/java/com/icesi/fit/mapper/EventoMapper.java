package com.icesi.fit.mapper;

import com.icesi.fit.dto.EventoDTO;
import com.icesi.fit.dto.EventoRequestDTO;
import com.icesi.fit.model.Evento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    @Mapping(source = "administrador.id", target = "administradorId")
    EventoDTO entityToDto(Evento evento);

    @Mapping(source = "administradorId", target = "administrador.id")
    @Mapping(target = "inscripciones", ignore = true)
    Evento dtoToEntity(EventoDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "administradorId", target = "administrador.id")
    @Mapping(target = "inscripciones", ignore = true)
    Evento requestDtoToEntity(EventoRequestDTO dto);
}
