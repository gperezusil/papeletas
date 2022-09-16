package com.essalud.gcpp.service.personal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.essalud.gcpp.entidades.personal.Resolucion;



public interface IResolucionService {
	
	List<Resolucion> getAllMine(String dni);
	Resolucion agregarResolucion(Resolucion reso);
	Resolucion findbyResolucion(Long id);
	
	public Resource cargar(String nombreFoto) throws MalformedURLException ;
	
	public String copiar(MultipartFile archivo) throws IOException;
	
	public boolean eliminar(String nombreFoto);
	
	public Path getPath(String nombreFoto);
	

}
