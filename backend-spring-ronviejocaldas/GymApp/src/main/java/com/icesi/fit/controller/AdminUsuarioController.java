package com.icesi.fit.controller;

import com.icesi.fit.model.Usuario;
import com.icesi.fit.model.Rol;
import com.icesi.fit.service.UsuarioService;
import com.icesi.fit.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
public class AdminUsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAllUsuarios());
        return "admin/usuarios";
    }

    @GetMapping("/{id}/editar")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.findUsuarioById(id).orElse(null);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/admin/usuarios";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.findAllRoles());
        return "admin/usuario-edit";
    }

    @PostMapping("/{id}/rol")
    public String updateUsuarioRol(@PathVariable Long id, @RequestParam Long rolId, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.findUsuarioById(id).orElse(null);
            if (usuario != null) {
                Rol rol = rolService.findRolById(rolId).orElse(null);
                if (rol != null) {
                    usuario.setRol(rol);
                    usuarioService.updateUsuario(usuario.getId(), usuario);
                    redirectAttributes.addFlashAttribute("success", "Rol de usuario actualizado correctamente.");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar rol de usuario.");
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar usuario.");
        }
        return "redirect:/admin/usuarios";
    }
}
