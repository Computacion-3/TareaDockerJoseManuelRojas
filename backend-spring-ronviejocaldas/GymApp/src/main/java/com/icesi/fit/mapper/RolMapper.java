package com.icesi.fit.mapper;

import com.icesi.fit.dto.RolDTO;
import com.icesi.fit.dto.RolRequestDTO;
import com.icesi.fit.model.Permiso;
import com.icesi.fit.model.Rol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RolMapper {

    @Mapping(source = "permisos", target = "permisoIds", qualifiedByName = "permisosToIds")
    RolDTO entityToDto(Rol rol);

    @Mapping(target = "permisos", ignore = true)
    Rol dtoToEntity(RolDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permisos", ignore = true)
    Rol requestDtoToEntity(RolRequestDTO dto);

    @Named("permisosToIds")
    default List<Long> permisosToIds(List<Permiso> permisos) {
        if (permisos == null) return List.of();
        return permisos.stream().map(Permiso::getId).toList();
    }
}
