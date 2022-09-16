package com.essalud.gcpp.controller.personal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.essalud.gcpp.entidades.Users;
import com.essalud.gcpp.entidades.personal.BoletaPago;
import com.essalud.gcpp.entidades.personal.Personal;
import com.essalud.gcpp.entidades.personal.Resolucion;
import com.essalud.gcpp.entidades.personal.Rol;
import com.essalud.gcpp.service.personal.IBoletaPagoService;
import com.essalud.gcpp.service.personal.IPersonalService;
import com.essalud.gcpp.service.personal.IResolucionService;
import com.essalud.gcpp.service.personal.IUploadServicePersonal;
import com.essalud.gcpp.service.personal.imp.CargarBoletaPago;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;


@RestController
@RequestMapping("/api/personal")
public class PersonalController {
	
	@Autowired
	private IPersonalService personalService;
	
	@Autowired
	private IUploadServicePersonal iuploadService;
	
	@Autowired
	private IResolucionService resolucionService;
	
	@Autowired
	private IBoletaPagoService boletaService;
	
	
	
	
	//ya esta
	@GetMapping("/listarpersonal/filtro")
	public Page<Personal> listarPersonalFiltro(
			@RequestParam Optional<String> filtro, 
			@RequestParam Optional<String> ceco,
			@RequestParam int page){
		Pageable pageable = PageRequest.of(page,15);
		
		return personalService.buscarporFiltro(filtro.orElse("").toLowerCase(), ceco.orElse(""),pageable);
	}
	//ya esta
	@GetMapping("/listarpersonal/filtro2")
	public List<Personal> listarPersonalFiltro2(
			@RequestParam Optional<String> filtro){
		
		return personalService.buscarporFiltro2(filtro.orElse("").toLowerCase());
	}
	
	@GetMapping("/ver")
	public Personal listarPersonal(Authentication auth){
		Users u = (Users)auth.getPrincipal();
		return personalService.buscarUsuario(u.getUsername());
		
	}

	//ya esta
	@GetMapping("/listaroles")
	public List<Rol> listarRoles(){
		return personalService.listarRoles();
	}
	
	//ya esta
	@SuppressWarnings("static-access")
	@GetMapping("/listarCumpleanos")
	public List<Personal> listarCumpleanos(){
		List<Personal> personal = new ArrayList<Personal>();
		ZoneId timeZone = ZoneId.of("GMT-0");
        LocalDate getLocalDate = new Date().toInstant().atZone(timeZone).toLocalDate();
				for(Personal per :personalService.listarCumpleanos())
				{
					
					@SuppressWarnings("deprecation")
					LocalDateTime nacimiento = LocalDateTime.of(getLocalDate.getYear(), per.getFechNaci().getMonth()+1, per.getFechNaci().getDate(),0,0);
					per.setFechNaci(new Date().from(nacimiento.atZone(timeZone).toInstant()));
					personal.add(per);
				}
				
				return personal;
	}
	
	//ya esta
	@GetMapping("/buscar")
	public Personal buscarUsuario(@RequestParam String dni){
		
		return personalService.buscarUsuario(dni);
		
	}
	//ya esta
	@PostMapping("/crear")
	@ResponseStatus(HttpStatus.CREATED)
	public Personal crear(@RequestBody Personal personal) {
		return personalService.crearPersonal(personal);
	}
	
