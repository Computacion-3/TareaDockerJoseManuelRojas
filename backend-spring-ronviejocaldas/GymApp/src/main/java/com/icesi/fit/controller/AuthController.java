package com.icesi.fit.controller;

import com.icesi.fit.model.Rol;
import com.icesi.fit.model.Usuario;
import com.icesi.fit.service.RolService;
import com.icesi.fit.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index(org.springframework.security.core.Authentication auth) {
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin";
            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ENTRENADOR"))) {
                return "redirect:/entrenador";
            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ESTUDIANTE"))) {
                return "redirect:/estudiante";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("roles", getRolesPermitidosParaRegistro());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String nombre,
                                  @RequestParam String correo,
                                  @RequestParam String password,
                                  @RequestParam Long rolId,
                                  Model model) {
        try {
            Optional<Rol> rol = rolService.findRolById(rolId);
            if (rol.isEmpty() || rol.get().getNombre().equalsIgnoreCase("ADMIN")) {
                model.addAttribute("error", "Rol inválido seleccionado.");
                model.addAttribute("roles", getRolesPermitidosParaRegistro());
                return "register";
            }

            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setCorreoInstitucional(correo);
            usuario.setPassword(passwordEncoder.encode(password));
            usuario.setRol(rol.get());
            usuario.setEnabled(true);

            usuarioService.saveUsuario(usuario);
            return "redirect:/login"; // Redirigir al login despues de registrar exitosamente
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear la cuenta: " + e.getMessage());
            model.addAttribute("roles", getRolesPermitidosParaRegistro());
            return "register";
        }
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    private java.util.List<Rol> getRolesPermitidosParaRegistro() {
        return rolService.findAllRoles().stream()
                .filter(r -> !r.getNombre().equalsIgnoreCase("ADMIN"))
                .toList();
    }
}