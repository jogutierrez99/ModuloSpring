package com.formacionspringboot.apirest.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.formacionspringboot.apirest.entity.Cliente;
import com.formacionspringboot.apirest.entity.Region;
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
			clienteUpdate.setRegion(cliente.getRegion());	
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

			String nombreImagenAnterior = cliente.getImagen();

			if (nombreImagenAnterior != null && nombreImagenAnterior.length() > 0) {
				// accedemos a la ruta y al archivo como tal guardada en uploads
				Path rutaImagenAnterior = Paths.get("uploads").resolve(nombreImagenAnterior).toAbsolutePath();
				File archivoImagenAnterior = rutaImagenAnterior.toFile();
				// comprobamos la presencia fisica del archivo dentro del directorio
				if (archivoImagenAnterior.exists() && archivoImagenAnterior.canRead()) {
					// borramos el archivo
					archivoImagenAnterior.delete();
				}

			}
			
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

	@PostMapping("/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {

		Map<String, Object> response = new HashMap<>();

		Cliente cliente = servicio.buscarPorId(id);

		if (!archivo.isEmpty()) {
			// guarda el nombre de la imagen
			// String nombreArchivo = archivo.getOriginalFilename();
			String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
			// guarda el path de ruta donde guarda la imagen
			Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();

			try {
				
				String nombreImagenAnterior = cliente.getImagen();

				if (nombreImagenAnterior != null && nombreImagenAnterior.length() > 0) {
					// accedemos a la ruta y al archivo como tal guardada en uploads
					Path rutaImagenAnterior = Paths.get("uploads").resolve(nombreImagenAnterior).toAbsolutePath();
					File archivoImagenAnterior = rutaImagenAnterior.toFile();
					// comprobamos la presencia fisica del archivo dentro del directorio
					if (archivoImagenAnterior.exists() && archivoImagenAnterior.canRead()) {
						// borramos el archivo
						archivoImagenAnterior.delete();
					}

				}
				
				// copia la imagen recibida al directorio de path
				Files.copy(archivo.getInputStream(), rutaArchivo);

			} catch (Exception e) {
				// TODO: handle exception
				// controlamos las excepciones que podamos tener al subir archivos
				response.put("mensaje", "Error al subir la imagen del cliente");
				response.put("Error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	

			cliente.setImagen(nombreArchivo);
			servicio.guardar(cliente);

			response.put("Cliente", cliente);
			response.put("Mensaje", "Imagen subida con éxito: " + nombreArchivo);

			// fin If
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
							//hay que añadir esto :.+ al final para que reconoxca la extension
	@GetMapping("clientes/imagen/{nombreImagen:.+}")
	public ResponseEntity<Resource> verImagen(@PathVariable String nombreImagen){
		
		Path rutaArchivo = Paths.get("uploads").resolve(nombreImagen).toAbsolutePath();
		
		Resource recurso = null;
		
		try {
			recurso = new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!recurso.exists() && !recurso.isReadable()) {
			throw new RuntimeException("Error no se puede cargar la imagen"+ nombreImagen);
		}
		
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+recurso.getFilename()+"\"");
		
		
		return new ResponseEntity<Resource>(recurso,cabecera,HttpStatus.OK);
	}
	
	@GetMapping("/clientes/regiones")
	public List<Region> listaRegiones(){
		return servicio.buscarTodasLasRegiones();
	}
	
	//No funciona
//	@GetMapping("/clientes/{apellido}")
//	public List<Cliente> listaApellido(@PathVariable String apellido){
//		return servicio.buscarPorApellido(apellido);
//	}
	

}
