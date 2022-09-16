package com.essalud.gcpp.controller.papeletas;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.dto.capacitacion.CapacitacionDTO;
import com.essalud.gcpp.entidades.capacitacion.Capacitacion;
import com.essalud.gcpp.service.capacitacion.ICapacitacionService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("/api/capacitacion")
public class CapacitacionController {
	
	@Autowired
	private ICapacitacionService capacitacionService;
	
	//ya esta
	@PostMapping("/capacitacion/save")
	public OperationResponse grabarCapacitacion(@RequestBody CapacitacionDTO capa) {
		return capacitacionService.save(capa);
	}
	//ya esta
	@GetMapping("/listarCapacitaciones")
	public Page<Object[]> listarCapacitaciones(@RequestParam Integer page){
		Pageable pageable = PageRequest.of(page,15);
		return capacitacionService.listarCapacitaciones(pageable);
	}
	//ya esta
	@GetMapping("/listarCapacitacionesUsuario")
	public Page<Capacitacion> listarCapacitacionesUsuario(@RequestParam String dni,@RequestParam Integer page){
		Pageable pageable = PageRequest.of(page,15);
		return capacitacionService.listarCapacitacionPorUsuario(dni, pageable);
	}
	
	//ya esta
	@GetMapping("/reporte/capacitacion")
	public ResponseEntity<byte[]> descargarCapacitacionJasper(
			@RequestParam String entidad,
			@RequestParam String curso,
			HttpServletRequest request) throws SQLException, JRException {
		
		JasperPrint recurso = null;
		byte[] recurso2 = null;
	
		recurso = capacitacionService.reporteCapacitacion(entidad, curso);

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
	@GetMapping("/reporte/capacitacion/excel")
	public void descargarCapacitacionExcel(
			@RequestParam String entidad,
			@RequestParam String curso,
			HttpServletResponse  request){
		try {
			JasperPrint recurso = null;
			recurso = capacitacionService.reporteCapacitacion(entidad, curso);
			JRXlsxExporter  exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
			reportConfigXLS.setSheetNames(new String[] { "capacitacion" });
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
