package com.cibertec.mangahub.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MangaDTO {

	private String titulo;
	private Integer numTomo;
	private String autor;
	private BigDecimal precio;
	private Integer stock;
	private Integer idEditorial;
	private Integer idGenero;
	private Integer idProveedor;
}
