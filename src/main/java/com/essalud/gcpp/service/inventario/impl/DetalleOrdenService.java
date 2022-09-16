package com.essalud.gcpp.service.inventario.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.essalud.gcpp.entidades.inventario.DetalleOrdenProveedor;
import com.essalud.gcpp.entidades.inventario.Inventario;
import com.essalud.gcpp.repository.inventario.DetalleOrdenRepository;
import com.essalud.gcpp.service.inventario.IDetalleOrdenService;

@Service
public class DetalleOrdenService implements IDetalleOrdenService{

	@Autowired DetalleOrdenRepository detalleOrdenRepository;
	
	@Autowired
	InventarioService inventarioService;
	
	@Override
	public Page<DetalleOrdenProveedor> listarDetalleOrden(Integer id, Pageable page) {

		return detalleOrdenRepository.listarDetalleOrden(id, page);
	}

	@Override
	public DetalleOrdenProveedor save(DetalleOrdenProveedor detalle) {
		DetalleOrdenProveedor ordena= detalleOrdenRepository.save(detalle);
		Inventario producto =inventarioService.findbyId(ordena.getProducto().getId());
		producto.setStockActual(producto.getStockActual()+ordena.getCantidad());
		inventarioService.save(producto);
		return ordena;
	}

}
