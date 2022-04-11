package com.formacionspringboot.apirest.service;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionspringboot.apirest.dao.ClienteDao;
import com.formacionspringboot.apirest.entity.Cliente;

@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteDao repositorio;
	
	@Override
	@Transactional( readOnly = true)
	public List<Cliente> mostrarTodo() {
		// TODO Auto-generated method stub
		return (List<Cliente>) repositorio.findAll();
	}

	//Buscar 1
	@Override
	@Transactional( readOnly = true)
	public Cliente buscarPorId(long id) {
		// TODO Auto-generated method stub
		return repositorio.findById(id).orElse(null);
	}

	//update
	@Override
	@Transactional
	public Cliente guardar(Cliente cliente) {
		// TODO Auto-generated method stub
		return repositorio.save(cliente);
	}

	//delete
	@Override
	@Transactional
	public void borrado(Long id) {
		// TODO Auto-generated method stub
		repositorio.deleteById(id);
		
	}
	

}
