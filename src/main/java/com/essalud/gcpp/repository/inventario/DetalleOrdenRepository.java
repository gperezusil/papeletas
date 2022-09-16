package com.essalud.gcpp.repository.inventario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.essalud.gcpp.entidades.inventario.DetalleOrdenProveedor;

public interface DetalleOrdenRepository  extends JpaRepository<DetalleOrdenProveedor, Integer>{

	@Query("select d from DetalleOrdenProveedor d where d.orden.id=?1")
	Page<DetalleOrdenProveedor> listarDetalleOrden(Integer idOrden,Pageable page);
}
