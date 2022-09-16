package com.essalud.gcpp.service.descanso;




import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.entidades.descanso.DescansoMedico;

public interface DescansoService {

	Page<DescansoMedico> findAll(String filter,Pageable page);
	DescansoMedico findById(Integer id);
	void eliminar(Integer id);
	DescansoMedico save(DescansoMedico descanso);
	public Resource cargar(String nombreFoto) throws MalformedURLException;
	
	public OperationResponse copiar(MultipartFile archivo ,Integer id);
	
	public boolean eliminar(String nombreFoto);
	
	public Path getPath(String nombreFoto);
}
