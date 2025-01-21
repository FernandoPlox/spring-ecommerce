package com.curso.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	@Autowired
	ProductoService productoService;
	
	@GetMapping("")
	public String show() {
		return "productos/show";
	}

}
