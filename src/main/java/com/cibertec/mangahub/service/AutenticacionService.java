
package com.cibertec.mangahub.service;

import org.springframework.stereotype.Service;

import com.cibertec.mangahub.dto.AutenticacionFilter;
import com.cibertec.mangahub.model.Usuario;
import com.cibertec.mangahub.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacionService {

	private final UsuarioRepository usuarioRepository;

	public Usuario authenticate(AutenticacionFilter filter) {

		Usuario usuario = usuarioRepository.findByUsernameAndPassword(filter.cuenta(), filter.clave());

		if (usuario != null && Boolean.TRUE.equals(usuario.getActivo())) {
			return usuario;
		}

		return null;
	}
}
