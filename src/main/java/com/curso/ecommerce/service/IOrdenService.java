package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;

public interface IOrdenService {
	
	//LISTAR LAS ORDENES
	public List<Orden> findAll();
	
	Optional<Orden> findById(Integer id);
	
	//GUARDAR UNA ORDEN
	public Orden save(Orden orden);
	
	public String generarNumeroOrden();
	
	public List<Orden> findByUsuario(Usuario usuario);
}
