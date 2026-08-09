package com.icesi.fit.controller;

import com.icesi.fit.model.Rol;
import com.icesi.fit.model.Permiso;
import com.icesi.fit.service.RolService;
import com.icesi.fit.service.PermisoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class AdminRolController {

    private final RolService rolService;
    private final PermisoService permisoService;

    @GetMapping
    public String listarRoles(Model model) {
        model.addAttribute("roles", rolService.findAllRoles());
        return "admin/roles";
    }

    @GetMapping("/nuevo")
    public String showCreateForm(Model model) {
        model.addAttribute("permisos", permisoService.findAllPermisos());
        return "admin/rol-form";
    }

    @PostMapping
    public String createRol(@RequestParam String nombre, @RequestParam(required = false) String descripcion, 
                          @RequestParam(required = false) List<Long> permisosIds, RedirectAttributes redirectAttributes) {
        try {
            Rol rol = new Rol();
            rol.setNombre(nombre.toUpperCase());
            rol.setDescripcion(descripcion);
            
            if (permisosIds != null && !permisosIds.isEmpty()) {
                List<Permiso> permisos = new ArrayList<>();
                for (Long pId : permisosIds) {
                    Permiso p = permisoService.findPermisoById(pId).orElse(null);
                    if (p != null) permisos.add(p);
                }
                rol.setPermisos(permisos);
            }
            
            rolService.saveRol(rol);
            redirectAttributes.addFlashAttribute("success", "Rol creado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el rol.");
        }
        return "redirect:/admin/roles";
    }

    @GetMapping("/{id}/permisos")
    public String showEditPermisosForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Rol rol = rolService.findRolById(id).orElse(null);
        if (rol == null) {
            redirectAttributes.addFlashAttribute("error", "Rol no encontrado.");
            return "redirect:/admin/roles";
        }
        model.addAttribute("rol", rol);
        model.addAttribute("todosPermisos", permisoService.findAllPermisos());
        return "admin/rol-permisos";
    }

    @PostMapping("/{id}/permisos")
    public String updateRolPermisos(@PathVariable Long id, @RequestParam(required = false) List<Long> permisosIds, RedirectAttributes redirectAttributes) {
        try {
            Rol rol = rolService.findRolById(id).orElse(null);
            if (rol != null) {
                List<Permiso> permisos = new ArrayList<>();
                if (permisosIds != null) {
                    for (Long pId : permisosIds) {
                        Permiso p = permisoService.findPermisoById(pId).orElse(null);
                        if (p != null) permisos.add(p);
                    }
                }
                rol.setPermisos(permisos);
                rolService.updateRol(rol.getId(), rol);
                redirectAttributes.addFlashAttribute("success", "Permisos del rol actualizados.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar permisos.");
        }
        return "redirect:/admin/roles";
    }

    @PostMapping("/{id}/eliminar")
    public String deleteRol(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rolService.deleteRol(id);
            redirectAttributes.addFlashAttribute("success", "Rol eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el rol. Puede estar en uso.");
        }
        return "redirect:/admin/roles";
    }
}
