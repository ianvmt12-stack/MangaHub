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
@Table(name = "tbl_proveedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Proveedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_proveedor")
	private Integer idProveedor;

	@Column(name = "ruc")
	private String ruc;

	@Column(name = "razon_social")
	private String razonSocial;

	@Column(name = "telefono")
	private String telefono;

	@Column(name = "correo")
	private String correo;

	@Column(name = "activo")
	private Boolean activo;

	public String getActivoDescripcion() {
		return activo ? "Activo" : "Inactivo";
	}
}