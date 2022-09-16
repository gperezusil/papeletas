package com.essalud.gcpp.service.papeletas.impl;

import java.io.File;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.entidades.papeletas.Boleta;
import com.essalud.gcpp.repository.papeletas.IBoletaDao;
import com.essalud.gcpp.service.papeletas.IUploadService;



@Service
public class UploadServicePapeletasImpl implements IUploadService{


	private final static String DIRECTORIO_UPLOAD = "data/papeletas/boleta";
	//private final static String DIRECTORIO_UPLOAD = "E:\\vacaciones\\papeletas\\boleta";
	
	@Autowired
	private IBoletaDao boletaRepository;
	
	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {
		
		Path rutaArchivo = getPath(nombreFoto);
		Resource recurso = new UrlResource(rutaArchivo.toUri());
	
		
		return recurso;
	}

	@Override
	public OperationResponse copiar(MultipartFile archivo,Long id) {
		OperationResponse response =null;
		Path rutaArchivo = getPath(archivo.getOriginalFilename());
		try {
			
			Boleta bo = boletaRepository.findByIdBo(id);
			if(bo.getArchivoBoleta()!=null) {
				if(getPath(bo.getArchivoBoleta()).toFile().exists()) {
					eliminar(bo.getArchivoBoleta());
					Files.copy(archivo.getInputStream(), rutaArchivo);
				}
			}
			bo.setArchivoBoleta(archivo.getOriginalFilename());
			bo.setEstado("1");
			boletaRepository.save(bo);
			response = new OperationResponse(true, "Exito al Subir el archivo");
		} catch (Exception e) {
			response = new OperationResponse(false,"El archivo ya existe");
		}

		
		
		return response;
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

}
