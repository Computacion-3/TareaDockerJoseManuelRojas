package com.icesi.fit.service;

import com.icesi.fit.model.Usuario;
import com.icesi.fit.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByCorreoInstitucional(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new org.springframework.security.core.userdetails.User(
                usuario.getCorreoInstitucional(),
                usuario.getPassword(),
                usuario.isEnabled(),
                true,
                true,
                true,
                getAuthorities(usuario)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Rol
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre()));

        // Permisos
        usuario.getRol().getPermisos().forEach(p ->
                authorities.add(new SimpleGrantedAuthority(p.getNombre()))
        );

        return authorities;
    }
}