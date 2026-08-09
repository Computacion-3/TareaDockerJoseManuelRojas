package com.icesi.fit.mapper;

import com.icesi.fit.dto.ProgresoDTO;
import com.icesi.fit.dto.ProgresoRequestDTO;
import com.icesi.fit.model.Progreso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgresoMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    ProgresoDTO entityToDto(Progreso progreso);

    @Mapping(source = "usuarioId", target = "usuario.id")
    Progreso dtoToEntity(ProgresoDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "usuarioId", target = "usuario.id")
    Progreso requestDtoToEntity(ProgresoRequestDTO dto);
}
