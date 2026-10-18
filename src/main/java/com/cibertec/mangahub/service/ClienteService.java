package com.cibertec.mangahub.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cibertec.mangahub.dto.ResultadoResponse;
import com.cibertec.mangahub.model.Cliente;
import com.cibertec.mangahub.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

	private final ClienteRepository clienteRepository;

	@Transactional(readOnly = true)
	public List<Cliente> listarClientes() {
		return clienteRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Cliente buscarPorId(Integer id) {
		return clienteRepository.findById(id).orElseThrow();
	}

	@Transactional
	public ResultadoResponse guardar(Cliente cliente) {

		boolean esNuevo = cliente.getIdCliente() == null;

		clienteRepository.save(cliente);

		String mensaje = esNuevo ? "Cliente registrado correctamente" : "Cliente actualizado correctamente";

		return new ResultadoResponse(true, mensaje);
	}

	@Transactional
	public ResultadoResponse desactivar(Integer id) {

		Cliente cliente = clienteRepository.findById(id).orElseThrow();

		cliente.setActivo(false);
		clienteRepository.save(cliente);

		return new ResultadoResponse(true, "Cliente desactivado correctamente");
	}

	@Transactional
	public ResultadoResponse activar(Integer id) {

		Cliente cliente = clienteRepository.findById(id).orElseThrow();

		cliente.setActivo(true);
		clienteRepository.save(cliente);

		return new ResultadoResponse(true, "Cliente activado correctamente");
	}
}
