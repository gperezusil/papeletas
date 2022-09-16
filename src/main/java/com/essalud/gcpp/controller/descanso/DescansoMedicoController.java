package com.essalud.gcpp.controller.descanso;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.entidades.descanso.DescansoMedico;
import com.essalud.gcpp.service.descanso.DescansoService;

@RestController
@RequestMapping("/api/descanso")
public class DescansoMedicoController {

	@Autowired
	private DescansoService descansoService;

	@GetMapping("/all")
	public Page<DescansoMedico> listarDescansoMedico(@RequestParam Optional<String> filtro,
			@RequestParam Integer page) {
		Pageable pageable = PageRequest.of(page, 15);
		return descansoService.findAll(filtro.orElse(""), pageable);
	}

	@PostMapping("/save")
	public DescansoMedico addDescanso(@RequestBody DescansoMedico descanso) {

		return descansoService.save(descanso);
	}

	@DeleteMapping("/delete")
	public void delete(@RequestParam Integer id) {

		descansoService.eliminar(id);
	}

	@GetMapping("/findbyid")
	public DescansoMedico findById(@RequestParam Integer id) {
		return descansoService.findById(id);
	}

	@PostMapping("/upload")
	public OperationResponse upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Integer id) {

		return descansoService.copiar(archivo, id);
	}

	@GetMapping("/download")
	public ResponseEntity<Resource> descargarBoleta(@RequestParam String nombreBoleta, HttpServletRequest request) {

		Resource recurso = null;
		String contentType = null;
		try {

			recurso = descansoService.cargar(nombreBoleta);
			contentType = request.getServletContext().getMimeType(recurso.getFile().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (contentType == null) {
			contentType = "image/jpeg";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
}
