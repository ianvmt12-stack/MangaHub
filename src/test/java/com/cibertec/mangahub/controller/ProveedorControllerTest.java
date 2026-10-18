package com.cibertec.mangahub.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.cibertec.mangahub.model.Proveedor;
import com.cibertec.mangahub.service.ProveedorService;

@ExtendWith(MockitoExtension.class)
class ProveedorControllerTest {

    @Mock
    private ProveedorService service;

    private MockMvc mvc;

    @BeforeEach
    void prepararVistas() {
        ClassLoaderTemplateResolver templates = new ClassLoaderTemplateResolver();
        templates.setPrefix("templates/");
        templates.setSuffix(".html");
        templates.setTemplateMode("HTML");
        templates.setCharacterEncoding("UTF-8");
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templates);
        ThymeleafViewResolver views = new ThymeleafViewResolver();
        views.setTemplateEngine(engine);
        views.setCharacterEncoding("UTF-8");
        mvc = MockMvcBuilders.standaloneSetup(new ProveedorController(service)).setViewResolvers(views).build();
    }

    @Test
    void renderizaElFormularioNuevoConLaRutaDeRegistro() throws Exception {
        mvc.perform(get("/proveedores/nuevo"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("action=\"/proveedores/guardar\"")))
                .andExpect(content().string(containsString("name=\"ruc\"")));
    }

    @Test
    void renderizaLaEdicionConDatosYLaRutaDelProveedor() throws Exception {
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(7);
        proveedor.setRuc("20123456789");
        proveedor.setRazonSocial("Editorial Manga");
        when(service.buscarPorId(7)).thenReturn(proveedor);

        mvc.perform(get("/proveedores/editar/7"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("action=\"/proveedores/actualizar/7\"")))
                .andExpect(content().string(containsString("value=\"Editorial Manga\"")));
    }

    @Test
    void conservaElFormularioYLaRutaDeEdicionCuandoHayUnRucRepetido() throws Exception {
        when(service.actualizar(eq(7), any())).thenThrow(new IllegalArgumentException("RUC repetido."));

        mvc.perform(post("/proveedores/actualizar/7")
                        .param("ruc", "20123456789").param("razonSocial", "Editorial Manga"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("RUC repetido.")))
                .andExpect(content().string(containsString("value=\"20123456789\"")))
                .andExpect(content().string(containsString("action=\"/proveedores/actualizar/7\"")));
    }

    @Test
    void muestraUnListadoVacioConLaBusquedaConservada() throws Exception {
        when(service.listarProveedores("  Editorial  ")).thenReturn(List.of());

        mvc.perform(get("/proveedores").param("q", "  Editorial  "))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No se encontraron proveedores.")))
                .andExpect(content().string(containsString("value=\"Editorial\"")));
    }

    @Test
    void muestraLasAccionesSegunElEstadoDelProveedor() throws Exception {
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(7);
        proveedor.setActivo(false);
        when(service.listarProveedores(null)).thenReturn(List.of(proveedor));

        mvc.perform(get("/proveedores"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("action=\"/proveedores/activar/7\"")))
                .andExpect(content().string(containsString("Inactivo")));
    }

    @Test
    void responde404ParaUnaEdicionInexistente() throws Exception {
        when(service.buscarPorId(99)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mvc.perform(get("/proveedores/editar/99")).andExpect(status().isNotFound());
    }
}
