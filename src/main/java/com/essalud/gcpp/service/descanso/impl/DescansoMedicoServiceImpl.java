package com.essalud.gcpp.service.descanso.impl;



import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.entidades.descanso.DescansoMedico;
import com.essalud.gcpp.repository.descanso.DescansoMedicoRepository;
import com.essalud.gcpp.service.descanso.DescansoService;

@Service
public class DescansoMedicoServiceImpl implements DescansoService {
	
	private final static String DIRECTORIO_UPLOAD = "data/descanso";
	//private final static String DIRECTORIO_UPLOAD = "E:\\vacaciones\\descanso";
	
	@Autowired
	private DescansoMedicoRepository descansomedicoRepository;

	@Override
	public Page<DescansoMedico> findAll(String filter,Pageable page) {
	
		return descansomedicoRepository.findAll("%"+filter+"%", page);
	}

	@Override
	public DescansoMedico findById(Integer id) {
		
		return descansomedicoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	public void eliminar(Integer id) {
		descansomedicoRepository.deleteById(id);
		
	}

	@Override
	public DescansoMedico save(DescansoMedico descanso) {
		descanso.setCreateBy("72524812");
		descanso.setCreateAt(new  Date());
		return descansomedicoRepository.save(descanso);
	}

	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {
		Path rutaArchivo = getPath(nombreFoto);
		Resource recurso = new UrlResource(rutaArchivo.toUri());
	
		
		return recurso;
	}

	@Override
	public OperationResponse copiar(MultipartFile archivo, Integer id) {
		OperationResponse response =null;
		Path rutaArchivo = getPath(archivo.getOriginalFilename());
		try {
			Files.copy(archivo.getInputStream(), rutaArchivo);
			DescansoMedico bo = descansomedicoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			if(bo.getArchivo()!=null) {
				if(getPath(bo.getArchivo()).toFile().exists()) {
					eliminar(bo.getArchivo());
				}
			}
			bo.setArchivo(archivo.getOriginalFilename());
			descansomedicoRepository.save(bo);
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
