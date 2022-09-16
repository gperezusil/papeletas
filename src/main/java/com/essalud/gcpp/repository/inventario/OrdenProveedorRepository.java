package com.essalud.gcpp.repository.inventario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.essalud.gcpp.entidades.inventario.OrdenProveedor;

public interface OrdenProveedorRepository extends JpaRepository<OrdenProveedor, Integer> {

	@Query("select o from OrdenProveedor o where ( lower(rucProveedor) like ?1 or lower(nombreProveedor) like ?1 or lower(nombreProveedor) like ?1 )")
	Page<OrdenProveedor> findOrdenFiltro(String filtro,Pageable page);
	
	
}
