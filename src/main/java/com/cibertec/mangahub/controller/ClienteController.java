package com.cibertec.mangahub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.mangahub.dto.ClienteDTO;
import com.cibertec.mangahub.model.Cliente;
import com.cibertec.mangahub.service.ClienteService;
import com.cibertec.mangahub.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

	private final ClienteService clienteService;

	private boolean tieneAcceso(HttpSession session) {
		Object rol = session.getAttribute("rol");
		return session.getAttribute("usuario") != null && ("ADMIN".equals(rol) || "VENDEDOR".equals(rol));
	}

	@GetMapping
	public String listarClientes(Model model, HttpSession session) {

		if (!tieneAcceso(session)) {
			return "redirect:/login";
		}

		model.addAttribute("listaClientes", clienteService.listarClientes());

		return "clientes/listado";
	}

	@GetMapping("/nuevo")
	public String nuevoCliente(Model model, HttpSession session) {

		if (!tieneAcceso(session)) {
			return "redirect:/login";
		}

		model.addAttribute("cliente", new ClienteDTO());

		return "clientes/nuevo";
	}

	@PostMapping("/guardar")
	public String guardarCliente(@ModelAttribute("cliente") ClienteDTO dto, RedirectAttributes flash,
			HttpSession session) {

		if (!tieneAcceso(session)) {
			return "redirect:/login";
		}

		Cliente cliente = new Cliente();

		cliente.setDni(dto.getDni());
		cliente.setNombres(dto.getNombres());
		cliente.setApellidos(dto.getApellidos());
		cliente.setTelefono(dto.getTelefono());
		cliente.setCorreo(dto.getCorreo());
		cliente.setActivo(true);

		var response = clienteService.guardar(cliente);

		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), "success", 5000));

		return "redirect:/clientes";
	}

	@GetMapping("/editar/{id}")
	public String editarCliente(@PathVariable Integer id, Model model, HttpSession session) {

		if (!tieneAcceso(session)) {
			return "redirect:/login";
		}

		Cliente cliente = clienteService.buscarPorId(id);

		ClienteDTO dto = new ClienteDTO();

		dto.setDni(cliente.getDni());
		dto.setNombres(cliente.getNombres());
		dto.setApellidos(cliente.getApellidos());
		dto.setTelefono(cliente.getTelefono());
		dto.setCorreo(cliente.getCorreo());

		model.addAttribute("cliente", dto);
		model.addAttribute("idCliente", cliente.getIdCliente());

		return "clientes/editar";
	}

	@PostMapping("/actualizar/{id}")
	public String actualizarCliente(@PathVariable Integer id, @ModelAttribute("cliente") ClienteDTO dto,
			RedirectAttributes flash, HttpSession session) {

		if (!tieneAcceso(session)) {
			return "redirect:/login";
		}

		Cliente cliente = clienteService.buscarPorId(id);

		cliente.setDni(dto.getDni());
		cliente.setNombres(dto.getNombres());
		cliente.setApellidos(dto.getApellidos());
		cliente.setTelefono(dto.getTelefono());
		cliente.setCorreo(dto.getCorreo());

		var response = clienteService.guardar(cliente);

		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), "success", 5000));

		return "redirect:/clientes";
	}

	@PostMapping("/desactivar/{id}")
	public String desactivarCliente(@PathVariable Integer id, RedirectAttributes flash, HttpSession session) {

		if (!tieneAcceso(session)) {
			return "redirect:/login";
		}

		var response = clienteService.desactivar(id);

		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), "success", 5000));

		return "redirect:/clientes";
	}

	@PostMapping("/activar/{id}")
	public String activarCliente(@PathVariable Integer id, RedirectAttributes flash, HttpSession session) {

		if (!tieneAcceso(session)) {
			return "redirect:/login";
		}

		var response = clienteService.activar(id);

		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), "success", 5000));

		return "redirect:/clientes";
	}
}
