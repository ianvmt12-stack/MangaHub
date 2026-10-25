
package com.cibertec.mangahub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cibertec.mangahub.model.DetalleVenta;
import com.cibertec.mangahub.model.DetalleVentaId;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, DetalleVentaId> {

    @Query("""
            SELECT d FROM DetalleVenta d
            JOIN FETCH d.venta v
            JOIN FETCH d.manga m
            ORDER BY v.idVenta DESC
            """)
    List<DetalleVenta> obtenerReporteVentas();
}
