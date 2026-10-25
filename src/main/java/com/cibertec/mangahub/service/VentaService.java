package com.cibertec.mangahub.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cibertec.mangahub.dto.MangaSeleccionado;
import com.cibertec.mangahub.dto.ResultadoResponse;
import com.cibertec.mangahub.dto.VentaFilter;
import com.cibertec.mangahub.model.Cliente;
import com.cibertec.mangahub.model.DetalleVenta;
import com.cibertec.mangahub.model.DetalleVentaId;
import com.cibertec.mangahub.model.Manga;
import com.cibertec.mangahub.model.Usuario;
import com.cibertec.mangahub.model.Venta;
import com.cibertec.mangahub.repository.ClienteRepository;
import com.cibertec.mangahub.repository.MangaRepository;
import com.cibertec.mangahub.repository.UsuarioRepository;
import com.cibertec.mangahub.repository.VentaRepository;
import com.cibertec.mangahub.repository.DetalleVentaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaService {

	private final VentaRepository ventaRepository;
	private final MangaRepository mangaRepository;
	private final ClienteRepository clienteRepository;
	private final UsuarioRepository usuarioRepository;
	private final DetalleVentaRepository detalleVentaRepository;

	@Transactional
	public ResultadoResponse registrarVenta(Integer idCliente, Integer idUsuario,
			List<MangaSeleccionado> mangasSeleccionados) {

		if (mangasSeleccionados == null || mangasSeleccionados.isEmpty()) {
			throw new IllegalArgumentException("Debe seleccionar al menos un manga");
		}

		Cliente cliente = clienteRepository.findById(idCliente)
				.orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

		List<MangaSeleccionado> mangasOrdenados = new ArrayList<>(mangasSeleccionados);

		if (mangasOrdenados.stream().anyMatch(manga -> manga == null || manga.getIdManga() == null)) {
			throw new IllegalArgumentException("Debe seleccionar mangas válidos");
		}

		mangasOrdenados.sort(Comparator.comparing(MangaSeleccionado::getIdManga));

		Venta venta = new Venta();
		venta.setCliente(cliente);
		venta.setUsuario(usuario);
		venta.setFechaEmision(LocalDateTime.now());
		venta.setActivo(true);

		List<DetalleVenta> detalles = new ArrayList<>();
		BigDecimal total = BigDecimal.ZERO;
		Set<Integer> mangasAgregados = new HashSet<>();

		for (MangaSeleccionado seleccionado : mangasOrdenados) {

			if (!mangasAgregados.add(seleccionado.getIdManga())) {
				throw new IllegalArgumentException("No se puede registrar el mismo manga dos veces");
			}

			if (seleccionado.getCantidad() == null || seleccionado.getCantidad() <= 0) {
				throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
			}

			Manga manga = mangaRepository.buscarParaVenta(seleccionado.getIdManga())
					.orElseThrow(() -> new IllegalArgumentException("Manga no encontrado"));

			if (!Boolean.TRUE.equals(manga.getActivo())) {
				throw new IllegalArgumentException("El manga no está activo");
			}

			if (manga.getStock() == null || manga.getStock() < seleccionado.getCantidad()) {
				throw new IllegalArgumentException("Stock insuficiente para el manga: " + manga.getTitulo());
			}

			if (manga.getPrecio() == null || manga.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
				throw new IllegalArgumentException("El manga no tiene un precio válido");
			}

			BigDecimal subtotal = manga.getPrecio().multiply(BigDecimal.valueOf(seleccionado.getCantidad()));

			DetalleVenta detalle = new DetalleVenta();
			detalle.setId(new DetalleVentaId(null, manga.getIdManga()));
			detalle.setVenta(venta);
			detalle.setManga(manga);
			detalle.setCantidad(seleccionado.getCantidad());
			detalle.setPrecio(manga.getPrecio());

			detalles.add(detalle);
			total = total.add(subtotal);

			manga.setStock(manga.getStock() - seleccionado.getCantidad());
			mangaRepository.save(manga);
		}

		venta.setMontoTotal(total);
		venta.setDetalles(detalles);

		ventaRepository.save(venta);

		return new ResultadoResponse(true, "Venta registrada correctamente");
	}

	@Transactional(readOnly = true)
	public List<Venta> listarVentas() {
		return ventaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Venta> buscarVentas(VentaFilter filter) {

		LocalDateTime fechaInicio = filter.getFechaInicio() == null ? LocalDateTime.of(1900, 1, 1, 0, 0)
				: filter.getFechaInicio().atStartOfDay();

		LocalDateTime fechaFin = filter.getFechaFin() == null ? LocalDateTime.of(9999, 1, 1, 0, 0)
				: filter.getFechaFin().plusDays(1).atStartOfDay();

		return ventaRepository.buscarVentas(filter.getIdVenta(), filter.getIdCliente(), fechaInicio, fechaFin);
	}

	@Transactional(readOnly = true)
	public List<DetalleVenta> obtenerReporteVentas() {
		return detalleVentaRepository.obtenerReporteVentas();
	}

}
