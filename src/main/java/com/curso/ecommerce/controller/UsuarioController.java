package com.curso.ecommerce.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private IUsuarioService usuarioService;

	
	// USUARIO/REGISTRO
	@GetMapping("/registro")
	public String create() {
		return "usuario/registro";
	}
	
	@PostMapping("/save")
	public String save(Usuario usuario) {
		log.info("Usuario Registro: {}" , usuario);
		usuario.setTipo("USER");
		usuarioService.save(usuario);
		return "redirect:/";
	}
	
	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}
	
	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		log.info("Accesos: {}" ,usuario);
		Optional<Usuario> user = usuarioService.findByEmail(usuario.getEmail());
		//log.info("Usuario Obtenido: {}", user.get());
		if (user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			if (user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			} else {
				return "redirect:/";
			}
		} else {
			log.info("Usuario no Existe");
		}
		
		return "redirect:/";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
