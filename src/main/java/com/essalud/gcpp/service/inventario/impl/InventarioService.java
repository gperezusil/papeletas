package com.essalud.gcpp.service.inventario.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.essalud.gcpp.entidades.inventario.Inventario;
import com.essalud.gcpp.repository.inventario.InventarioRepository;
import com.essalud.gcpp.service.inventario.IinventarioService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;



@Service
public class InventarioService implements IinventarioService {

	@Autowired private InventarioRepository inventarioRepository;
	@Autowired private UploadServiceInventarioImpl uploadServiceInventario;
		
    @Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Page<Inventario> findAll(String filter,Pageable page) {
		
		return inventarioRepository.findAll("%"+filter.toLowerCase()+"%", page);
	}

	@Override
	public Inventario save(Inventario inventario) {
		
		return inventarioRepository.save(inventario);
	}

	@Override
	public List<Inventario> findAllTodo() {
		
		return inventarioRepository.findAll();
	}

	@Override
	public void deleteById(Integer id) {
		inventarioRepository.deleteById(id);
		
	}

	@Override
	public Inventario findbyId(Integer id) {
		
		return inventarioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	public JasperPrint cargar() throws SQLException, JRException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		File jasper = uploadServiceInventario.getPath2("reporteProducto.jasper").toFile();
		Map<String, Object> parametros = new HashMap<String, Object>();
        JasperPrint jasperprint;
        jasperprint = JasperFillManager.fillReport(jasper.getPath(), parametros, conn);
		conn.close();
		return jasperprint;
	}

	@Override
	public List<Inventario> searchProducto(String filter) {
		return inventarioRepository.buscarProductoSearch("%"+filter.toLowerCase()+"%");
	}

}
