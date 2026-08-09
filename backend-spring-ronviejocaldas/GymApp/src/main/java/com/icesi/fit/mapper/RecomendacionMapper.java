package com.icesi.fit.mapper;

import com.icesi.fit.dto.RecomendacionDTO;
import com.icesi.fit.dto.RecomendacionRequestDTO;
import com.icesi.fit.model.Recomendacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecomendacionMapper {

    @Mapping(source = "entrenador.id", target = "entrenadorId")
    @Mapping(source = "usuarioAsignado.id", target = "usuarioAsignadoId")
    RecomendacionDTO entityToDto(Recomendacion recomendacion);

    @Mapping(source = "entrenadorId", target = "entrenador.id")
    @Mapping(source = "usuarioAsignadoId", target = "usuarioAsignado.id")
    Recomendacion dtoToEntity(RecomendacionDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "entrenadorId", target = "entrenador.id")
    @Mapping(source = "usuarioAsignadoId", target = "usuarioAsignado.id")
    @Mapping(target = "fecha", ignore = true)
    Recomendacion requestDtoToEntity(RecomendacionRequestDTO dto);
}
