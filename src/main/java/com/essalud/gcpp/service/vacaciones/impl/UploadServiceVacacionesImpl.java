package com.essalud.gcpp.service.vacaciones.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.essalud.gcpp.service.vacaciones.IUploadServiceVacaciones;

@Service
public class UploadServiceVacacionesImpl implements IUploadServiceVacaciones{

	private final Logger log = LoggerFactory.getLogger(UploadServiceVacacionesImpl.class);
	//private final static String DIRECTORIO_UPLOAD = "E:\\vacaciones\\vacaciones\\resoluciones";
	private final static String DIRECTORIO_UPLOAD = "data/vacaciones/resoluciones";
	
	
	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {
		// TODO Auto-generated method stub
		Path rutaArchivo = getPath(nombreFoto);
		log.info(rutaArchivo.toString());
		Resource recurso = new UrlResource(rutaArchivo.toUri());
	
		
		return recurso;
	}

	@Override
	public String copiar(MultipartFile archivo) throws IOException {
		
		String nombreArchivo =  archivo.getOriginalFilename().replace(" ", "");
		Path rutaArchivo = getPath(nombreArchivo);
		if (rutaArchivo.toFile().exists()) {
			eliminar(nombreArchivo);
		}
		Files.copy(archivo.getInputStream(), rutaArchivo);
		
		return nombreArchivo;
	}

	@Override
	public boolean eliminar(String nombreFoto) {
		if (nombreFoto != null && nombreFoto.length() > 0) {
			Path rutaFotoAnterior = Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
			File archivoFotoAnterior = rutaFotoAnterior.toFile();

			if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
				archivoFotoAnterior.delete();
				return true;
			}
		}

		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {
		// TODO Auto-generated method stub
		return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
	}

}
