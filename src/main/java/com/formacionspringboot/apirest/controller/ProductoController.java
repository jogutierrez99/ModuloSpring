package com.formacionspringboot.apirest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formacionspringboot.apirest.entity.Cliente;
import com.formacionspringboot.apirest.entity.Producto;
import com.formacionspringboot.apirest.service.ProductoService;

@RestController
@RequestMapping("/api")
public class ProductoController {
	
	@Autowired
	private ProductoService servicio;
	
	// mostrar todos
		@GetMapping("/productos")
		public List<Producto> mostrarProductos() {
			return servicio.mostrarTodo();
		};
		
	//Muestra 1 producto por id
		
		@GetMapping("/productos/{id}")
		public ResponseEntity<?> buscarProducto(@PathVariable long id) {

			Producto producto = null;
			Map<String, Object> response = new HashMap<>();

			try {
				producto = servicio.buscarPorId(id);
			} catch (DataAccessException e) {
				// TODO: handle exception
				response.put("Mensaje", "Error al realizar consulta a base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			if (producto == null) {
				response.put("mensaje", "El producto con ID: " + id + " no existe ");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<Producto>(producto, HttpStatus.OK);
		}
		
	//Añadir producto
		
		@PostMapping("/productos")
		public ResponseEntity<?> guardarProducto(@RequestBody Producto producto) {

			Producto nuevoProducto = null;
			Map<String, Object> response = new HashMap<>();

			try {

				nuevoProducto = servicio.guardar(producto);

			} catch (DataAccessException e) {
				// TODO: handle exception
				response.put("Mensaje", "Error al insertar en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			response.put("mensaje", "El cliente ha sido creado con éxito");
			response.put("Cliente", nuevoProducto);

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		};
	
		//Actualizar Producto
		@PutMapping("/productos/{id}")
		public ResponseEntity<?> actualizarProducto(@RequestBody Producto producto, @PathVariable long id) {

			Producto productoUpdate = null;
			Map<String, Object> response = new HashMap<>();

			productoUpdate = servicio.buscarPorId(id);

			if (productoUpdate == null) {
				response.put("Mensaje",
						"Error: No se pudo editar, el producto con ID: " + id + " no existe en la base de datos");

				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}

			try {

				productoUpdate.setNombre(producto.getNombre());
				productoUpdate.setDescripcion(producto.getDescripcion());
				productoUpdate.setFechaPedido(producto.getFechaPedido());
				servicio.guardar(productoUpdate);

			} catch (DataAccessException e) {
				// TODO: handle exception
				response.put("Mensaje", "Error al actualizar la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			response.put("Mensaje", "El cliente ha sido actualizado con exito");
			response.put("Cliente", productoUpdate);

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		};

		//Eliminar Producto
		@DeleteMapping("/productos/{id}")
		public ResponseEntity<?> borrarPrducto(@PathVariable long id) {
			
			Producto producto = null;
			Map<String, Object> response = new HashMap<>();
			
			producto = servicio.buscarPorId(id);
			
			if (producto == null) {
				response.put("Mensaje",
						"Error: No se pudo eliminar el producto con ID: " + id + " no existe en la base de datos");

				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}

			try {

				servicio.borrado(id);

			} catch (DataAccessException e) {
				// TODO: handle exception
				response.put("Mensaje", "Error al eliminar en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			response.put("Mensaje", "El Producto ha sido eliminado con exito!!");

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}
}
