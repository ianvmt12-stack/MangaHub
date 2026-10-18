package com.cibertec.mangahub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.mangahub.dto.MangaDTO;
import com.cibertec.mangahub.model.Manga;
import com.cibertec.mangahub.repository.EditorialRepository;
import com.cibertec.mangahub.repository.GeneroRepository;
import com.cibertec.mangahub.repository.ProveedorRepository;
import com.cibertec.mangahub.service.MangaService;

@Controller
public class MangaController {

	@Autowired
	private MangaService mangaService;

	@Autowired
	private EditorialRepository editorialRepository;

	@Autowired
	private GeneroRepository generoRepository;

	@Autowired
	private ProveedorRepository proveedorRepository;

	// LISTAR MANGAS
	@GetMapping("/mangas")
	public String listarMangas(@RequestParam(required = false) Integer idEditorial,
			@RequestParam(required = false) Integer idGenero, @RequestParam(required = false) Integer idProveedor,
			Model model) {

		model.addAttribute("listaMangas", mangaService.listarMangas(idEditorial, idGenero, idProveedor));

		model.addAttribute("listaEditoriales", editorialRepository.findAll());

		model.addAttribute("listaGeneros", generoRepository.findAll());

		model.addAttribute("listaProveedores", proveedorRepository.findAll());

		model.addAttribute("idEditorial", idEditorial);
		model.addAttribute("idGenero", idGenero);
		model.addAttribute("idProveedor", idProveedor);

		return "mangas/listado";
	}

	// MOSTRAR FORMULARIO NUEVO MANGA
	@GetMapping("/mangas/nuevo")
	public String nuevoManga(Model model) {

		model.addAttribute("manga", new MangaDTO());

		model.addAttribute("listaEditoriales", editorialRepository.findAll());

		model.addAttribute("listaGeneros", generoRepository.findAll());

		model.addAttribute("listaProveedores", proveedorRepository.findAll());

		return "mangas/nuevo";
	}

	// GUARDAR NUEVO MANGA
	@PostMapping("/mangas/guardar")
	public String guardarManga(@ModelAttribute("manga") MangaDTO dto, RedirectAttributes redirectAttributes) {

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

		mangaService.guardar(manga);

		redirectAttributes.addFlashAttribute("mensaje", "Manga registrado correctamente");

		return "redirect:/mangas";
	}

	// MOSTRAR FORMULARIO EDITAR MANGA
	@GetMapping("/mangas/editar/{id}")
	public String editarManga(@PathVariable Integer id, Model model) {

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

	// ACTUALIZAR MANGA
	@PostMapping("/mangas/actualizar/{id}")
	public String actualizarManga(@PathVariable Integer id, @ModelAttribute("manga") MangaDTO dto,
			RedirectAttributes redirectAttributes) {

		Manga manga = mangaService.buscarPorId(id);

		manga.setTitulo(dto.getTitulo());
		manga.setNumTomo(dto.getNumTomo());
		manga.setAutor(dto.getAutor());
		manga.setPrecio(dto.getPrecio());
		manga.setStock(dto.getStock());

		manga.setEditorial(editorialRepository.findById(dto.getIdEditorial()).orElseThrow());

		manga.setGenero(generoRepository.findById(dto.getIdGenero()).orElseThrow());

		manga.setProveedor(proveedorRepository.findById(dto.getIdProveedor()).orElseThrow());

		mangaService.guardar(manga);

		redirectAttributes.addFlashAttribute("mensaje", "Manga actualizado correctamente");

		return "redirect:/mangas";
	}

	// DESACTIVA EL MANGAAAAAAAA
	@PostMapping("/mangas/desactivar/{id}")
	public String desactivarManga(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

		mangaService.desactivar(id);

		redirectAttributes.addFlashAttribute("mensaje", "Manga desactivado correctamente");

		return "redirect:/mangas";
	}

	// ACTIVA EL MANGAAAAAAAAA
	@PostMapping("/mangas/activar/{id}")
	public String activarManga(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

		mangaService.activar(id);

		redirectAttributes.addFlashAttribute("mensaje", "Manga activado correctamente");

		return "redirect:/mangas";
	}

}
