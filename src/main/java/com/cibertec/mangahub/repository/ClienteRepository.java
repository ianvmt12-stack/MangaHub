package com.cibertec.mangahub.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.mangahub.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	List<Cliente> findAllByOrderByIdClienteDesc();
}