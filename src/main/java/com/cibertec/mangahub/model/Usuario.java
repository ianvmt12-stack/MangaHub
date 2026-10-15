package com.cibertec.mangahub.model;

import org.hibernate.annotations.DynamicInsert;

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
@Table(name = "tbl_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer idUsuario;

	@Column(name = "nombres")
	private String nombres;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "rol")
	private String rol;

	@Column(name = "activo")
	private Boolean activo;

	public String getActivoDescripcion() {
		return activo ? "Activo" : "Inactivo";
	}
}