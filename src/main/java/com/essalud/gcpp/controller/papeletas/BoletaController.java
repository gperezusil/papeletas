package com.essalud.gcpp.controller.papeletas;

import java.io.IOException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;

import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.dto.papeletas.DashPapeletaDTO;
import com.essalud.gcpp.dto.papeletas.PadreDashDTO;
import com.essalud.gcpp.entidades.papeletas.Boleta;
import com.essalud.gcpp.entidades.papeletas.Restriccion;
import com.essalud.gcpp.service.papeletas.IBoletaService;
import com.essalud.gcpp.service.papeletas.IUploadService;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("/api/boletas")
public class BoletaController {
	
	
	@Autowired
	private IBoletaService boletaService;
	@Autowired
	private IUploadService iuploadService;
	
	//ya esta	
	@GetMapping("/obtenerboleta")
	public Boleta listarBoletaId(@RequestParam Long id){
		return boletaService.findbyId(id);
	}
	//ya esta	
	@GetMapping("/listarboletas/pendientes/page")
	public Page<Boleta> listarBoletasRevision(@RequestParam Integer page) {
		Pageable pageable = PageRequest.of(page,15);
		return boletaService.findByEstado(pageable);
	}
	
	//ya esta
	@GetMapping("/listarboletas/boletas")
	public Page<Boleta> listarBoletasPorMesAnno(
			@RequestParam Integer page,
			@RequestParam String dni,
			@RequestParam String mes,
			@RequestParam String anno) {
		Pageable pageable = PageRequest.of(page,8,Sort.by("fechInicio").descending());
		
		return boletaService.listarBoletasUsuarioPorMes(pageable,dni,mes,anno);
	}
	
	
	//ya esta
	@GetMapping("/listarboletas/sumaHoras")
	public List<Boleta> listarBoletasPorMesyAnno(
			@RequestParam String dni,
			@RequestParam String mes,
			@RequestParam String anno) {
		return boletaService.listarBoletasporMesyAnno(dni, mes, anno);
	}
	
	//ya esta
	@PostMapping(value="/crear",produces =MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> crear(@RequestBody Boleta boleta) {

		return Collections.singletonMap("response", boletaService.grabar(boleta));
	}  
	

	//ya esta
	@DeleteMapping("/eliminar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@RequestParam Long id) {
		boletaService.deleteById(id);
	}
	//ya esta
	@PostMapping("/upload")
	public OperationResponse upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {

		return iuploadService.copiar(archivo, id);
	}
	
	//ya esta
	@GetMapping("/download/boletapdf/{nombreBoleta:.+}")
	public ResponseEntity<Resource> descargarBoleta(@PathVariable String nombreBoleta,HttpServletRequest request) {
		
		Resource recurso = null;
		 String contentType = null;
		try {
		
			recurso = iuploadService.cargar(nombreBoleta);
			contentType = request.getServletContext().getMimeType(recurso.getFile().getAbsolutePath());
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
	
	
	//ya esta
	@GetMapping("/download/boletajasper/{id:.+}")
	public ResponseEntity<byte[]> descargarBoletaJasper(@PathVariable Long id,HttpServletRequest request) throws SQLException, JRException {
		
		JasperPrint recurso = null;
		byte[] recurso2 = null;
	
		recurso = boletaService.cargar(id);

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
	@GetMapping("/setting/boleta")
	public List<Restriccion> listarRestriccionesBoleta() {
		return boletaService.listarRestriccionesBoleta();

	}
	//ya esta
	@GetMapping("/setting/vacaciones")
	public List<Restriccion> listarRestriccionesVacacaciones() {
		return boletaService.listarRestriccionesVacaciones();

	}
	//ya esta
	@PostMapping("actualizar/restricciones")
	public OperationResponse actualizarRestricciones(@RequestBody List<Restriccion> restricciones) {
		
		return boletaService.actualizarRestricciones(restricciones);
		
	}
	
	
	//ya esta
	@GetMapping("/listarboletas/reporte")
	public Page<Boleta> listarBoletasReporte(
			@RequestParam String area,
			@RequestParam Date inicio , 
			@RequestParam Date fin,
			@RequestParam Integer page) {
		Pageable pageable = PageRequest.of(page,15);
		return boletaService.listarBoletasReporte(area, inicio, fin, pageable);
	}
	
	//ya esta	
	@GetMapping("/reporte/boleta")
	public ResponseEntity<byte[]> descargarReporteGerenciaJasper(
			@RequestParam String area,
			@RequestParam Date inicio , 
			@RequestParam Date fin,
			HttpServletRequest request) throws SQLException, JRException {
		
		JasperPrint recurso = null;
		byte[] recurso2 = null;
	
		recurso = boletaService.reportePapeletaGerencia(area, inicio, fin);

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
	@GetMapping("/reporte/boleta/excel")
	public void descargarReporteGerenciaExcel(
			@RequestParam String area,
			@RequestParam Date inicio , 
			@RequestParam Date fin,
			HttpServletResponse  request){
		try {
			JasperPrint recurso = null;
			recurso = boletaService.reportePapeletaGerencia(area, inicio, fin);
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
	
	//Ya esta
	@GetMapping("/listarTotal/dash")
	public PadreDashDTO listarDashPapeletaTotal(@RequestParam String mes,@RequestParam String anno) {
		String variable =mes+"/"+anno;
		return boletaService.listarDashPapeletaTotal(variable);

	}
	
}