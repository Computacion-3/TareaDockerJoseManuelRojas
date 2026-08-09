package com.icesi.fit.service;

import com.icesi.fit.model.Recomendacion;
import com.icesi.fit.repository.RecomendacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecomendacionService {

    private final RecomendacionRepository recomendacionRepository;

    public Recomendacion saveRecomendacion(Recomendacion recomendacion) {
        if (recomendacion.getFecha() == null) {
            recomendacion.setFecha(LocalDate.now());
        }
        return recomendacionRepository.save(recomendacion);
    }

    public Recomendacion updateRecomendacion(Long id, Recomendacion recomendacion) {
        Recomendacion existing = recomendacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recomendacion no encontrada con id: " + id));
        existing.setMensaje(recomendacion.getMensaje());
        if (recomendacion.getEntrenador() != null) existing.setEntrenador(recomendacion.getEntrenador());
        if (recomendacion.getUsuarioAsignado() != null) existing.setUsuarioAsignado(recomendacion.getUsuarioAsignado());
        if (recomendacion.getFecha() != null) existing.setFecha(recomendacion.getFecha());
        return recomendacionRepository.save(existing);
    }

    @Transactional(readOnly = true)
    public Optional<Recomendacion> findRecomendacionById(Long id) {
        return recomendacionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Recomendacion> findAllRecomendaciones() {
        return recomendacionRepository.findAll();
    }

    public void deleteRecomendacion(Long id) {
        if (!recomendacionRepository.existsById(id)) {
            throw new IllegalArgumentException("Recomendacion no encontrada con id: " + id);
        }
        recomendacionRepository.deleteById(id);
    }
}
