package com.essalud.gcpp.service.personal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadServicePersonal {
	
	public Resource cargar(String nombreFoto) throws MalformedURLException ;
	
	public String copiar(MultipartFile archivo,String dni) throws IOException;
	
	public boolean eliminar(String nombreFoto);
	
	public Path getPath(String nombreFoto);
	
	public Resource cargarLocador(String nombreFoto) throws MalformedURLException ;
	
	public String copiarLocador(MultipartFile archivo) throws IOException;
	
	public boolean eliminarLocador(String nombreFoto);
	
	public Path getPathLocador(String nombreFoto);
}
