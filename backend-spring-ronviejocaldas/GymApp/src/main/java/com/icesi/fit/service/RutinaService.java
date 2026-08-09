package com.icesi.fit.service;

import com.icesi.fit.model.Rutina;
import com.icesi.fit.repository.RutinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RutinaService {

    private final RutinaRepository rutinaRepository;

    public Rutina saveRutina(Rutina rutina) {
        return rutinaRepository.save(rutina);
    }

    @Transactional(readOnly = true)
    public Optional<Rutina> findRutinaById(Long id) {
        return rutinaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Rutina> findAllRutinas() {
        return rutinaRepository.findAll();
    }

    public void deleteRutina(Long id) {
        if (!rutinaRepository.existsById(id)) {
            throw new IllegalArgumentException("Rutina no encontrada con id: " + id);
        }
        rutinaRepository.deleteById(id);
    }
}
