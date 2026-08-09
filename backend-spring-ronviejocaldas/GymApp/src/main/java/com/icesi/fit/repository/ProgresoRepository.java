package com.icesi.fit.repository;

import com.icesi.fit.model.Progreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgresoRepository extends JpaRepository<Progreso, Long> {

    List<Progreso> findByUsuarioId(Long usuarioId);
}
