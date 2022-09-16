package com.essalud.gcpp.service.inventario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.essalud.gcpp.entidades.inventario.DetalleOrdenProveedor;

public interface IDetalleOrdenService {

	Page<DetalleOrdenProveedor> listarDetalleOrden(Integer id , Pageable page);
	DetalleOrdenProveedor save (DetalleOrdenProveedor detalle);
}
