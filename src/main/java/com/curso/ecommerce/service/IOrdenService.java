package com.curso.ecommerce.service;

import java.util.List;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;

public interface IOrdenService {
	
	//LISTAR LAS ORDENES
	public List<Orden> findAll();
	//GUARDAR UNA ORDEN
	public Orden save(Orden orden);
	
	public String generarNumeroOrden();
	
	public List<Orden> findByUsuario(Usuario usuario);
}
