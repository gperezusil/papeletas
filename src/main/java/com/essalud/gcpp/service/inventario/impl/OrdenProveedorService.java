package com.essalud.gcpp.service.inventario.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.essalud.gcpp.entidades.inventario.Inventario;
import com.essalud.gcpp.entidades.inventario.OrdenProveedor;
import com.essalud.gcpp.repository.inventario.OrdenProveedorRepository;
import com.essalud.gcpp.service.inventario.IOrdenProveedorService;
@Service
public class OrdenProveedorService implements IOrdenProveedorService{

	@Autowired
	OrdenProveedorRepository ordenProveedorRepository;
	
	@Autowired
	InventarioService inventarioService;
	
	@Override
	public OrdenProveedor save(OrdenProveedor orden) {
		orden.setFecha(new Date());
		return ordenProveedorRepository.save(orden);
	}

	@Override
	public Page<OrdenProveedor> listarOrdenProducto(String filtro,Pageable page) {
		
		return ordenProveedorRepository.findOrdenFiltro("%"+filtro.toLowerCase()+"%",page);
	}

	@Override
	public OrdenProveedor findById(Integer id) {
		
		return ordenProveedorRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	public OrdenProveedor actualizar(OrdenProveedor orden) {
		// TODO Auto-generated method stub
		return ordenProveedorRepository.save(orden);
	}

}
