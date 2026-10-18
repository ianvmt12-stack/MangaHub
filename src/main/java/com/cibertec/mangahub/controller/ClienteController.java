package com.cibertec.mangahub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.mangahub.dto.ClienteDTO;
import com.cibertec.mangahub.model.Cliente;
import com.cibertec.mangahub.service.ClienteService;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	// LISTAR CLIENTES
	@GetMapping("/clientes")
	public String listarClientes(Model model) {

		model.addAttribute("listaClientes", clienteService.listarClientes());

		return "clientes/listado";
	}

	// MOSTRAR FORMULARIO NUEVO CLIENTE
	@GetMapping("/clientes/nuevo")
	public String nuevoCliente(Model model) {

		model.addAttribute("cliente", new ClienteDTO());

		return "clientes/nuevo";
	}

	// GUARDAR NUEVO CLIENTE
	@PostMapping("/clientes/guardar")
	public String guardarCliente(@ModelAttribute("cliente") ClienteDTO dto, RedirectAttributes redirectAttributes) {

		Cliente cliente = new Cliente();

		cliente.setDni(dto.getDni());
		cliente.setNombres(dto.getNombres());
		cliente.setApellidos(dto.getApellidos());
		cliente.setTelefono(dto.getTelefono());
		cliente.setCorreo(dto.getCorreo());
		cliente.setActivo(true);

		clienteService.guardar(cliente);

		redirectAttributes.addFlashAttribute("mensaje", "Cliente registrado correctamente");

		return "redirect:/clientes";
	}

	// SE MUESTRA EL FORMULARIO EDITAR CLIENTE
	@GetMapping("/clientes/editar/{id}")
	public String editarCliente(@PathVariable Integer id, Model model) {

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

	@PostMapping("/clientes/actualizar/{id}")
	public String actualizarCliente(@PathVariable Integer id, @ModelAttribute("cliente") ClienteDTO dto,
			RedirectAttributes redirectAttributes) {

		Cliente cliente = clienteService.buscarPorId(id);

		cliente.setDni(dto.getDni());
		cliente.setNombres(dto.getNombres());
		cliente.setApellidos(dto.getApellidos());
		cliente.setTelefono(dto.getTelefono());
		cliente.setCorreo(dto.getCorreo());

		clienteService.guardar(cliente);

		redirectAttributes.addFlashAttribute("mensaje", "Cliente actualizado correctamente");

		return "redirect:/clientes";
	}

	@PostMapping("/clientes/desactivar/{id}")
	public String desactivarCliente(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

		clienteService.desactivar(id);

		redirectAttributes.addFlashAttribute("mensaje", "Cliente desactivado correctamente");

		return "redirect:/clientes";
	}

	@PostMapping("/clientes/activar/{id}")
	public String activarCliente(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

		clienteService.activar(id);

		redirectAttributes.addFlashAttribute("mensaje", "Cliente activado correctamente");

		return "redirect:/clientes";
	}

}
