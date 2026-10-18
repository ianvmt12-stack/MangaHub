package com.cibertec.mangahub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.mangahub.dto.MangaDTO;
import com.cibertec.mangahub.model.Manga;
import com.cibertec.mangahub.repository.EditorialRepository;
import com.cibertec.mangahub.repository.GeneroRepository;
import com.cibertec.mangahub.repository.ProveedorRepository;
import com.cibertec.mangahub.service.MangaService;
import com.cibertec.mangahub.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@Controller
@RequestMapping("/mangas")
@RequiredArgsConstructor
public class MangaController {

	private final MangaService mangaService;
	private final EditorialRepository editorialRepository;
	private final GeneroRepository generoRepository;
	private final ProveedorRepository proveedorRepository;

	private boolean esAdmin(HttpSession session) {
		return "ADMIN".equals(session.getAttribute("rol"));
	}

	@GetMapping
	public String listarMangas(@RequestParam(required = false) Integer idEditorial,
			@RequestParam(required = false) Integer idGenero, @RequestParam(required = false) Integer idProveedor,
			@RequestParam(defaultValue = "0") int pagina, Model model, HttpSession session) {

		if (session.getAttribute("usuario") == null) {
			return "redirect:/login";
		}

		if (pagina < 0) {
			return "redirect:/mangas";
		}

		Page<Manga> resultado = mangaService.listarMangas(idEditorial, idGenero, idProveedor, pagina);

		model.addAttribute("listaMangas", resultado.getContent());
		model.addAttribute("paginaActual", pagina);
		model.addAttribute("totalPaginas", resultado.getTotalPages());
		model.addAttribute("totalElementos", resultado.getTotalElements());

		model.addAttribute("listaEditoriales", editorialRepository.findAll());
		model.addAttribute("listaGeneros", generoRepository.findAll());
		model.addAttribute("listaProveedores", proveedorRepository.findAll());

		model.addAttribute("idEditorial", idEditorial);
		model.addAttribute("idGenero", idGenero);
		model.addAttribute("idProveedor", idProveedor);

		return "mangas/listado";
	}

	@GetMapping("/nuevo")
	public String nuevoManga(Model model, HttpSession session) {

		if (!esAdmin(session)) {
			return "redirect:/mangas";
		}

		model.addAttribute("manga", new MangaDTO());
		model.addAttribute("listaEditoriales", editorialRepository.findAll());
		model.addAttribute("listaGeneros", generoRepository.findAll());
		model.addAttribute("listaProveedores", proveedorRepository.findAll());

		return "mangas/nuevo";
	}

	@PostMapping("/guardar")
	public String guardarManga(@ModelAttribute("manga") MangaDTO dto, RedirectAttributes flash, HttpSession session) {

		if (!esAdmin(session)) {
			return "redirect:/mangas";
		}

		Manga manga = new Manga();

		manga.setTitulo(dto.getTitulo());
		manga.setNumTomo(dto.getNumTomo());
		manga.setAutor(dto.getAutor());
		manga.setPrecio(dto.getPrecio());
		manga.setStock(dto.getStock());
		manga.setActivo(true);

		manga.setEditorial(editorialRepository.findById(dto.getIdEditorial()).orElseThrow());

		manga.setGenero(generoRepository.findById(dto.getIdGenero()).orElseThrow());

		manga.setProveedor(proveedorRepository.findById(dto.getIdProveedor()).orElseThrow());

		var response = mangaService.guardar(manga);

		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), "success", 5000));

		return "redirect:/mangas";
	}

	@GetMapping("/editar/{id}")
	public String editarManga(@PathVariable Integer id, Model model, HttpSession session) {

		if (!esAdmin(session)) {
			return "redirect:/mangas";
		}

		Manga manga = mangaService.buscarPorId(id);
		MangaDTO dto = new MangaDTO();

		dto.setTitulo(manga.getTitulo());
		dto.setNumTomo(manga.getNumTomo());
		dto.setAutor(manga.getAutor());
		dto.setPrecio(manga.getPrecio());
		dto.setStock(manga.getStock());

		dto.setIdEditorial(manga.getEditorial().getIdEditorial());
		dto.setIdGenero(manga.getGenero().getIdGenero());
		dto.setIdProveedor(manga.getProveedor().getIdProveedor());

		model.addAttribute("manga", dto);
		model.addAttribute("idManga", manga.getIdManga());

		model.addAttribute("listaEditoriales", editorialRepository.findAll());
		model.addAttribute("listaGeneros", generoRepository.findAll());
		model.addAttribute("listaProveedores", proveedorRepository.findAll());

		return "mangas/editar";
	}

	@PostMapping("/actualizar/{id}")
	public String actualizarManga(@PathVariable Integer id, @ModelAttribute("manga") MangaDTO dto,
			RedirectAttributes flash, HttpSession session) {

		if (!esAdmin(session)) {
			return "redirect:/mangas";
		}

		Manga manga = mangaService.buscarPorId(id);

		manga.setTitulo(dto.getTitulo());
		manga.setNumTomo(dto.getNumTomo());
		manga.setAutor(dto.getAutor());
		manga.setPrecio(dto.getPrecio());
		manga.setStock(dto.getStock());

		manga.setEditorial(editorialRepository.findById(dto.getIdEditorial()).orElseThrow());

		manga.setGenero(generoRepository.findById(dto.getIdGenero()).orElseThrow());

		manga.setProveedor(proveedorRepository.findById(dto.getIdProveedor()).orElseThrow());

		var response = mangaService.guardar(manga);

		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), "success", 5000));

		return "redirect:/mangas";
	}

	@PostMapping("/desactivar/{id}")
	public String desactivarManga(@PathVariable Integer id, RedirectAttributes flash, HttpSession session) {

		if (!esAdmin(session)) {
			return "redirect:/mangas";
		}

		var response = mangaService.desactivar(id);

		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), "success", 5000));

		return "redirect:/mangas";
	}

	@PostMapping("/activar/{id}")
	public String activarManga(@PathVariable Integer id, RedirectAttributes flash, HttpSession session) {

		if (!esAdmin(session)) {
			return "redirect:/mangas";
		}

		var response = mangaService.activar(id);

		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), "success", 5000));

		return "redirect:/mangas";
	}
}
