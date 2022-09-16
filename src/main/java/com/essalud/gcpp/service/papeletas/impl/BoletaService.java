package com.essalud.gcpp.service.papeletas.impl;


import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.dto.papeletas.DashPapeletaDTO;
import com.essalud.gcpp.dto.papeletas.MainChartDTO;
import com.essalud.gcpp.dto.papeletas.PadreDashDTO;
import com.essalud.gcpp.dto.papeletas.SeconDashDTO;
import com.essalud.gcpp.entidades.papeletas.Boleta;
import com.essalud.gcpp.entidades.papeletas.Restriccion;
import com.essalud.gcpp.repository.papeletas.IBoletaDao;
import com.essalud.gcpp.repository.papeletas.IRestriccionDao;
import com.essalud.gcpp.service.papeletas.IBoletaService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class BoletaService implements IBoletaService{

	private final static String DIRECTORIO_UPLOAD = "data/papeletas/template";
	//private final static String DIRECTORIO_UPLOAD = "E:\\vacaciones\\papeletas\\template";
	@Autowired
	private IBoletaDao boletaDao;
	
	@Autowired
	private IRestriccionDao restriccionDao;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
	
	@Override
	@Transactional(readOnly = true)
	public Page<Boleta> findAll(Pageable pageable) {

		return boletaDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Boleta findbyId(Long id) {

		return boletaDao.findByIdBo(id);
		}

	@Override
	@Transactional(readOnly = true)
	public Page<Boleta> findByEstado(Pageable pageable) {

		return boletaDao.listarPendienteRevision(pageable,"0");
	}

	@Override
	@Transactional
	public String grabar(Boleta boleta) {
		SimpleJdbcCall jdbCall = new SimpleJdbcCall(jdbcTemplate)
			.withFunctionName("FUNCION_GENERAR_BOLETA");
		SqlParameterSource paramMap= new MapSqlParameterSource()
				.addValue("c_dni", boleta.getDni())
				.addValue("c_fecha_inicio", boleta.getFechInicio())
				.addValue("c_fecha_fin", boleta.getFechFin())
				.addValue("c_fecha_pedido", boleta.getFechPedido())
				.addValue("c_motivo", boleta.getMotivo())
				.addValue("c_metodo", boleta.getMetodo());
		return jdbCall.executeFunction(String.class,paramMap);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		boletaDao.deleteById(id);
		
	}

	@Override
	public Page<Boleta> listarBoletasUsuarioPorMes(Pageable pageable,String dni,String mes,String anno) {
		
		return boletaDao.buscarBoletaporMesAnno(pageable,dni,mes,anno);
	}

	@Override
	public List<Boleta> listarBoletasporMesyAnno(String dni, String mes, String anno) {
		
		return boletaDao.buscarBoletaspormesyAnno(dni, mes, anno);
	}

	@Override
	public JasperPrint cargar(Long id) throws  SQLException, JRException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		File jasper = getPath("papeletas.jasper").toFile();
		Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("id", id);
        JasperPrint jasperprint;
        jasperprint = JasperFillManager.fillReport(jasper.getPath(), parametros, conn);
		conn.close();
		return jasperprint;
	}

	@Override
	public Path getPath(String template) {
	
		return Paths.get(DIRECTORIO_UPLOAD).resolve(template).toAbsolutePath();
	}

	@Override
	public List<Restriccion> listarRestriccionesBoleta() {

		return restriccionDao.listarRestriccionesBoleta();
	}

	@Override
	public List<Restriccion> listarRestriccionesVacaciones() {
	
		return restriccionDao.listarRestriccionesVacaciones();
	}

	@Override
	public List<Boleta> listarDashPapeletas(String fecha) {

		return boletaDao.listarDashBoletas(fecha);
	}

	@Override
	public List<PadreDashDTO> listarDashPapeleta(String data) {
		  List<PadreDashDTO> listatotal = new ArrayList<>();
		SimpleJdbcCall jdbCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("LISTAR_DASH")
	              .returningResultSet("retorno",
	                      BeanPropertyRowMapper.newInstance(DashPapeletaDTO.class));
		jdbCall.addDeclaredParameter(new SqlParameter("C_DATA", Types.VARCHAR));

	    Map<String, Object> params=new HashMap<String, Object>();
	    params.put("C_DATA", data);
		 Map<String, Object> result =
				 jdbCall.execute(params);
		  List<DashPapeletaDTO> lista =(List) result.get("retorno");
		  
		  List<DashPapeletaDTO> listpresupuesto = new ArrayList<>();
		  List<DashPapeletaDTO> listcentral = new ArrayList<>();
		  List<DashPapeletaDTO> listcorporativo = new ArrayList<>();
		  List<DashPapeletaDTO> listinversiones = new ArrayList<>();
		  List<DashPapeletaDTO> listprocesos = new ArrayList<>();
		  List<DashPapeletaDTO> listinformacion = new ArrayList<>();
		  lista.forEach(c->{
			  if(c.getSubgerencia().equalsIgnoreCase("Gerencia De Presupuesto")) {
				 listpresupuesto.add(new DashPapeletaDTO(null, c.getName(), c.getValue()));
			  }else if(c.getSubgerencia().equalsIgnoreCase("GERENCIA CENTRAL DE PLANEAMIENTO Y PRESUPUESTO")) {
				  listcentral.add(new DashPapeletaDTO(null, c.getName(), c.getValue()));
			  }else if(c.getSubgerencia().equalsIgnoreCase("GERENCIA DE PLANEAMIENTO CORPORATIVO")) {
				  listcorporativo.add(new DashPapeletaDTO(null, c.getName(), c.getValue()));
			  }else if(c.getSubgerencia().equalsIgnoreCase("GERENCIA DE PLANIFICACION Y EVALUACION DE INVERSIONES")) {
				  listinversiones.add(new DashPapeletaDTO(null, c.getName(), c.getValue()));
			  }else if(c.getSubgerencia().equalsIgnoreCase("GERENCIA DE ORGANIZACION Y PROCESOS")) {
				  listprocesos.add(new DashPapeletaDTO(null, c.getName(), c.getValue()));
			  }
			  else if(c.getSubgerencia().equalsIgnoreCase("GERENCIA DE GESTION DE LA INFORMACION")) {
				  listinformacion.add(new DashPapeletaDTO(null, c.getName(), c.getValue()));
			  }
			 
		  });
		  PadreDashDTO presupuesto = new PadreDashDTO("Reporte Gerencia de Presupuesto",  listpresupuesto, null);
		  PadreDashDTO central = new PadreDashDTO("Reporte Gerencia Central de  Planeamiento y Presupuesto",  listcentral, null);
		  PadreDashDTO corporativo = new PadreDashDTO("Reporte Gerencia de Planeamiento Corporativo",  listcorporativo, null);
		  PadreDashDTO inversiones = new PadreDashDTO("Reporte Gerencia de Planificacion y Evaluacion de Inversiones",  listinversiones, null);
		  PadreDashDTO procesos = new PadreDashDTO("Reporte Gerencia de Organizacion y Procesos",  listprocesos, null);
		  PadreDashDTO informacion = new PadreDashDTO("Reporte Gerencia de Gestion de la Informacion",  listinformacion, null);
		  if(!listpresupuesto.isEmpty()) {
			  listatotal.add(presupuesto);
		  }
		  if(!listcentral.isEmpty()) {
			  listatotal.add(central);
		  }
		  if(!listcorporativo.isEmpty()) {
			  listatotal.add(corporativo);
		  }
		  if(!listinversiones.isEmpty()) {
			  listatotal.add(inversiones);
		  }
		  if(!listprocesos.isEmpty()) {
			  listatotal.add(procesos);
		  }
		  if(!listinformacion.isEmpty()) {
			  listatotal.add(informacion);
		  }

	
		
		 return listatotal;
	}

	@Override
	public PadreDashDTO listarDashPapeletaTotal(String data) {
		SimpleJdbcCall jdbCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("REPORTE_DASH_TOTAL")
	              .returningResultSet("retorno",
	                      BeanPropertyRowMapper.newInstance(DashPapeletaDTO.class));
		jdbCall.addDeclaredParameter(new SqlParameter("C_FECHA", Types.VARCHAR));

	    Map<String, Object> params=new HashMap<String, Object>();
	    params.put("C_FECHA", data);
		 Map<String, Object> result =
				 jdbCall.execute(params);
	      List<DashPapeletaDTO> lista =(List) result.get("retorno");

	      

	     PadreDashDTO padre = new PadreDashDTO("Reporte por Gerencia", lista,listarDashPapeleta(data));
	    
	      	
	     
	      return padre;
	}

	@Override
	public Restriccion nuevaRestriccion(Restriccion data) {
		
		return restriccionDao.save(data);
	}

	@Override
	public Restriccion buscarRestriccion(Long id) {
		
		return restriccionDao.buscarRestriccion(id);
	}

	@Override
	public Boleta actualizarPapeleta(Boleta bo) {
		
		return boletaDao.save(bo);
	}

	@Override
	public JasperPrint reportePapeletaGerencia(String area, Date inicio, Date fin) throws SQLException, JRException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		File jasper = getPath("reportePapeleta.jasper").toFile();
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
	public Page<Boleta> listarBoletasReporte(String area, Date inicio, Date fin, Pageable pageable) {

		return boletaDao.listarBoletaReporte(area, sdf.format(inicio), sdf.format(fin), pageable);
	}
	
	

	@Override
	public OperationResponse actualizarRestricciones(List<Restriccion> restricciones) {
		OperationResponse response = null;
		try {
			restricciones.forEach(resp->{	
				restriccionDao.save(resp);
		});
			response=new OperationResponse(true, "Exito al actualizar");
		} catch (Exception e) {
			response=new OperationResponse(false, "Error al actualizar");
		}
		
		return response;
	}



}
