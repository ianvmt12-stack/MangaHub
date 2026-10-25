package com.cibertec.mangahub.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MangaSeleccionado {

    private Integer idManga;
    private String titulo;
    private Integer cantidad;
    private BigDecimal precio;
    private BigDecimal subtotal;
}
