package com.icesi.fit.mapper;

import com.icesi.fit.dto.RutinaDTO;
import com.icesi.fit.dto.RutinaRequestDTO;
import com.icesi.fit.model.Rutina;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RutinaMapper {

    @Mapping(source = "creador.id", target = "creadorId")
    RutinaDTO entityToDto(Rutina rutina);

    @Mapping(source = "creadorId", target = "creador.id")
    @Mapping(target = "rutinaEjercicios", ignore = true)
    Rutina dtoToEntity(RutinaDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "creadorId", target = "creador.id")
    @Mapping(target = "rutinaEjercicios", ignore = true)
    Rutina requestDtoToEntity(RutinaRequestDTO dto);
}
