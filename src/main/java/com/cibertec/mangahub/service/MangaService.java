package com.cibertec.mangahub.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cibertec.mangahub.dto.ResultadoResponse;
import com.cibertec.mangahub.model.Manga;
import com.cibertec.mangahub.repository.MangaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MangaService {

	private final MangaRepository mangaRepository;

	@Transactional(readOnly = true)
	public Page<Manga> listarMangas(Integer idEditorial, Integer idGenero, Integer idProveedor, int pagina) {

		Pageable pageable = PageRequest.of(pagina, 5, Sort.by("idManga").descending());

		return mangaRepository.findByFilters(idEditorial, idGenero, idProveedor, pageable);
	}

	@Transactional(readOnly = true)
	public Manga buscarPorId(Integer id) {
		return mangaRepository.findById(id).orElseThrow();
	}

	@Transactional
	public ResultadoResponse guardar(Manga manga) {

		boolean esNuevo = manga.getIdManga() == null;

		mangaRepository.save(manga);

		String mensaje = esNuevo ? "Manga registrado correctamente" : "Manga actualizado correctamente";

		return new ResultadoResponse(true, mensaje);
	}

	@Transactional
	public ResultadoResponse desactivar(Integer id) {

		Manga manga = mangaRepository.findById(id).orElseThrow();

		manga.setActivo(false);
		mangaRepository.save(manga);

		return new ResultadoResponse(true, "Manga desactivado correctamente");
	}

	@Transactional
	public ResultadoResponse activar(Integer id) {

		Manga manga = mangaRepository.findById(id).orElseThrow();

		manga.setActivo(true);
		mangaRepository.save(manga);

		return new ResultadoResponse(true, "Manga activado correctamente");
	}
}
