package com.cibertec.mangahub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.mangahub.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	Usuario findByUsernameAndPassword(String username, String password);

}