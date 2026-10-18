package com.cibertec.mangahub.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cibertec.mangahub.dto.ProveedorDTO;
import com.cibertec.mangahub.model.Proveedor;
import com.cibertec.mangahub.repository.ProveedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public List<Proveedor> listarProveedores(String busqueda) {
        String termino = limpiar(busqueda);
        if (termino.isEmpty()) {
            return proveedorRepository.findAllByOrderByIdProveedorDesc();
        }
        return proveedorRepository
                .findByRucContainingIgnoreCaseOrRazonSocialContainingIgnoreCaseOrderByIdProveedorDesc(termino, termino);
    }

    public Proveedor buscarPorId(Integer id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El proveedor no existe."));
    }

    @Transactional
    public Proveedor registrar(ProveedorDTO dto) {
        validar(dto, null);
        Proveedor proveedor = new Proveedor();
        copiarDatos(dto, proveedor);
        proveedor.setActivo(true);
        return proveedorRepository.saveAndFlush(proveedor);
    }

    @Transactional
    public Proveedor actualizar(Integer id, ProveedorDTO dto) {
        Proveedor proveedor = buscarPorId(id);
        validar(dto, id);
        copiarDatos(dto, proveedor);
        return proveedorRepository.saveAndFlush(proveedor);
    }

    @Transactional
    public void cambiarActivo(Integer id, boolean activo) {
        Proveedor proveedor = buscarPorId(id);
        proveedor.setActivo(activo);
        proveedorRepository.saveAndFlush(proveedor);
    }

    private void validar(ProveedorDTO dto, Integer id) {
        String ruc = limpiar(dto.getRuc());
        if (!ruc.matches("[0-9]{11}")) {
            throw new IllegalArgumentException("El RUC debe contener exactamente 11 dígitos.");
        }
        String razonSocial = limpiar(dto.getRazonSocial());
        if (razonSocial.isEmpty() || razonSocial.length() > 255) {
            throw new IllegalArgumentException("Ingrese una razón social de hasta 255 caracteres.");
        }
        if (limpiar(dto.getTelefono()).length() > 255) {
            throw new IllegalArgumentException("El teléfono debe tener hasta 255 caracteres.");
        }
        String correo = limpiar(dto.getCorreo());
        if (correo.length() > 255 || (!correo.isEmpty() && !correo.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))) {
            throw new IllegalArgumentException("Ingrese un correo válido o deje el campo vacío.");
        }
        boolean repetido = id == null ? proveedorRepository.existsByRuc(ruc)
                : proveedorRepository.existsByRucAndIdProveedorNot(ruc, id);
        if (repetido) {
            throw new IllegalArgumentException("Ya existe un proveedor con ese RUC.");
        }
    }

    private void copiarDatos(ProveedorDTO dto, Proveedor proveedor) {
        proveedor.setRuc(limpiar(dto.getRuc()));
        proveedor.setRazonSocial(limpiar(dto.getRazonSocial()));
        proveedor.setTelefono(limpiar(dto.getTelefono()));
        proveedor.setCorreo(limpiar(dto.getCorreo()));
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
