package com.essalud.gcpp.repository.inventario;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.essalud.gcpp.entidades.inventario.Inventario;



public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
	
	  
	  @Query("select i from Inventario i where (lower(i.nombre) like ?1 or lower(i.descripcion)  like ?1 or lower(i.marca) like ?1) and rownum <= 6")
	  List<Inventario> buscarProductoSearch(String filter);
	  @Query("select i from Inventario i where (lower(i.nombre) like ?1 or lower(i.descripcion)  like ?1 or lower(i.marca) like ?1)")
	  Page<Inventario> findAll(String filter,Pageable page);
}
