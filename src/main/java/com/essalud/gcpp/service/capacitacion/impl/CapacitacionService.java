package com.essalud.gcpp.service.capacitacion.impl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.dto.capacitacion.CapacitacionDTO;
import com.essalud.gcpp.entidades.capacitacion.Capacitacion;
import com.essalud.gcpp.repository.capacitacion.CapacitacionRepository;
import com.essalud.gcpp.repository.personal.IEmpleadoDao;
import com.essalud.gcpp.service.capacitacion.ICapacitacionService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;


@Service
public class CapacitacionService implements ICapacitacionService{
	
	@Autowired
	private CapacitacionRepository capacitacionRepository;
	@Autowired
	private IEmpleadoDao personalRepository;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	//private final static String DIRECTORIO_UPLOAD = "E:\\vacaciones\\papeletas\\template";
	private final static String DIRECTORIO_UPLOAD = "data/papeletas/template";
	@Override
	public Page<Capacitacion> listarCapacitacionPorUsuario(String dni, Pageable page) {
		
		return capacitacionRepository.findByPersonal(dni, page);
	}

	@Override
	public OperationResponse save(CapacitacionDTO capa) {
	
		try {
			capa.getDni().forEach(cap->
			{
				System.out.println();
				capacitacionRepository.save(new Capacitacion(null, personalRepository.findById(cap)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
						, capa.getFecha(), capa.getNombreEntidad(), capa.getCurso()));
			});
			return new OperationResponse(true, "ok");
		}catch (Exception e) {
			return new OperationResponse(false, "error");
		}
		
	}

	@Override
	public Page<Object[]> listarCapacitaciones(Pageable page) {
		
		return capacitacionRepository.listarCapacitaciones(page);
	}
	
	@Override
	public JasperPrint reporteCapacitacion(String entidad, String curso) throws SQLException, JRException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		File jasper = getPath("reporteCapacitaciones.jasper").toFile();
		Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("entidad", entidad);
        parametros.put("curso", curso);
        JasperPrint jasperprint;
        jasperprint = JasperFillManager.fillReport(jasper.getPath(), parametros, conn);
		conn.close();
		return jasperprint;
	}
	@Override
	public Path getPath(String template) {
	
		return Paths.get(DIRECTORIO_UPLOAD).resolve(template).toAbsolutePath();
	}

}
