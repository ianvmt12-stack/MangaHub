package com.cibertec.mangahub.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.mangahub.model.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
	List<Proveedor> findAllByOrderByIdProveedorDesc();
}