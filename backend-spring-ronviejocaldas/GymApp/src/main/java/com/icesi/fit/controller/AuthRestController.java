package com.icesi.fit.controller;

import com.icesi.fit.dto.AuthRequestDTO;
import com.icesi.fit.dto.AuthResponseDTO;
import com.icesi.fit.repository.UsuarioRepository;
import com.icesi.fit.security.JwtUtil;
import com.icesi.fit.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticación", description = "Endpoint de login para obtener token JWT")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Login", description = "Retorna un token JWT con roles y fecha de expiración")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getCorreoInstitucional(), request.getPassword()
            )
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getCorreoInstitucional());
        String token = jwtUtil.generateToken(userDetails);
        String rol = userDetails.getAuthorities().stream()
                .filter(a -> a.getAuthority().startsWith("ROLE_"))
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");
        Long id = usuarioRepository.findByCorreoInstitucional(request.getCorreoInstitucional())
                .map(u -> u.getId())
                .orElse(null);
        return ResponseEntity.ok(new AuthResponseDTO(id, token, "Bearer", request.getCorreoInstitucional(), rol));
    }
}
