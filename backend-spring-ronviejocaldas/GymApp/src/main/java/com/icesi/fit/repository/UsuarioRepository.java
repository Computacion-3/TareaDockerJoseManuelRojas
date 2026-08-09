package com.icesi.fit.repository;

import com.icesi.fit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoInstitucional(String correoInstitucional);

    List<Usuario> findByEntrenadorAsignadoId(Long entrenadorId);
}
