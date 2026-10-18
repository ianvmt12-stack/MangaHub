package com.cibertec.mangahub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.mangahub.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
}