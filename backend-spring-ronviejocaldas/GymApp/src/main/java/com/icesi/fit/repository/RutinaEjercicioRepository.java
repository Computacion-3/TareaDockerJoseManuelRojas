package com.icesi.fit.repository;

import com.icesi.fit.model.RutinaEjercicio;
import com.icesi.fit.model.RutinaEjercicioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutinaEjercicioRepository extends JpaRepository<RutinaEjercicio, RutinaEjercicioId> {
}
