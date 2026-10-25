package com.cibertec.mangahub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cibertec.mangahub.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

	@Query("""
			SELECT v FROM Venta v
			WHERE (:idVenta IS NULL OR v.idVenta = :idVenta)
			  AND (:idCliente IS NULL OR v.cliente.idCliente = :idCliente)
			  AND (v.fechaEmision >= :fechaInicio)
			  AND (v.fechaEmision < :fechaFin)
			ORDER BY v.idVenta DESC
			""")
	List<Venta> buscarVentas(@Param("idVenta") Integer idVenta, @Param("idCliente") Integer idCliente,
			@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

}
