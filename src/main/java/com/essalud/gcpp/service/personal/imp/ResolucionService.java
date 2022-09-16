package com.essalud.gcpp.service.personal.imp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.essalud.gcpp.entidades.personal.Resolucion;
import com.essalud.gcpp.repository.personal.IResolucionDao;
import com.essalud.gcpp.service.personal.IResolucionService;



@Service
public class ResolucionService implements IResolucionService{
	
	private final static String DIRECTORIO_UPLOAD = "E:\\vacaciones\\resoluciones";
	//private final static String DIRECTORIO_UPLOAD = "data/resoluciones";
	@Autowired
	private IResolucionDao resolucionDao;
	
	@Override
	public List<Resolucion> getAllMine(String dni) {
		
		return resolucionDao.findByDni(dni);
	}

	@Override
	public Resolucion agregarResolucion(Resolucion reso) {
		// TODO Auto-generated method stub
		return resolucionDao.save(reso);
	}

	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {
		Path rutaArchivo = getPath(nombreFoto);

		Resource recurso = new UrlResource(rutaArchivo.toUri());
	
		
		return recurso;
	}

	@Override
	public String copiar(MultipartFile archivo) throws IOException {
		String nombreArchivo =  archivo.getOriginalFilename();
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
				
				return archivoFotoAnterior.delete();
			}
		}

		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {

				return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
	}

	@Override
	public Resolucion findbyResolucion(Long id) {
		
		return resolucionDao.findById(id).orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

}
