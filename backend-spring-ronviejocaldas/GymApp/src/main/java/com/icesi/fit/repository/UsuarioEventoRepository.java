package com.icesi.fit.repository;

import com.icesi.fit.model.UsuarioEvento;
import com.icesi.fit.model.UsuarioEventoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioEventoRepository extends JpaRepository<UsuarioEvento, UsuarioEventoId> {
}
