package com.icesi.fit.mapper;

import com.icesi.fit.dto.UsuarioCreateDTO;
import com.icesi.fit.dto.UsuarioDTO;
import com.icesi.fit.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(source = "rol.id", target = "rolId")
    @Mapping(source = "rol.nombre", target = "rolNombre")
    @Mapping(source = "entrenadorAsignado.id", target = "entrenadorAsignadoId")
    UsuarioDTO entityToDto(Usuario usuario);

    @Mapping(source = "rolId", target = "rol.id")
    @Mapping(target = "entrenadorAsignado", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    Usuario createDtoToEntity(UsuarioCreateDTO dto);
}
