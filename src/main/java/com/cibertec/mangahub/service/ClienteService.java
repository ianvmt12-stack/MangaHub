package com.cibertec.mangahub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.mangahub.model.Cliente;
import com.cibertec.mangahub.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public List<Cliente> listarClientes() {
		return clienteRepository.findAll();
	}

	public Cliente buscarPorId(Integer id) {
		return clienteRepository.findById(id).orElseThrow();
	}

	public void guardar(Cliente cliente) {
		clienteRepository.save(cliente);
	}

	public void desactivar(Integer id) {

		Cliente cliente = clienteRepository.findById(id).orElseThrow();

		cliente.setActivo(false);

		clienteRepository.save(cliente);
	}

	public void activar(Integer id) {

		Cliente cliente = clienteRepository.findById(id).orElseThrow();

		cliente.setActivo(true);

		clienteRepository.save(cliente);
	}

}
