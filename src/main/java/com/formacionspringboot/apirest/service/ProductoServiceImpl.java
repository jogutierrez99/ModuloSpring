package com.formacionspringboot.apirest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionspringboot.apirest.dao.ProductoDao;
import com.formacionspringboot.apirest.entity.Cliente;
import com.formacionspringboot.apirest.entity.Producto;

@Service
public class ProductoServiceImpl implements ProductoService {
	
	@Autowired
	private ProductoDao repositorio;

	@Override
	public List<Producto> mostrarTodo() {
		// TODO Auto-generated method stub
		return (List<Producto>) repositorio.findAll();
	}

	//Buscar 1
	@Override
	@Transactional( readOnly = true)
	public Producto buscarPorId(long id) {
		// TODO Auto-generated method stub
		return repositorio.findById(id).orElse(null);
	}

	//update
	@Override
	@Transactional
	public Producto guardar(Producto producto) {
		// TODO Auto-generated method stub
		return repositorio.save(producto);
	}

	@Override
	@Transactional
	public void borrado(Long id) {
		// TODO Auto-generated method stub
		repositorio.deleteById(id);	
	}
	
	

}
