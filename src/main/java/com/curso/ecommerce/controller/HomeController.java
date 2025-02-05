package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IDetalleOrdenService;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;


@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private ProductoService productoService; //SERVICIO PRODUCTO
	
	@Autowired
	private IUsuarioService usuarioService; //SERVICIO USUARIO
	
	@Autowired
	private IOrdenService ordenService; // SERVICIO ORDEN
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService; // SERVICIO DETALLE
	
	//PARA ALMACENAR LOS DETALLES DE LA ORDEN
	List<DetalleOrden> detalles = new ArrayList<>();
	
	//DATOS DE LA ORDEN
	Orden orden = new Orden();

	@GetMapping("")//CONTROLADOR QUE LLEVA AL INICIO Y LISTA LOS PRODUCTOS
	public String home(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "usuario/home";
	}

	//CONTROLADOR QUE ENVIA UN PRODUCTO A LA VISTA Y LLEVA A VER LA DESCRIPCION DEL PRODUCTO 
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		log.info("Id producto enviado como parametro {}", id);
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();
		model.addAttribute("producto", producto);

		return "usuario/productohome";
	}
	
	//CONTROLADOR QUE LLEVA ALA VISTA Y AÑADE UN PRODUCTO AL CARRITO DE COMPRAS
	//SE ENVIA LA CANTIDAD Y EL PRODUCTO EN EL CUERPO DE LA SOLICITUD POST
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;
		
		Optional<Producto> optionalProducto = productoService.get(id);
		log.info("Producto añadido: {}", optionalProducto.get());
		log.info("Cantidad: {}", cantidad);
		producto = optionalProducto.get();
		
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);
		
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
		
		if (!ingresado) {
			detalles.add(detalleOrden);
		}
		
		//detalles.add(detalleOrden);
		
		sumaTotal = detalles.stream().mapToDouble(td -> td.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	//QUITAR UN PRODUCTO DE UN CARRITO
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id,Model model) {
	
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();
		
		for (DetalleOrden detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId() != id) {
				ordenesNueva.add(detalleOrden);
			}
		}
		//PONER LA NUEVA LISTA CON LOS PRODUCTOS RESTANTES
		detalles = ordenesNueva;
		
		double sumaTotal = 0;
		
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	//CONTROLADOR PARA ABRIR/CONSULTAR LA VISTA CARRITO SIN ENVIAR DATOS.
	@GetMapping("/getCart")
	public String getCart(Model model) {
		
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	
	@GetMapping("/order")
	public String order(Model model) {
		
		Usuario usuario = usuarioService.findById(1).get();
		
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario",usuario);
		
		return "usuario/resumenorden";
	}
	
	//CONTROLADOR PARA GUARDAR LA ORDEN
	@GetMapping("/saveOrder")
	public String saveOrder() {
		
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		//USUARIO
		Usuario usuario = usuarioService.findById(1).get();
		
		orden.setUsuario(usuario);
		ordenService.save(orden);
		
		//GUARADAR DETALLES
		for (DetalleOrden dt : detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}
		
		//LIMPIEZA LISTA Y ORDEN
		orden = new Orden();
		detalles.clear();
		
		
		
		return "redirect:/";
	}
	
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre,Model model) {
		
		log.info("Nombre del produto: {}", nombre);
		List<Producto> productos = productoService.findAll().stream().filter(p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
		
		model.addAttribute("productos",productos);
		
		
		return "usuario/home";
	}

}






















