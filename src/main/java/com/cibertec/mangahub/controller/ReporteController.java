
package com.cibertec.mangahub.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cibertec.mangahub.model.DetalleVenta;
import com.cibertec.mangahub.service.VentaService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

	private final VentaService ventaService;

	@GetMapping("/ventas")
	public String reporteVentas(Model model, HttpSession session) {

		if (session.getAttribute("idUsuario") == null) {
			return "redirect:/login";
		}

		if (!"ADMIN".equals(session.getAttribute("rol"))) {
			return "redirect:/";
		}

		List<DetalleVenta> detalles = ventaService.obtenerReporteVentas();

		BigDecimal totalGeneral = BigDecimal.ZERO;
		int cantidadVendida = 0;

		for (DetalleVenta detalle : detalles) {

			BigDecimal subtotal = detalle.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad()));

			totalGeneral = totalGeneral.add(subtotal);
			cantidadVendida += detalle.getCantidad();
		}

		model.addAttribute("detalles", detalles);
		model.addAttribute("totalGeneral", totalGeneral);
		model.addAttribute("cantidadVendida", cantidadVendida);

		return "reporte/ventas";
	}
}
