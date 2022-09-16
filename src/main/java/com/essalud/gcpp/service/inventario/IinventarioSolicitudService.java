package com.essalud.gcpp.service.inventario;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.essalud.gcpp.dto.inventario.SolicitudInventarioDTO;
import com.essalud.gcpp.entidades.Users;
import com.essalud.gcpp.entidades.inventario.Solicitud;

public interface IinventarioSolicitudService {
	
	Page<Object[]> listarSolicitudesPendientes(String filtro,Pageable pageable);
	Page<Object[]> listarSolicitudesPendientesPersonal(String filtro,Users u,Pageable pageable);
	Page<Object[]> listarSolicitudesPendientesHistorial(String filtro,Pageable pageable);
	Page<Solicitud> listarSolicitudPersonalFecha(String dni , String fecha ,Pageable pageable);
	
	
	Solicitud save(SolicitudInventarioDTO solicitud,Users u);
	
	Solicitud updateStatus(Solicitud solicitud);
	
	Page<Solicitud> listarSolicitudesHistorial(Pageable pageable);
	
	Page<Solicitud> listarmisSolicitudes(Pageable pageable ,Users u);
	
	List<Solicitud> listarHistorialTotal();
	
	

}
