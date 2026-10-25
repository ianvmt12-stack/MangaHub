package com.cibertec.mangahub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cibertec.mangahub.model.Manga;

import jakarta.persistence.LockModeType;

public interface MangaRepository extends JpaRepository<Manga, Integer> {

	List<Manga> findAllByOrderByIdMangaDesc();

	@Query("""
			SELECT m FROM Manga m
			WHERE (:idEditorial IS NULL OR m.editorial.idEditorial = :idEditorial)
			  AND (:idGenero IS NULL OR m.genero.idGenero = :idGenero)
			  AND (:idProveedor IS NULL OR m.proveedor.idProveedor = :idProveedor)
			""")
	Page<Manga> findByFilters(@Param("idEditorial") Integer idEditorial, @Param("idGenero") Integer idGenero,
			@Param("idProveedor") Integer idProveedor, Pageable pageable);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT m FROM Manga m WHERE m.idManga = :idManga")
	Optional<Manga> buscarParaVenta(@Param("idManga") Integer idManga);
}
