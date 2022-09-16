package com.essalud.gcpp.service.personal.imp;

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

import com.essalud.gcpp.service.personal.IUploadServicePersonal;




@Service
public class UploadServiceImpl implements IUploadServicePersonal{

	private final Logger log = LoggerFactory.getLogger(UploadServiceImpl.class);
	//private final static String DIRECTORIO_UPLOAD = "fotos";
	private final static String DIRECTORIO_UPLOAD_LOCADOR = "data/locadores";
	private final static String DIRECTORIO_UPLOAD = "data/fotos_personal";
	//private final static String DIRECTORIO_UPLOAD_LOCADOR = "E:\\vacaciones\\locadores";
	//private final static String DIRECTORIO_UPLOAD = "E:\\vacaciones\\fotos_personal";
	
	
	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {
		
		File archivo = Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toFile();
		if (!archivo.exists()) {
		    nombreFoto="sin-foto.jpg";
		}
		Path rutaArchivo = getPath(nombreFoto);
		log.info(rutaArchivo.toString());
		Resource recurso = new UrlResource(rutaArchivo.toUri());
	
		
		return recurso;
	}

	@Override
	public String copiar(MultipartFile archivo,String dni) throws IOException {
		
		String nombreArchivo =  dni+".jpg";
		Path rutaArchivo = getPath(nombreArchivo);


		if (rutaArchivo.toFile().exists()) {
			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY);
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

	@Override
	public Resource cargarLocador(String nombreFoto) throws MalformedURLException {
		File archivo = Paths.get(DIRECTORIO_UPLOAD_LOCADOR).resolve(nombreFoto).toFile();
		if (!archivo.exists()) {
		    nombreFoto="sin-foto.jpg";
		}
		Path rutaArchivo = getPathLocador(nombreFoto);
		log.info(rutaArchivo.toString());
		Resource recurso = new UrlResource(rutaArchivo.toUri());
	
		
		return recurso;
	}

	@Override
	public String copiarLocador(MultipartFile archivo) throws IOException {
		String nombreArchivo =  archivo.getResource().getFilename();
		Path rutaArchivo = getPathLocador(nombreArchivo);
		log.info(rutaArchivo.toString());
		if(rutaArchivo.toFile().exists()) {
			System.out.println("Ya registrado");
		}else {
			Files.copy(archivo.getInputStream(), rutaArchivo);
		}
		
		
		return nombreArchivo;
	}

	@Override
	public boolean eliminarLocador(String nombreFoto) {
		if (nombreFoto != null && nombreFoto.length() > 0) {
			Path rutaFotoAnterior = Paths.get(DIRECTORIO_UPLOAD_LOCADOR).resolve(nombreFoto).toAbsolutePath();
			File archivoFotoAnterior = rutaFotoAnterior.toFile();

			if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
				archivoFotoAnterior.delete();
				return true;
			}
		}

		return false;
	}

	@Override
	public Path getPathLocador(String nombreFoto) {
		// TODO Auto-generated method stub
		return Paths.get(DIRECTORIO_UPLOAD_LOCADOR).resolve(nombreFoto).toAbsolutePath();
	}


}
