package com.essalud.gcpp.controller.vacaciones;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.essalud.gcpp.entidades.vacaciones.Vacaciones;
import com.essalud.gcpp.entidades.vacaciones.VacacionesDias;
import com.essalud.gcpp.service.vacaciones.IUploadServiceVacaciones;
import com.essalud.gcpp.service.vacaciones.IVacacionesService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("/api/vacaciones")
public class VacacionesController {
	
	@Autowired
	private IVacacionesService vacacionesService;
	
	@Autowired
	private IUploadServiceVacaciones iuploadService;
	
	
	//Ya esta
	@GetMapping("/listarvacaciones/{dni}/{page}")
	public Page<Vacaciones> listarVacacionesPendientes(@PathVariable String dni,@PathVariable int page) {
		Pageable pageable = PageRequest.of(page,15);
		return vacacionesService.buscarVacacionesUsuario(dni, pageable);
	}
	//Ya esta
	@GetMapping("/vacacion")
	public Vacaciones buscarVacacion(@RequestParam Long id) {
		return vacacionesService.buscarVacaciones(id);
	}
	//Ya esta
	@GetMapping("/listarvacacionesPasadas/{dni}/{page}")
	public Page<Vacaciones> listarVacaciones(@PathVariable String dni,@PathVariable int page) {
		Pageable pageable = PageRequest.of(page,15);
		return vacacionesService.buscarVacacionesPasadas(dni, pageable);
	}
	//Ya esta
	@GetMapping("/listarVacacionesPeriodo/{dni}")
	public List<VacacionesDias> listarVacacionesPeriodoDisponible(@PathVariable String dni) {
		return vacacionesService.listarVacacionesPeriodoDisponible(dni);
	}
	//Ya esta
	@RequestMapping(value="/reprogramarVacaciones/{cod}/{identificador}",produces =MediaType.APPLICATION_JSON_VALUE, method =RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, String> reprogramarVacaciones(@PathVariable Long cod,@PathVariable String identificador) {
		return Collections.singletonMap("response", vacacionesService.reprogramarVacaciones(cod,identificador));
	}
	//Ya esta
	@RequestMapping(value="/denegarVacaciones/{cod}/{identificador}",produces =MediaType.APPLICATION_JSON_VALUE, method =RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, String> denegarVacaciones(@PathVariable Long cod,@PathVariable String identificador) {
		return Collections.singletonMap("response", vacacionesService.denegarVacaciones(cod,identificador));
	}
	//Ya esta
	@RequestMapping(value="/programarVacaciones",produces =MediaType.APPLICATION_JSON_VALUE, method =RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, String> programarVacaciones(@RequestBody Vacaciones vacaciones ) {
		return Collections.singletonMap("response", vacacionesService.programarVacaciones(vacaciones));
	}
	//Ya esta
	@RequestMapping(value="/programarVacacionesAdmin",produces =MediaType.APPLICATION_JSON_VALUE, method =RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, String> programarVacacionesAdmin(@RequestBody Vacaciones vacaciones ) {
		return Collections.singletonMap("response", vacacionesService.programarVacacionesAdmin(vacaciones));
	}
	//Ya esta
	@GetMapping("/listarvacacionesRevision/page/{page}")
	public Page<Vacaciones> listarVacacionesRevision(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page,8);
		return vacacionesService.listarVacacionesRevision(pageable);
	}
	//Ya esta
	@GetMapping("/revisionSubgerente/{ceco}/{page}")
	public Page<Vacaciones> listarVacacionesRevisionSubgerente(@PathVariable String ceco,@PathVariable int page) {
		Pageable pageable = PageRequest.of(page,8);
		return vacacionesService.listarVacacionesSubgerente(ceco, pageable);
	}
	//Ya esta
	@GetMapping("/revisionGerente/{ceco}/{page}")
	public Page<Vacaciones> listarVacacionesRevisionGerente(@PathVariable String ceco,@PathVariable int page) {
		Pageable pageable = PageRequest.of(page,8);
		return vacacionesService.listarVacacionesGerente(ceco, pageable);
	}
	//Ya esta
	@GetMapping("/historial/{dni}/{page}/{anno}")
	public Page<Vacaciones> listarHistorialVacaciones(@PathVariable String dni,@PathVariable String anno,@PathVariable int page) {
		Pageable pageable = PageRequest.of(page,8);
		if(anno.equalsIgnoreCase("Todo")) {
			return vacacionesService.historialVacacionesTodo(dni, pageable);
		}else {
			return vacacionesService.historialVacaciones(dni, anno,pageable);
		}
		
	}
	//Ya esta
	@PutMapping("/actualizarVacaciones/{id}")
	public ResponseEntity<?> update(@Validated @RequestBody Vacaciones vaca, BindingResult result, @PathVariable Long id) {
		Vacaciones clienteActual = vacacionesService.buscarVacaciones(id);
		Vacaciones clienteUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> err.getField() + ": " + err.getDefaultMessage()).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (clienteActual == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			switch(vaca.getEstado()) {
				case "Pendiente de Aprobacion": vaca.setEstado("0"); break;
				case "Pendiente de Ejecutar":  vaca.setEstado("1"); break;
				case "Denegado": vaca.setEstado("2"); break;
				case "Reprogramado": vaca.setEstado("3"); break;
				case "Ejecutado": vaca.setEstado("4"); break;
				}
			
			clienteUpdated = vacacionesService.guardarVacaciones(vaca);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el update en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Vacaciones ha sido actualizado con éxito");
		response.put("cliente", clienteUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	//Ya esta
	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<>();

		Vacaciones boleta = vacacionesService.buscarVacaciones(id);
		if (!archivo.isEmpty()) {

			String nombreArchivo = null;
			try {
				nombreArchivo = iuploadService.copiar(archivo);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				response.put("mensaje", "Error al subir la imagen del cliente");
				//response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			String nombreFotoAnterior = boleta.getCarta();

			iuploadService.eliminar(nombreFotoAnterior);

			boleta.setCarta(nombreArchivo);

			vacacionesService.guardarVacaciones(boleta);

			response.put("boleta", boleta);
			response.put("mensaje", "Has subido correctamente la imagen: " + nombreArchivo);
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	//Ya esta
	@GetMapping("/uploads/vacaciones/{nombreArchivo:.+}")
	public ResponseEntity<Resource> descargarCarta(@PathVariable String nombreArchivo,HttpServletRequest request) {
		
		Resource recurso = null;
		 String contentType = null;
		try {
		
			recurso = iuploadService.cargar(nombreArchivo);
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
	
	@DeleteMapping("/eliminarPeriodoVacaciones/{id}")
	public void eliminarPeriodoVacaciones(@PathVariable Long id) {
	
		vacacionesService.eliminarVacacionePeriodo(id);
	}
	//Ya esta
	@DeleteMapping("/eliminarVacaciones/{id}")
	public void eliminarVacaciones(@PathVariable Long id) {
	
		vacacionesService.eliminarHistorialVacaciones(id);
	}
	
	@PutMapping("/actualizarVacacionesPeriodo/{id}")
	public ResponseEntity<?> updateVacacionesPeriodo(@Validated @RequestBody VacacionesDias vaca, BindingResult result, @PathVariable Long id) {
		VacacionesDias clienteActual = vacacionesService.buscarVacacionesPeriodo(id);
		VacacionesDias clienteUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> err.getField() + ": " + err.getDefaultMessage()).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (clienteActual == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			
			clienteUpdated = vacacionesService.actualizarDias(vaca);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el update en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Vacaciones ha sido actualizado con éxito");
		response.put("cliente", clienteUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	//Ya esta
	@GetMapping("/listarvacacionesSimilares/{area}/{fecha}")
	public List<Vacaciones> listarVacacionesSimilares(@PathVariable String area,@PathVariable Date fecha) {
	
		return vacacionesService.listarVacacionesCoincidencia(area, fecha);
	}
	//Ya esta
	@GetMapping(value="/programacionPersonalAnual")
	public List<VacacionesDias> programacionAnualPersonal(@RequestParam String anno ) {
		return vacacionesService.programacionAnualPersonal(anno);
	}
	
	
	
	//ya esta
	@GetMapping("/listarvacaReporte")
	public Page<Vacaciones> listarVacacionesReporte(@RequestParam String area,@RequestParam Date inicio,@RequestParam Date fin,@RequestParam int page) {
		Pageable pageable = PageRequest.of(page,10);
		return vacacionesService.listarVacacionesReporteGerencia(area,  inicio, fin, pageable);
	}
	//ya esta
	@GetMapping("/descargar/reporteVacacionesGerencia")
	public ResponseEntity<byte[]> descargarBoletaJasper(@RequestParam String area,@RequestParam Date inicio,@RequestParam Date fin,HttpServletRequest request) throws SQLException, JRException {
		
		JasperPrint recurso = null;
		byte[] recurso2 = null;
	
		recurso = vacacionesService.cargar(area,inicio,fin);

		recurso2 = JasperExportManager.exportReportToPdf(recurso);
		return ResponseEntity
			      .ok()
			      // Specify content type as PDF
			      .header("Content-Type", "application/pdf; charset=UTF-8")
			      // Tell browser to display PDF if it can
			      .header("Content-Disposition", "inline; filename=\"" + recurso2.toString() + ".pdf\"")
			      .body(recurso2);
	}
	//ya esta
	@GetMapping("/descargar/reporteVacacionesGerencia/excel")
	public void descargarVacacionesGerenciaExcel(@RequestParam String area,@RequestParam Date inicio,@RequestParam Date fin,HttpServletResponse  request) {
		try {
			JasperPrint recurso = null;
			recurso = vacacionesService.cargar(area,inicio,fin);
			JRXlsxExporter  exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
			reportConfigXLS.setSheetNames(new String[] { "reporte" });
			exporter.setConfiguration(reportConfigXLS);
			exporter.setExporterInput(new SimpleExporterInput(recurso));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(request.getOutputStream()));
			request.setHeader("Content-Disposition", "attachment;filename=jasperReport.xlsx");
			request.setContentType("application/octet-stream");
			exporter.exportReport();
		} catch (IOException | SQLException | JRException e) {
			e.printStackTrace();
		}		
	}
	//ya esta
	@GetMapping("/descargar/reporteVacacionesUsuario")
	public ResponseEntity<byte[]> descargarVacacionesPersonal(@RequestParam String dni,HttpServletRequest request) throws SQLException, JRException {
		
		JasperPrint recurso = null;
		byte[] recurso2 = null;
	
		recurso = vacacionesService.reporteVacacionesPersonal(dni);

		recurso2 = JasperExportManager.exportReportToPdf(recurso);
		return ResponseEntity
			      .ok()
			      // Specify content type as PDF
			      .header("Content-Type", "application/pdf; charset=UTF-8")
			      // Tell browser to display PDF if it can
			      .header("Content-Disposition", "inline; filename=\"" + recurso2.toString() + ".pdf\"")
			      .body(recurso2);
	}
	
	//ya esta
	@GetMapping("/descargar/reporteVacacionesUsuario/excel")
	public void descargarVacacionesGerenciaExcel(@RequestParam String dni,HttpServletResponse  request) {
		try {
			JasperPrint recurso = null;
			recurso = vacacionesService.reporteVacacionesPersonal(dni);
			JRXlsxExporter  exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
			reportConfigXLS.setSheetNames(new String[] { "reporte" });
			exporter.setConfiguration(reportConfigXLS);
			exporter.setExporterInput(new SimpleExporterInput(recurso));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(request.getOutputStream()));
			request.setHeader("Content-Disposition", "attachment;filename=jasperReport.xlsx");
			request.setContentType("application/octet-stream");
			exporter.exportReport();
		} catch (IOException | SQLException | JRException e) {
			e.printStackTrace();
		}		
	}
	

}
