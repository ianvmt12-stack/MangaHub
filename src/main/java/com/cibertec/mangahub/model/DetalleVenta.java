package com.cibertec.mangahub.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_detalle_venta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_detalle")
	private Integer idDetalle;

	@ManyToOne
	@JoinColumn(name = "id_venta")
	private Venta venta;

	@ManyToOne
	@JoinColumn(name = "id_manga")
	private Manga manga;

	@Column(name = "cantidad")
	private Integer cantidad;

	@Column(name = "precio_unitario")
	private BigDecimal precioUnitario;

	@Column(name = "subtotal")
	private BigDecimal subtotal;
}