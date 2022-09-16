package com.essalud.gcpp.controller.inventario;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.essalud.gcpp.dto.inventario.SolicitudInventarioDTO;
import com.essalud.gcpp.entidades.Users;
import com.essalud.gcpp.entidades.inventario.DetalleOrdenProveedor;
import com.essalud.gcpp.entidades.inventario.Inventario;
import com.essalud.gcpp.entidades.inventario.OrdenProveedor;
import com.essalud.gcpp.entidades.inventario.Solicitud;
import com.essalud.gcpp.service.inventario.IDetalleOrdenService;
import com.essalud.gcpp.service.inventario.IOrdenProveedorService;
import com.essalud.gcpp.service.inventario.IinventarioService;
import com.essalud.gcpp.service.inventario.IinventarioSolicitudService;
import com.essalud.gcpp.service.inventario.impl.UploadServiceInventarioImpl;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;






@RestController
@RequestMapping("/api/inventario")
public class InventarioController {
	
	@Autowired private IinventarioService inventarioService;
	
	@Autowired private IinventarioSolicitudService inventarioSolicitudService;
	
	@Autowired private IOrdenProveedorService ordenProveedorService;
	
	@Autowired private IDetalleOrdenService detalleOrdenService;
	
	@Autowired private UploadServiceInventarioImpl uploadInventario;
	
	@GetMapping("/listarInventario")
	public Page<Inventario> listarInventario(@RequestParam Optional<String> filtro,@RequestParam Integer page){
		Pageable pageable = PageRequest.of(page,15,Sort.by("id").ascending());
		return inventarioService.findAll(filtro.orElse(""),pageable);
	}
	

	
	@PostMapping("/inventario/save")
	public Inventario grabarCapacitacion(@RequestBody Inventario inventario) {
		return inventarioService.save(inventario);
	}
	
	@DeleteMapping("/inventario/delete/{id}")
	public void eliminarProducto(@PathVariable Integer id) {
		inventarioService.deleteById(id);
	}
	
	@GetMapping("/inventario/solicitud")
	public Page<Object[]> listarSolicitudInventario(
			@RequestParam Optional<String> filtro,
			@RequestParam Integer page){
		Pageable pageable = PageRequest.of(page,10);
		return inventarioSolicitudService.listarSolicitudesPendientes(filtro.orElse(""),pageable);
	}
	@GetMapping("/inventario/solicitud/dni")
	public Page<Object[]> listarSolicitudInventarioPersonal(
			@RequestParam Optional<String> filtro,
			@RequestParam Integer page,
			Authentication auth){
		Pageable pageable = PageRequest.of(page,10);
		Users u = (Users)auth.getPrincipal();
		return inventarioSolicitudService.listarSolicitudesPendientesPersonal(filtro.orElse(""),u,pageable);
	}
	@GetMapping("/inventario/solicitud/historial")
	public Page<Object[]> listarSolicitudInventarioHistorial(
			@RequestParam Optional<String> filtro,
			@RequestParam Integer page){
		Pageable pageable = PageRequest.of(page,10);
		
		return inventarioSolicitudService.listarSolicitudesPendientesHistorial(filtro.orElse(""),pageable);
	}
	@GetMapping("/inventario/solicitud/personal")
	public Page<Solicitud> listarSolicitudPersonalFecha(
			@RequestParam String dni,
			@RequestParam String fecha,
			@RequestParam Integer page){
		Pageable pageable = PageRequest.of(page,5);
		return inventarioSolicitudService.listarSolicitudPersonalFecha(dni, fecha, pageable);
	}
	
	
	@PostMapping("/inventario/solicitud/save")
	public Solicitud grabarSolicitudInventario(@RequestBody SolicitudInventarioDTO inventario,Authentication auth) {
		Users u = (Users)auth.getPrincipal();
		return inventarioSolicitudService.save(inventario,u);
	}
	
	@PostMapping("/inventario/solicitud/actualizar")
	public Solicitud actualizarSolicitud(@RequestBody Solicitud inventario) {
		return inventarioSolicitudService.updateStatus(inventario);
	}
	
	@GetMapping("/inventario/historial/{page}")
	public Page<Solicitud> listarSolicitudHistorial(@PathVariable Integer page){
		Pageable pageable = PageRequest.of(page,10,Sort.by("fecha").descending());
		return inventarioSolicitudService.listarSolicitudesHistorial(pageable);
	}
	
	@GetMapping("/inventario/misSolicitudes/{page}")
	public Page<Solicitud> listarMisSolicitudes(@PathVariable Integer page,
			Authentication auth){
		Users u = (Users) auth.getPrincipal();
		Pageable pageable = PageRequest.of(page,10,Sort.by("fecha").descending());
		return inventarioSolicitudService.listarmisSolicitudes(pageable, u);
	}
	

	
	@PostMapping("/ordenProveedor/save")
	public OrdenProveedor grabarOrdenProveedor(@RequestBody OrdenProveedor inventario) {
		return ordenProveedorService.save(inventario);
	}
	
	
	@GetMapping("/ordenProveedor/producto")
	public Page<OrdenProveedor> listarOrdenes(@RequestParam Optional<String> filtro,@RequestParam Integer page){
		Pageable pageable = PageRequest.of(page,15,Sort.by("fecha").descending());
		return ordenProveedorService.listarOrdenProducto(filtro.orElse(""),pageable);
	}
	
	@PostMapping("/ordenProveedor/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo,@RequestParam("variable") String variable, @RequestParam("id") Integer id) {
		Map<String, Object> response = new HashMap<>();

		OrdenProveedor orden = ordenProveedorService.findById(id);
		if (!archivo.isEmpty()) {

			String nombreArchivo = null;
			try {
				nombreArchivo = uploadInventario.copiar(archivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen del cliente");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if(variable.equalsIgnoreCase("factura")) {
			orden.setArchivoFactura(nombreArchivo);
			}
			if(variable.equalsIgnoreCase("guia")) {
				orden.setArchivoGuia(nombreArchivo);
			}
			ordenProveedorService.actualizar(orden);

			response.put("orden", orden);
			response.put("mensaje", "Has subido correctamente la imagen: " + nombreArchivo);
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/download/inventario/orden")
	public ResponseEntity<Resource> descargarOrdenProveedor(@RequestParam String archivo,HttpServletRequest request) {
		
		Resource recurso = null;
		 String contentType = null;
		try {
		
			recurso = uploadInventario.cargar(archivo);
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
	
	@GetMapping("/descargar/inventario")
	public void descargarBoletaJasper(HttpServletResponse  request) {
		try {
			JasperPrint recurso = null;
			recurso = inventarioService.cargar();
			JRXlsxExporter  exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
			reportConfigXLS.setSheetNames(new String[] { "inventario" });
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
	
	@GetMapping("/listarDetalleOrden")
	public Page<DetalleOrdenProveedor> listarInventario(@RequestParam Integer id,@RequestParam Integer page){
		Pageable pageable = PageRequest.of(page,5,Sort.by("id").ascending());
		return detalleOrdenService.listarDetalleOrden(id, pageable);
	}
	
	@GetMapping("/searchProducto")
	public List<Inventario> searchProducto(@RequestParam String filter){
		return inventarioService.searchProducto(filter);
	}
	
	@PostMapping("/detalleOrden/save")
	public DetalleOrdenProveedor grabarOrdenProveedor(@RequestBody DetalleOrdenProveedor inventario) {
		return detalleOrdenService.save(inventario);
	}

}
