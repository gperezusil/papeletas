package com.essalud.gcpp.controller.personal;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.essalud.gcpp.entidades.personal.Locador;
import com.essalud.gcpp.entidades.personal.OrdenesLocador;
import com.essalud.gcpp.service.personal.ILocadorService;
import com.essalud.gcpp.service.personal.IUploadServicePersonal;


@RestController
@RequestMapping("/api/locador")
public class LocadorController {

	@Autowired
	private ILocadorService locadorService;
	
	
	
	@Autowired
	private IUploadServicePersonal iuploadService;
	
	@GetMapping("/listarLocadores/{ceco}/{page}")
	public Page<Locador> listarLocadores(@PathVariable String ceco,@PathVariable int page){
		Pageable pageable = PageRequest.of(page,10);
		return locadorService.listarLocadores(ceco, pageable);
	}
	
	@GetMapping("/listarOrdenes/{ruc}/{page}")
	public Page<OrdenesLocador> listarOrdenesLocadores(@PathVariable String ruc,@PathVariable int page){
		Pageable pageable = PageRequest.of(page,10);
		return locadorService.listarOrdenesLocadores(ruc, pageable);
	}
	
	@PutMapping("/locador/{ruc}")
	public ResponseEntity<?> update(@Validated @RequestBody Locador cliente, BindingResult result, @PathVariable String ruc) {

		Locador clienteActual = locadorService.buscar(ruc);
		Locador clienteUpdated = null;
		Map<String, Object> response = new HashMap<String, Object>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (clienteActual == null) {
			response.put("mensaje", "Error: al actualizar, el cliente ID: "
					.concat(ruc.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			clienteActual.setRazonSocial(cliente.getRazonSocial());
			clienteActual.setCeco(cliente.getCeco());
			clienteActual.setProfesion(cliente.getProfesion());
			clienteActual.setCelular(cliente.getCelular());
			clienteActual.setCorreo(cliente.getCorreo());

			clienteUpdated = locadorService.save(clienteActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar en la base de datos");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El locador ha sido actualizado con exito!");
		response.put("cliente", clienteUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}
	@PostMapping("/locador/save")
	public OrdenesLocador grabarOrden(@RequestBody OrdenesLocador orden) {
		return locadorService.saveOrden(orden);
	}
	
	@PostMapping("/locador/crear")
	public Locador grabarLocador(@RequestBody Locador locador) {
		return locadorService.save(locador);
	}
	
	@PostMapping("/upload/locador")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("ruc") String ruc) {
		Map<String, Object> response = new HashMap<>();
		Locador locador = locadorService.buscar(ruc);
		if (!archivo.isEmpty()) {

			String nombreArchivo = null;
			try {
				locador.setFoto(archivo.getResource().getFilename());
				locadorService.save(locador);
				nombreArchivo = iuploadService.copiarLocador(archivo);
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
				response.put("mensaje", "Error al subir el archivo del locador");
				//response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			response.put("orden", locador);
			response.put("mensaje", "Has subido correctamente la orden: " + nombreArchivo);
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/uploads/locador/{nombreArchivo:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreArchivo,HttpServletRequest request) {
		
		Resource recurso = null;
		 String contentType = null;
		try {
		
			recurso = iuploadService.cargarLocador(nombreArchivo);
			contentType = request.getServletContext().getMimeType(recurso.getFile().getAbsolutePath());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if (contentType == null) {
	            contentType = "image/jpeg";
	        }
			
		return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
	            .body(recurso);
	}
}
