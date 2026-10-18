package com.cibertec.mangahub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.mangahub.model.Usuario;
import com.cibertec.mangahub.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario validarLogin(String username, String password) {

		Usuario usuario = usuarioRepository.findByUsernameAndPassword(username, password);

		if (usuario != null && Boolean.TRUE.equals(usuario.getActivo())) {
			return usuario;
		}

		return null;
	}
}
