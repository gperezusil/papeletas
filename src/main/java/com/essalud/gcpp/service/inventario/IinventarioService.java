package com.essalud.gcpp.service.inventario;

import java.sql.SQLException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.essalud.gcpp.entidades.inventario.Inventario;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;



public interface IinventarioService {

	Page<Inventario> findAll(String filter,Pageable page);
	List<Inventario> findAllTodo();
	Inventario save(Inventario inventario);
	
	void deleteById(Integer id);
	Inventario findbyId(Integer id);
	
	JasperPrint cargar() throws  SQLException, JRException;
	
	List<Inventario> searchProducto(String filter);

	
}
