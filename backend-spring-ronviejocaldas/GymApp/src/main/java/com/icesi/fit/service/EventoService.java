package com.icesi.fit.service;

import com.icesi.fit.model.Evento;
import com.icesi.fit.model.UsuarioEvento;
import com.icesi.fit.model.UsuarioEventoId;
import com.icesi.fit.model.Usuario;
import com.icesi.fit.repository.EventoRepository;
import com.icesi.fit.repository.UsuarioEventoRepository;
import com.icesi.fit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioEventoRepository usuarioEventoRepository;
    private final UsuarioRepository usuarioRepository;

    public Evento saveEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    @Transactional(readOnly = true)
    public Optional<Evento> findEventoById(Long id) {
        return eventoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Evento> findAllEventos() {
        return eventoRepository.findAll();
    }

    public void deleteEvento(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new IllegalArgumentException("Evento no encontrado con id: " + id);
        }
        eventoRepository.deleteById(id);
    }

    /**
     * Inscribes a user to an event.
     */
    public UsuarioEvento inscribirUsuario(Long eventoId, Long usuarioId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado con id: " + eventoId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + usuarioId));

        UsuarioEventoId ueId = new UsuarioEventoId(usuarioId, eventoId);
        if (usuarioEventoRepository.existsById(ueId)) {
            throw new IllegalArgumentException("El usuario ya está inscrito en este evento.");
        }

        UsuarioEvento ue = UsuarioEvento.builder()
                .id(ueId)
                .usuario(usuario)
                .evento(evento)
                .fechaInscripcion(LocalDateTime.now())
                .build();

        return usuarioEventoRepository.save(ue);
    }
}
