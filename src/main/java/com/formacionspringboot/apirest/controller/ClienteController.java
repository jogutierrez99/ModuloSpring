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
import com.formacionspringboot.apirest.service.ClienteService;

@RestController
@RequestMapping("/api")
public class ClienteController {

	@Autowired
	private ClienteService servicio;

	// mostrar todos
	@GetMapping("/clientes")
	public List<Cliente> mostrarClientes() {
		return servicio.mostrarTodo();
	};

	// buscar 1 por id
//	@GetMapping("/clientes/{id}")
//	public Cliente buscarCliente(@PathVariable long id) {
//		return servicio.buscarPorId(id);
//	}

	// Buscar1 cliente por id
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> buscarCliente(@PathVariable long id) {

		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();

		try {
			cliente = servicio.buscarPorId(id);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("Mensaje", "Error al realizar consulta a base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (cliente == null) {
			response.put("mensaje", "El cliente con ID: " + id + " no existe ");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}

	// Añadir usuario
//	@PostMapping("/clientes")
//	public Cliente guardarCliente(@RequestBody Cliente cliente) {
//		return servicio.guardar(cliente);
//	};

	@PostMapping("/clientes")
	public ResponseEntity<?> guardarCliente(@RequestBody Cliente cliente) {

		Cliente nuevoCliente = null;
		Map<String, Object> response = new HashMap<>();

		try {

			nuevoCliente = servicio.guardar(cliente);

		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("Mensaje", "Error al insertar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El cliente ha sido creado con éxito");
		response.put("Cliente", nuevoCliente);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	};

	// update usuario
//	@PutMapping("/clientes/{id}")
//	public Cliente actualizarCliente(@RequestBody Cliente cliente, @PathVariable long id) {
//
//		Cliente clienteUpdate = servicio.buscarPorId(id);
//
//		clienteUpdate.setNombre(cliente.getNombre());
//		clienteUpdate.setApellido(cliente.getApellido());
//		clienteUpdate.setEmail(cliente.getEmail());
//		clienteUpdate.setTelefono(cliente.getTelefono());
//
//		return servicio.guardar(clienteUpdate);
//	};

	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> actualizarCliente(@RequestBody Cliente cliente, @PathVariable long id) {

		Cliente clienteUpdate = null;
		Map<String, Object> response = new HashMap<>();

		clienteUpdate = servicio.buscarPorId(id);

		if (clienteUpdate == null) {
			response.put("Mensaje",
					"Error: No se pudo editar, el cliente con ID: " + id + " no existe en la base de datos");

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			clienteUpdate.setNombre(cliente.getNombre());
			clienteUpdate.setApellido(cliente.getApellido());
			clienteUpdate.setEmail(cliente.getEmail());
			clienteUpdate.setTelefono(cliente.getTelefono());
			servicio.guardar(clienteUpdate);

		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("Mensaje", "Error al actualizar la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("Mensaje", "El cliente ha sido actualizado con exito");
		response.put("Cliente", clienteUpdate);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	};

	// eliminar usuario
//	@DeleteMapping("/clientes/{id}")
//	public void borrarCliente(@PathVariable long id) {
//		servicio.borrado(id);
//	}

	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> borrarCliente(@PathVariable long id) {
		
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();
		
		cliente = servicio.buscarPorId(id);
		
		if (cliente == null) {
			response.put("Mensaje",
					"Error: No se pudo eliminar el cliente con ID: " + id + " no existe en la base de datos");

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
		
		response.put("Mensaje", "El cliente ha sido eliminado con exito!!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