	//ya esta
	@PutMapping("/personal")
	public ResponseEntity<?> update(@Validated @RequestBody Personal cliente, BindingResult result, @RequestParam String dni) {

		Personal clienteActual = personalService.buscarUsuario(dni);
		Personal clienteUpdated = null;
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
					.concat(dni.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			clienteActual.setApenombres(cliente.getApenombres());
			clienteActual.setCod00(cliente.getCod00());
			clienteActual.setCodigoPlanilla(cliente.getCodigoPlanilla());
			clienteActual.setCorreo(cliente.getCorreo());
			clienteActual.setProfesion(cliente.getProfesion());
			clienteActual.setNivel(cliente.getNivel());
			clienteActual.setCelular(cliente.getCelular());
			clienteActual.setAnexo(cliente.getAnexo());
			clienteActual.setConingreso(cliente.getConingreso());
			clienteActual.setNumeroPlaza(cliente.getNumeroPlaza());
			clienteActual.setEstadoPlaza(cliente.isEstadoPlaza());
			clienteActual.setFechNaci(cliente.getFechNaci());
			clienteActual.setFIngreso(cliente.getFIngreso());
			clienteActual.setCodcargo(cliente.getCodcargo());
			clienteActual.setCargo(cliente.getCargo());
			clienteActual.setCorreoPer(cliente.getCorreoPer());

			clienteUpdated = personalService.crearPersonal(clienteActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar en la base de datos");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El personal ha sido actualizado con exito!");
		response.put("cliente", clienteUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}
	//ya esta
	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("dni") String dni) {
		Map<String, Object> response = new HashMap<>();

		Personal personal = personalService.buscarUsuario(dni);
		String nombreFotoAnterior = personal.getDni();
		iuploadService.eliminar(nombreFotoAnterior+".jpg");
		if (!archivo.isEmpty()) {

			String nombreArchivo = null;
			try {
				nombreArchivo = iuploadService.copiar(archivo,dni);
				
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen del cliente");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

	


			response.put("personal", personal);
			response.put("mensaje", "Has subido correctamente la imagen: " + nombreArchivo);
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	//ya esta
	@GetMapping("/uploads/img")
	public ResponseEntity<Resource> verFoto(@RequestParam String nombreFoto,HttpServletRequest request) {
		
		Resource recurso = null;
		 String contentType = null;
		try {
		
			recurso = iuploadService.cargar(nombreFoto);
			contentType = request.getServletContext().getMimeType(recurso.getFile().getAbsolutePath());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@DeleteMapping("/eliminar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@RequestParam String dni) {
		personalService.deleteById(dni);
	}
	
	//ya esta
	@GetMapping("/listarResoluciones")
	public List<Resolucion> listarResoluciones(@RequestParam String dni){
		return resolucionService.getAllMine(dni);
	}
	//ya esta
	@PostMapping("/update/resolucion")
	public ResponseEntity<?> uploadResolucionUpdate(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		
		Resolucion resolucion = resolucionService.findbyResolucion(id);
		String nombreFotoAnterior = resolucion.getNombreArchivo();
		resolucionService.eliminar(nombreFotoAnterior);
		if (!archivo.isEmpty()) {

			String nombreArchivo = null;
			try {
				nombreArchivo = resolucionService.copiar(archivo);
				resolucion.setNombreArchivo(archivo.getResource().getFilename());
				resolucionService.agregarResolucion(resolucion);
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
				response.put("mensaje", "Error al subir la imagen del cliente");
				//response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

	


			response.put("personal", resolucion);
			response.put("mensaje", "Has subido correctamente la imagen: " + nombreArchivo);
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	//ya esta
	@PostMapping("/upload/resolucion")
	public ResponseEntity<?> uploadResolucion(@RequestParam("archivo") MultipartFile archivo) {
		Map<String, Object> response = new HashMap<>();
		
		resolucionService.eliminar(archivo.getOriginalFilename());
		if (!archivo.isEmpty()) {

			String nombreArchivo = null;
			try {
				nombreArchivo = resolucionService.copiar(archivo);
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
				response.put("mensaje", "Error al subir la imagen del cliente");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			response.put("mensaje", nombreArchivo);
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	//ya esta
	@PostMapping("/crear/resolucion")
	@ResponseStatus(HttpStatus.CREATED)
	public Resolucion crear(@RequestBody Resolucion resolucion) {
		return resolucionService.agregarResolucion(resolucion);
	}
	//ya esta
	@GetMapping("/get/resolucion")
	public Resolucion obtenerResolucion(@RequestParam Long id) {
		return resolucionService.findbyResolucion(id);
	}
	//ya esta
	@GetMapping("/download/resolucion")
	public ResponseEntity<Resource> descargarResolucion(@RequestParam String nombreResolucion,HttpServletRequest request) {
		
		Resource recurso = null;
		 String contentType = null;
		try {
		
			recurso = resolucionService.cargar(nombreResolucion);
			contentType = request.getServletContext().getMimeType(recurso.getFile().getAbsolutePath());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 if (contentType == null) {
	            contentType = "application/pdf";
	        }
			
		return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
	            .body(recurso);
	}
	
	@GetMapping("/reporte/personal/pdf")
	public ResponseEntity<byte[]> descargarReporteGerenciaJasper(HttpServletRequest request) {
		
		JasperPrint recurso = null;
		byte[] recurso2 = null;
	
		recurso = personalService.reportePersonal();

		try {
			recurso2 = JasperExportManager.exportReportToPdf(recurso);
		} catch (JRException e) {
			e.printStackTrace();
		}
		return ResponseEntity
			      .ok()
			      // Specify content type as PDF
			      .header("Content-Type", "application/pdf; charset=UTF-8")
			      // Tell browser to display PDF if it can
			      .header("Content-Disposition", "inline; filename=\"" + recurso2.toString() + ".pdf\"")
			      .body(recurso2);
	}
	
	@GetMapping("/reporte/personal/excel")
	public void descargarReporteGerenciaExcel(HttpServletResponse  request){
		try {
			JasperPrint recurso = null;
			recurso = personalService.reportePersonal();
			JRXlsxExporter  exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
			reportConfigXLS.setSheetNames(new String[] { "reporte" });
			exporter.setConfiguration(reportConfigXLS);
			exporter.setExporterInput(new SimpleExporterInput(recurso));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(request.getOutputStream()));
			request.setHeader("Content-Disposition", "attachment;filename=jasperReport.xlsx");
			request.setContentType("application/octet-stream");
			exporter.exportReport();
		} catch (IOException  | JRException e) {
			e.printStackTrace();
		}		
	}
	
	
	
	@PostMapping("/cargar/boletapago")
	public boolean cargarBoletaPago(@RequestPart("archivo") MultipartFile archivo) {
		return personalService.cargarArchivoBoletaPago(archivo);
	}
	
	@GetMapping("/listaBoletaPago")
	public List<BoletaPago> listarBoletasPago(@RequestParam String anno , Authentication auth){
		Users u = (Users) auth.getPrincipal();
		return boletaService.listarBoletaPagoAnno(anno, u);
	}
	
	@GetMapping("/descargar/boletaPago")
	public ResponseEntity<Resource> descargarBoletaPago(@RequestParam String anno,@RequestParam String mes,@RequestParam String archivo, HttpServletRequest request) {
		CargarBoletaPago cargaBoletaPago = new CargarBoletaPago();
		Resource recurso = null;
		 String contentType = null;
		try {
		
			recurso = cargaBoletaPago.cargar(anno,mes,archivo);
			contentType = request.getServletContext().getMimeType(recurso.getFile().getAbsolutePath());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if (contentType == null) {
	            contentType = "application/pdf";
	        }
			
		return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
	            .body(recurso);
	}
	
	
	@GetMapping("/download/manual")
	public ResponseEntity<Resource> descargarManual(HttpServletRequest request) {
		
		Resource recurso = null;
		 String contentType = null;
		try {
		
			recurso = personalService.descargarManual();
			contentType = request.getServletContext().getMimeType(recurso.getFile().getAbsolutePath());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if (contentType == null) {
	            contentType = "application/pdf";
	        }
			
		return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
	            .body(recurso);
	}

	
}
