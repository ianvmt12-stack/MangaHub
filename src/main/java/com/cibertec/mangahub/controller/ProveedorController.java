package com.cibertec.mangahub.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.mangahub.dto.ProveedorDTO;
import com.cibertec.mangahub.model.Proveedor;
import com.cibertec.mangahub.service.ProveedorService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService proveedorService;

    @InitBinder("proveedor")
    public void configurarFormulario(WebDataBinder binder) {
        binder.setAllowedFields("ruc", "razonSocial", "telefono", "correo");
    }

    @GetMapping
    public String listar(@RequestParam(name = "q", required = false) String busqueda, Model model) {
        model.addAttribute("listaProveedores", proveedorService.listarProveedores(busqueda));
        model.addAttribute("busqueda", busqueda == null ? "" : busqueda.trim());
        return "proveedores/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("proveedor", new ProveedorDTO());
        return "proveedores/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("proveedor") ProveedorDTO dto, Model model, RedirectAttributes flash) {
        try {
            proveedorService.registrar(dto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensajeError", e.getMessage());
            return "proveedores/formulario";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("mensajeError", "No se pudo guardar el proveedor. Revise los datos y el RUC.");
            return "proveedores/formulario";
        }
        flash.addFlashAttribute("mensaje", "Proveedor registrado correctamente.");
        return "redirect:/proveedores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Proveedor proveedor = proveedorService.buscarPorId(id);
        ProveedorDTO dto = new ProveedorDTO();
        dto.setRuc(proveedor.getRuc());
        dto.setRazonSocial(proveedor.getRazonSocial());
        dto.setTelefono(proveedor.getTelefono());
        dto.setCorreo(proveedor.getCorreo());
        model.addAttribute("proveedor", dto);
        model.addAttribute("idProveedor", id);
        return "proveedores/formulario";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id, @ModelAttribute("proveedor") ProveedorDTO dto,
            Model model, RedirectAttributes flash) {
        model.addAttribute("idProveedor", id);
        try {
            proveedorService.actualizar(id, dto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensajeError", e.getMessage());
            return "proveedores/formulario";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("mensajeError", "No se pudo actualizar el proveedor. Revise los datos y el RUC.");
            return "proveedores/formulario";
        }
        flash.addFlashAttribute("mensaje", "Proveedor actualizado correctamente.");
        return "redirect:/proveedores";
    }

    @PostMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Integer id, RedirectAttributes flash) {
        proveedorService.cambiarActivo(id, false);
        flash.addFlashAttribute("mensaje", "Proveedor desactivado correctamente.");
        return "redirect:/proveedores";
    }

    @PostMapping("/activar/{id}")
    public String activar(@PathVariable Integer id, RedirectAttributes flash) {
        proveedorService.cambiarActivo(id, true);
        flash.addFlashAttribute("mensaje", "Proveedor activado correctamente.");
        return "redirect:/proveedores";
    }
}
