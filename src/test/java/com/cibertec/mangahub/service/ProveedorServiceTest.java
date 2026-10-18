package com.cibertec.mangahub.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.cibertec.mangahub.dto.ProveedorDTO;
import com.cibertec.mangahub.model.Proveedor;
import com.cibertec.mangahub.repository.ProveedorRepository;

@ExtendWith(MockitoExtension.class)
class ProveedorServiceTest {

    @Mock
    private ProveedorRepository repository;

    @InjectMocks
    private ProveedorService service;

    @Test
    void registraActivoYNormalizaLosDatos() {
        ProveedorDTO dto = datosValidos();
        dto.setRuc(" 20123456789 ");
        dto.setRazonSocial("  Editorial Manga  ");
        when(repository.saveAndFlush(any(Proveedor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Proveedor proveedor = service.registrar(dto);

        assertEquals("20123456789", proveedor.getRuc());
        assertEquals("Editorial Manga", proveedor.getRazonSocial());
        assertTrue(proveedor.getActivo());
    }

    @Test
    void rechazaRucDuplicadoSinGuardar() {
        when(repository.existsByRuc("20123456789")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.registrar(datosValidos()));

        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void rechazaRucInvalidoSinConsultarLaBase() {
        ProveedorDTO dto = datosValidos();
        dto.setRuc("1234567890A");

        assertThrows(IllegalArgumentException.class, () -> service.registrar(dto));

        verifyNoInteractions(repository);
    }

    @Test
    void rechazaRazonSocialEnBlanco() {
        ProveedorDTO dto = datosValidos();
        dto.setRazonSocial("   ");

        assertThrows(IllegalArgumentException.class, () -> service.registrar(dto));

        verifyNoInteractions(repository);
    }

    @Test
    void rechazaCorreoInvalido() {
        ProveedorDTO dto = datosValidos();
        dto.setCorreo("correo-invalido");

        assertThrows(IllegalArgumentException.class, () -> service.registrar(dto));

        verifyNoInteractions(repository);
    }

    @Test
    void permiteEditarElMismoRucYConservaElEstadoInactivo() {
        Proveedor existente = new Proveedor();
        existente.setIdProveedor(7);
        existente.setRuc("20123456789");
        existente.setActivo(false);
        when(repository.findById(7)).thenReturn(Optional.of(existente));
        when(repository.saveAndFlush(existente)).thenReturn(existente);

        Proveedor actualizado = service.actualizar(7, datosValidos());

        assertEquals(7, actualizado.getIdProveedor());
        assertFalse(actualizado.getActivo());
        verify(repository).existsByRucAndIdProveedorNot("20123456789", 7);
    }

    @Test
    void impideCambiarElRucPorElDeOtroProveedor() {
        when(repository.findById(7)).thenReturn(Optional.of(new Proveedor()));
        when(repository.existsByRucAndIdProveedorNot("20123456789", 7)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.actualizar(7, datosValidos()));

        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void desactivaSinEliminarElProveedorRelacionadoConMangas() {
        Proveedor existente = new Proveedor();
        existente.setIdProveedor(7);
        existente.setActivo(true);
        when(repository.findById(7)).thenReturn(Optional.of(existente));

        service.cambiarActivo(7, false);

        assertFalse(existente.getActivo());
        verify(repository).saveAndFlush(existente);
        verify(repository, never()).delete(any(Proveedor.class));
    }

    @Test
    void devuelve404SiElProveedorNoExiste() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.actualizar(99, datosValidos()));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void buscaPorRucORazonSocialSinEspaciosExternos() {
        when(repository.findByRucContainingIgnoreCaseOrRazonSocialContainingIgnoreCaseOrderByIdProveedorDesc(
                "Manga", "Manga")).thenReturn(List.of());

        assertTrue(service.listarProveedores("  Manga  ").isEmpty());
    }

    private ProveedorDTO datosValidos() {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setRuc("20123456789");
        dto.setRazonSocial("Editorial Manga");
        dto.setTelefono("999888777");
        dto.setCorreo("ventas@example.com");
        return dto;
    }
}
