package com.essalud.gcpp.service.personal;



import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.essalud.gcpp.entidades.personal.Personal;
import com.essalud.gcpp.entidades.personal.Rol;


import net.sf.jasperreports.engine.JasperPrint;

public interface IPersonalService {
	
	Page<Personal> buscarPersonalArea(String cod00, Pageable pageable);
	
	Personal buscarUsuario(String dni);
	
	List<Personal> listarCumpleanos();
	
	List<Rol> listarRoles();
	
	Personal crearPersonal(Personal personal);
	
	List<Personal> listarTodos();
	
	void deleteById(String dni);
	
	boolean cargarArchivoBoletaPago(MultipartFile files);

	void cambiarclave() ;
	
	public JasperPrint reportePersonal() ;
	
	Resource descargarManual();
	
	Page<Personal> buscarporFiltro(String filtro, String ceco, Pageable pageable);
	
	List<Personal> buscarporFiltro2(String filtro);
	
	
}
