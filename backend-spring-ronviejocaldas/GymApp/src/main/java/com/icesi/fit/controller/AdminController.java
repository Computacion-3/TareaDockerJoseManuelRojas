package com.icesi.fit.controller;

import com.icesi.fit.service.PermisoService;
import com.icesi.fit.service.RolService;
import com.icesi.fit.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final PermisoService permisoService;

    @GetMapping
    public String admin(Model model) {
        model.addAttribute("totalUsuarios", usuarioService.findAllUsuarios().size());
        model.addAttribute("totalRoles", rolService.findAllRoles().size());
        model.addAttribute("totalPermisos", permisoService.findAllPermisos().size());
        return "admin/dashboard";
    }
}