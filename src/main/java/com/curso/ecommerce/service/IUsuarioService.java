package com.curso.ecommerce.service;

import java.util.Optional;

import com.curso.ecommerce.model.Usuario;

public interface IUsuarioService {
	
	//BUSCAR USUARIO POR ID
	Optional<Usuario> findById(Integer id);
	

}
