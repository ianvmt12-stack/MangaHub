package com.cibertec.mangahub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTO {

    private String dni;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
}
