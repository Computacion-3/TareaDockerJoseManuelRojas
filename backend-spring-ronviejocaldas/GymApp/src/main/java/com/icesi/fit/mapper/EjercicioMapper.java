package com.icesi.fit.mapper;

import com.icesi.fit.dto.EjercicioDTO;
import com.icesi.fit.dto.EjercicioRequestDTO;
import com.icesi.fit.model.Ejercicio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EjercicioMapper {
    EjercicioDTO entityToDto(Ejercicio ejercicio);

    @Mapping(target = "rutinaEjercicios", ignore = true)
    Ejercicio dtoToEntity(EjercicioDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rutinaEjercicios", ignore = true)
    Ejercicio requestDtoToEntity(EjercicioRequestDTO dto);
}
