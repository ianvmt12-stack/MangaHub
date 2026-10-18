package com.cibertec.mangahub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.mangahub.model.Manga;
import com.cibertec.mangahub.repository.MangaRepository;

@Service
public class MangaService {

	@Autowired
	private MangaRepository mangaRepository;

	public List<Manga> listarMangas(Integer idEditorial, Integer idGenero, Integer idProveedor) {

		return mangaRepository.findByFilters(idEditorial, idGenero, idProveedor);
	}

	public Manga buscarPorId(Integer id) {
		return mangaRepository.findById(id).orElseThrow();
	}

	public void guardar(Manga manga) {
		mangaRepository.save(manga);
	}

	public void desactivar(Integer id) {

		Manga manga = mangaRepository.findById(id).orElseThrow();

		manga.setActivo(false);

		mangaRepository.save(manga);
	}

	public void activar(Integer id) {

		Manga manga = mangaRepository.findById(id).orElseThrow();

		manga.setActivo(true);

		mangaRepository.save(manga);
	}

}
