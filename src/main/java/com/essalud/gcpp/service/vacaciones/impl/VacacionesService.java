package com.essalud.gcpp.service.vacaciones.impl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.essalud.gcpp.entidades.vacaciones.Vacaciones;
import com.essalud.gcpp.entidades.vacaciones.VacacionesDias;
import com.essalud.gcpp.repository.vacaciones.VacacionesDao;
import com.essalud.gcpp.repository.vacaciones.Vacaciones_DiasDao;
import com.essalud.gcpp.service.vacaciones.IVacacionesService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class VacacionesService implements IVacacionesService {
	
	@Autowired
	private VacacionesDao vacacionesDao;
	
	@Autowired
	private Vacaciones_DiasDao vacacionesDiasDao;
	
	@Autowired
	private JdbcTemplate simpleJdbcCall;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
	
	//private final static String DIRECTORIO_UPLOAD = "E:\\vacaciones\\vacaciones\\template";
	private final static String DIRECTORIO_UPLOAD = "data/vacaciones/template";
	
	
	@Override
	@Transactional(readOnly = true)
	public Page<Vacaciones>buscarVacacionesUsuario(String dni,Pageable pageable) {
		
		return vacacionesDao.listarVacacionesPendientes(dni, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<VacacionesDias> listarVacacionesPeriodoDisponible(String dni) {
		
		return vacacionesDiasDao.buscarVacacionesPeriodoDisponibles(dni);
	}

	@Override
	@Transactional
	public String reprogramarVacaciones(Long cod,String identificador) {
		SimpleJdbcCall jdbCall = new SimpleJdbcCall(simpleJdbcCall)
				.withFunctionName("REPROGRAMAR_VACACIONES");
			SqlParameterSource paramMap= new MapSqlParameterSource()
					.addValue("ID_VACACIONES", cod)
					.addValue("IDENTIFICADOR", identificador);
			return jdbCall.executeFunction(String.class,paramMap);
	}

	@Override
	@Transactional
	public String programarVacaciones(Vacaciones vacaciones) {
		SimpleJdbcCall jdbCall = new SimpleJdbcCall(simpleJdbcCall)
				.withFunctionName("GENERAR_VACACIONES");
			SqlParameterSource paramMap= new MapSqlParameterSource()
					.addValue("C_DNI", vacaciones.getDni())
			.addValue("C_IDVA", vacaciones.getIdDva())
			.addValue("C_DNI", vacaciones.getDni())
			.addValue("C_FECHA_INICIO", vacaciones.getFInicio())
			.addValue("C_FECHA_FIN", vacaciones.getFFin())
			.addValue("C_ID_VACACIONES", vacaciones.getIdDva())
			.addValue("C_MOTIVO", vacaciones.getMotivo())
			.addValue("IDENTIFICADOR", vacaciones.getCarta());
			return jdbCall.executeFunction(String.class,paramMap);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Vacaciones> listarVacacionesRevision(Pageable pageable) {
		// TODO Auto-generated method stub
		return vacacionesDao.listarVacacionesRevision(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Vacaciones> buscarVacacionesPasadas(String dni, Pageable pageable) {
		// TODO Auto-generated method stub
		return vacacionesDao.listarVacacionesPasadas(dni, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Vacaciones> listarVacacionesSubgerente(String ceco, Pageable pageable) {
		return vacacionesDao.listarVacacionesPersonalSubgerente(ceco, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Vacaciones buscarVacaciones(Long idva) {
		// TODO Auto-generated method stub
		return vacacionesDao.findByIdVa(idva);
	}

	@Override
	@Transactional
	public Vacaciones guardarVacaciones(Vacaciones vaca) {
		// TODO Auto-generated method stub
		return vacacionesDao.save(vaca);
	}

	@Override
	public Page<Vacaciones> listarVacacionesGerente(String ceco, Pageable pageable) {
		
		return vacacionesDao.listarVacacionesPersonalGerente(ceco.substring(0,6), pageable);
	}

	@Override
	public Page<Vacaciones> historialVacaciones(String dni,String anno, Pageable pageable) {
		
		return vacacionesDao.listarHistorialVacaciones(dni,anno, pageable);
	}

	@Override
	public void eliminarVacacionePeriodo(Long id) {
		
		vacacionesDiasDao.deleteById(id);
	}

	@Override
	public VacacionesDias actualizarDias(VacacionesDias periodo) {
		
		return vacacionesDiasDao.save(periodo);
	}

	@Override
	public VacacionesDias buscarVacacionesPeriodo(Long id) {
		
		return vacacionesDiasDao.findById(id).orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	public void eliminarHistorialVacaciones(Long id) {
		vacacionesDao.deleteById(id);
		
	}

	@Override
	public Page<Vacaciones> historialVacacionesTodo(String dni, Pageable pageable) {
		return vacacionesDao.listarHistorialVacacionesTodo(dni,pageable);
	}

	@Override
	@Transactional
	public String programarVacacionesAdmin(Vacaciones vacaciones) {
		SimpleJdbcCall jdbCall = new SimpleJdbcCall(simpleJdbcCall)
				.withFunctionName("GENERAR_VACACIONES_ADMIN");
			SqlParameterSource paramMap= new MapSqlParameterSource()
					.addValue("C_DNI", vacaciones.getDni())
			.addValue("C_DNI", vacaciones.getDni())
			.addValue("C_IDVA", vacaciones.getIdDva())
			.addValue("C_FECHA_INICIO", vacaciones.getFInicio())
			.addValue("C_FECHA_FIN", vacaciones.getFFin())
			.addValue("C_ID_VACACIONES", vacaciones.getIdDva())
			.addValue("C_MOTIVO", vacaciones.getMotivo())
			.addValue("C_ESTADO", 1);
			return jdbCall.executeFunction(String.class,paramMap);
	}

	@Override
	public List<Vacaciones> listarVacacionesCoincidencia(String area, Date fecha) {
		
		return vacacionesDao.listarVacacionesSimilares(area, fecha);
	}

	@Override
	public String denegarVacaciones(Long cod,String identificador) {
		SimpleJdbcCall jdbCall = new SimpleJdbcCall(simpleJdbcCall)
				.withFunctionName("DENEGAR_VACACIONES");
			SqlParameterSource paramMap= new MapSqlParameterSource()
					.addValue("ID_VACACIONES", cod)
					.addValue("IDENTIFICADOR", identificador);
			return jdbCall.executeFunction(String.class,paramMap);
	}

	@Override
	public List<VacacionesDias> programacionAnualPersonal(String anno) {
		return vacacionesDiasDao.getlistPersonalProgramacionAnual(anno).stream().map(c->{
			VacacionesDias v = new VacacionesDias();
			v.setDias(30);
			v.setDni(c.getDni());
			v.setPeriodo(anno);
			return vacacionesDiasDao.save(v);
		}).collect(Collectors.toList());
		
	}

	@Override
	public Page<Vacaciones> listarVacacionesReporteGerencia(String area, Date fechaInicio, Date fechaFin,
			Pageable pageable) {

		return vacacionesDao.listarVacacionesreporteGerencia(area, sdf.format(fechaInicio), sdf.format(fechaFin), pageable);
	}
	
	@Override
	public JasperPrint cargar(String area,Date inicio,Date fin) throws  SQLException, JRException {
		Connection conn = simpleJdbcCall.getDataSource().getConnection();
		File jasper = getPath("reporteVacacionesGerencia.jasper").toFile();
		System.out.println(jasper.getAbsolutePath());
		Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("cod", area);
        parametros.put("inicio", sdf.format(inicio));
        parametros.put("fin", sdf.format(fin));
        JasperPrint jasperprint;
        jasperprint = JasperFillManager.fillReport(jasper.getPath(), parametros, conn);
        conn.close();
		return jasperprint;
	}
	
	@Override
	public Path getPath(String template) {
		// TODO Auto-generated method stub
		return Paths.get(DIRECTORIO_UPLOAD).resolve(template).toAbsolutePath();
	}

	@Override
	public JasperPrint reporteVacacionesPersonal(String dni) throws SQLException, JRException {
		Connection conn = simpleJdbcCall.getDataSource().getConnection();
		File jasper = getPath("reporteVacaciones.jasper").toFile();
		System.out.println(jasper.getAbsolutePath());
		Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("dni", dni);
        JasperPrint jasperprint;
        jasperprint = JasperFillManager.fillReport(jasper.getPath(), parametros, conn);
		conn.close();
		return jasperprint;
	}

}
