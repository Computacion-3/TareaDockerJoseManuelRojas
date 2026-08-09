package com.icesi.fit.mapper;

import com.icesi.fit.dto.PermisoDTO;
import com.icesi.fit.dto.PermisoRequestDTO;
import com.icesi.fit.model.Permiso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermisoMapper {
    PermisoDTO entityToDto(Permiso permiso);

    @Mapping(target = "roles", ignore = true)
    Permiso dtoToEntity(PermisoDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Permiso requestDtoToEntity(PermisoRequestDTO dto);
}
