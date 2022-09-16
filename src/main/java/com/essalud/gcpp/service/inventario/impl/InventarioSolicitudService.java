package com.essalud.gcpp.service.inventario.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.essalud.gcpp.dto.inventario.SolicitudInventarioDTO;
import com.essalud.gcpp.entidades.Users;
import com.essalud.gcpp.entidades.inventario.Inventario;
import com.essalud.gcpp.entidades.inventario.Solicitud;
import com.essalud.gcpp.entidades.personal.Personal;
import com.essalud.gcpp.repository.inventario.InventarioSolicitudRepository;
import com.essalud.gcpp.repository.personal.IEmpleadoDao;
import com.essalud.gcpp.service.inventario.IinventarioService;
import com.essalud.gcpp.service.inventario.IinventarioSolicitudService;

@Service
public class InventarioSolicitudService implements IinventarioSolicitudService{

	@Autowired private InventarioSolicitudRepository inventarioSolicitud;
	
	@Autowired private IEmpleadoDao  personalRepository;
	@Autowired private IinventarioService  inventarioService;
	
	@Override
	public Page<Object[]> listarSolicitudesPendientes(String filtro,Pageable pageable) {
		
		return inventarioSolicitud.listarSolicitudInventario("%"+filtro.toLowerCase()+"%",pageable);
	}
	
	@Override
	public Page<Object[]> listarSolicitudesPendientesPersonal(String filtro, Users u, Pageable pageable) {
		
		return inventarioSolicitud.listarSolicitudInventarioPersonal("%"+filtro.toLowerCase()+"%",u.getUsername(),pageable);
	}
	@Override
	public Page<Object[]> listarSolicitudesPendientesHistorial(String filtro, Pageable pageable) {
		return inventarioSolicitud.listarSolicitudInventarioHistorial("%"+filtro.toLowerCase()+"%", pageable);
	}

	@Override
	public Solicitud save(SolicitudInventarioDTO solicitud,Users u) {
		Inventario producto  = inventarioService.findbyId(solicitud.getProducto());
		if(producto.getStockActual()>solicitud.getCantidad()) {
			producto.setStockActual(producto.getStockActual()-solicitud.getCantidad());
			inventarioService.save(producto);
			return inventarioSolicitud.save(new Solicitud(null,producto,solicitud.getCantidad(),0,u.getUsername(),new Date(),0));
		}else {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
			
		
	}


	@Override
	public Solicitud updateStatus(Solicitud solicitud) {
		Inventario producto  = inventarioService.findbyId(solicitud.getProducto().getId());
		if(solicitud.getEstado()==2) {
			producto.setStockActual(producto.getStockActual()+solicitud.getCantidadSol());
			inventarioService.save(producto);
			inventarioSolicitud.save(solicitud);
		}else if(solicitud.getEstado()==1){
			producto.setStockActual(producto.getStockActual()+solicitud.getCantidadSol()-solicitud.getCantEntr());
			inventarioService.save(producto);
		}
		return inventarioSolicitud.save(solicitud);
	}

	@Override
	public Page<Solicitud> listarSolicitudesHistorial(Pageable pageable) {
		
		return inventarioSolicitud.listarHistorialSolicitud(pageable);
	}

	@Override
	public Page<Solicitud> listarmisSolicitudes(Pageable pageable, Users u) {
		
		return inventarioSolicitud.listarMisSolicitudes(u.getUsername(), pageable);
	}

	@Override
	public List<Solicitud> listarHistorialTotal() {
		
		return inventarioSolicitud.listarHistorialTodo();
	}

	@Override
	public Page<Solicitud> listarSolicitudPersonalFecha(String dni, String fecha, Pageable pageable) {
		
		return inventarioSolicitud.findByPersonalAndFecha(dni, fecha, pageable);
	}

	
	

}
