package com.cibertec.mangahub.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.mangahub.dto.MangaSeleccionado;
import com.cibertec.mangahub.model.Manga;
import com.cibertec.mangahub.repository.ClienteRepository;
import com.cibertec.mangahub.repository.MangaRepository;
import com.cibertec.mangahub.service.VentaService;
import com.cibertec.mangahub.dto.VentaFilter;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/ventas")
@SessionAttributes("mangasSeleccionados")
@RequiredArgsConstructor
public class VentaController {

	private final VentaService ventaService;
	private final MangaRepository mangaRepository;
	private final ClienteRepository clienteRepository;

	private boolean puedeGestionarVentas(HttpSession session) {
		String rol = (String) session.getAttribute("rol");
		return "ADMIN".equals(rol) || "VENDEDOR".equals(rol);
	}

	@ModelAttribute("mangasSeleccionados")
	public List<MangaSeleccionado> mangasSeleccionados() {
		return new ArrayList<>();
	}

	@GetMapping("/nuevo")
	public String nuevo(Model model, HttpSession session) {

		if (session.getAttribute("idUsuario") == null) {
			return "redirect:/login";
		}

		if (!puedeGestionarVentas(session)) {
			return "redirect:/";
		}

		model.addAttribute("clientes", clienteRepository.findAll());
		model.addAttribute("mangas", mangaRepository.findAll());

		return "venta/nuevo";
	}

	@PostMapping("/agregar")
	public String agregar(@RequestParam Integer idManga, @RequestParam Integer cantidad,
			@ModelAttribute("mangasSeleccionados") List<MangaSeleccionado> seleccionados, HttpSession session,
			RedirectAttributes redirect) {

		if (session.getAttribute("idUsuario") == null) {
			return "redirect:/login";
		}

		if (cantidad == null || cantidad <= 0) {
			redirect.addFlashAttribute("error", "Cantidad inválida");
			return "redirect:/ventas/nuevo";
		}

		Manga manga = mangaRepository.findById(idManga)
				.orElseThrow(() -> new IllegalArgumentException("Manga no encontrado"));

		MangaSeleccionado seleccionado = new MangaSeleccionado();

		seleccionado.setIdManga(manga.getIdManga());
		seleccionado.setTitulo(manga.getTitulo());
		seleccionado.setCantidad(cantidad);
		seleccionado.setPrecio(manga.getPrecio());
		seleccionado.setSubtotal(manga.getPrecio().multiply(BigDecimal.valueOf(cantidad)));

		seleccionados.removeIf(m -> m.getIdManga().equals(idManga));
		seleccionados.add(seleccionado);

		return "redirect:/ventas/nuevo";
	}

	@PostMapping("/registrar")
	public String registrar(@RequestParam Integer idCliente,
			@ModelAttribute("mangasSeleccionados") List<MangaSeleccionado> seleccionados, HttpSession session,
			SessionStatus sessionStatus, RedirectAttributes redirect) {

		Integer idUsuario = (Integer) session.getAttribute("idUsuario");

		if (idUsuario == null) {
			return "redirect:/login";
		}

		try {
			ventaService.registrarVenta(idCliente, idUsuario, seleccionados);

			sessionStatus.setComplete();

			redirect.addFlashAttribute("mensaje", "Venta registrada correctamente");

			return "redirect:/ventas/nuevo";

		} catch (Exception e) {
			redirect.addFlashAttribute("error", e.getMessage());
			return "redirect:/ventas/nuevo";
		}
	}

	@GetMapping("/listado")
	public String listado(@ModelAttribute("filtro") VentaFilter filtro, Model model, HttpSession session) {

		if (session.getAttribute("idUsuario") == null) {
			return "redirect:/login";
		}

		model.addAttribute("clientes", clienteRepository.findAll());
		model.addAttribute("ventas", ventaService.buscarVentas(filtro));

		return "venta/listado";
	}

}
