package com.icesi.fit.service;

import com.icesi.fit.model.Ejercicio;
import com.icesi.fit.repository.EjercicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EjercicioService {

    private final EjercicioRepository ejercicioRepository;

    public Ejercicio saveEjercicio(Ejercicio ejercicio) {
        return ejercicioRepository.save(ejercicio);
    }

    @Transactional(readOnly = true)
    public Optional<Ejercicio> findEjercicioById(Long id) {
        return ejercicioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Ejercicio> findAllEjercicios() {
        return ejercicioRepository.findAll();
    }

    public void deleteEjercicio(Long id) {
        if (!ejercicioRepository.existsById(id)) {
            throw new IllegalArgumentException("Ejercicio no encontrado con id: " + id);
        }
        ejercicioRepository.deleteById(id);
    }
}
