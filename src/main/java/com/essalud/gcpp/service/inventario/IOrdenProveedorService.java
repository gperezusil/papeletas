package com.essalud.gcpp.service.inventario;

import java.sql.SQLException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.essalud.gcpp.entidades.inventario.OrdenProveedor;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface IOrdenProveedorService {
	
	OrdenProveedor save(OrdenProveedor orden);
	Page<OrdenProveedor> listarOrdenProducto(String filtro,Pageable pageable);
	
	OrdenProveedor findById(Integer id);
	
	OrdenProveedor actualizar(OrdenProveedor orden);
	
	
}
