package com.icesi.fit.service;

import com.icesi.fit.model.Progreso;
import com.icesi.fit.repository.ProgresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgresoService {

    private final ProgresoRepository progresoRepository;

    public Progreso saveProgreso(Progreso progreso) {
        return progresoRepository.save(progreso);
    }

    @Transactional(readOnly = true)
    public Optional<Progreso> findProgresoById(Long id) {
        return progresoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Progreso> findAllProgresos() {
        return progresoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Progreso> findByUsuarioId(Long usuarioId) {
        return progresoRepository.findByUsuarioId(usuarioId);
    }

    public void deleteProgreso(Long id) {
        if (!progresoRepository.existsById(id)) {
            throw new IllegalArgumentException("Progreso no encontrado con id: " + id);
        }
        progresoRepository.deleteById(id);
    }
}
