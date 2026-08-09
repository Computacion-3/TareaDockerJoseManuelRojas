package com.icesi.fit.service;

import com.icesi.fit.model.Notificacion;
import com.icesi.fit.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public Notificacion saveNotificacion(Notificacion notificacion) {
        notificacion.setLeido(false);
        notificacion.setFechaEnvio(LocalDateTime.now());
        return notificacionRepository.save(notificacion);
    }

    @Transactional(readOnly = true)
    public Optional<Notificacion> findNotificacionById(Long id) {
        return notificacionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Notificacion> findAllNotificaciones() {
        return notificacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Notificacion> findByReceptorId(Long receptorId) {
        return notificacionRepository.findByReceptorId(receptorId);
    }

    /**
     * Marks a notification as read.
     */
    public Notificacion markAsRead(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada con id: " + id));
        notificacion.setLeido(true);
        return notificacionRepository.save(notificacion);
    }

    public void deleteNotificacion(Long id) {
        if (!notificacionRepository.existsById(id)) {
            throw new IllegalArgumentException("Notificación no encontrada con id: " + id);
        }
        notificacionRepository.deleteById(id);
    }
}
