package com.cibertec.mangahub.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VentaFilter {

    private Integer idVenta;
    private Integer idCliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
