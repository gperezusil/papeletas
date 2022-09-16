package com.essalud.gcpp.service.papeletas;


import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.essalud.gcpp.dto.OperationResponse;

public interface IUploadService {
	
	public Resource cargar(String nombreFoto) throws MalformedURLException;
	
	public OperationResponse copiar(MultipartFile archivo ,Long id);
	
	public boolean eliminar(String nombreFoto);
	
	public Path getPath(String nombreFoto);
}
