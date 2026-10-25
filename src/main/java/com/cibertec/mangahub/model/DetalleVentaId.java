package com.cibertec.mangahub.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaId implements Serializable {

    @Column(name = "id_venta")
    private Integer idVenta;

    @Column(name = "id_manga")
    private Integer idManga;
}