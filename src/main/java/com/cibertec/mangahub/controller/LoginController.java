
package com.cibertec.mangahub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.cibertec.mangahub.dto.LoginDTO;
import com.cibertec.mangahub.model.Usuario;
import com.cibertec.mangahub.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/login")
	public String login(Model model) {

		model.addAttribute("login", new LoginDTO());

		return "login";
	}

	@PostMapping("/login")
	public String validarLogin(@ModelAttribute("login") LoginDTO login, HttpSession session, Model model) {

		Usuario usuario = usuarioService.validarLogin(login.getUsername(), login.getPassword());

		if (usuario != null) {

			session.setAttribute("usuario", usuario.getNombres());
			session.setAttribute("rol", usuario.getRol());

			return "redirect:/";
		}

		model.addAttribute("error", "Usuario o contraseña incorrectos");

		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {

		session.invalidate();

		return "redirect:/login";
	}
}
