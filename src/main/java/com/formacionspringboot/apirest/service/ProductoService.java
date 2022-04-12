package com.formacionspringboot.apirest.service;

import java.util.List;

import com.formacionspringboot.apirest.entity.Producto;

public interface ProductoService {

	public List<Producto> mostrarTodo();

	public Producto buscarPorId(long id);

	public Producto guardar(Producto cliente);

	public void borrado(Long id);

}
