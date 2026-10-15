package com.cibertec.mangahub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_editorial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Editorial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_editorial")
	private Integer idEditorial;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "pais")
	private String pais;
}