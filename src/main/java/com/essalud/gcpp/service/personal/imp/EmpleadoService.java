package com.essalud.gcpp.service.personal.imp;


import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.essalud.gcpp.entidades.Users;
import com.essalud.gcpp.entidades.personal.BoletaPago;
import com.essalud.gcpp.entidades.personal.Personal;
import com.essalud.gcpp.entidades.personal.Rol;
import com.essalud.gcpp.repository.IUserFunctionsRepository;
import com.essalud.gcpp.repository.IUserRepository;
import com.essalud.gcpp.repository.personal.BoletaPagoRepository;
import com.essalud.gcpp.repository.personal.IEmpleadoDao;
import com.essalud.gcpp.service.personal.IBoletaPagoService;
import com.essalud.gcpp.service.personal.IPersonalService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;






@Service
public class EmpleadoService implements IPersonalService  {

	@Autowired
	private IEmpleadoDao personalDao;
	
	@Autowired BoletaPagoRepository boletaPagoRepository;
	
	@Autowired IUserRepository userRepository;
	@Autowired IUserFunctionsRepository functionsRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//private final static String DIRECTORIO_UPLOAD = "E:\\vacaciones\\personal";
	private final static String DIRECTORIO_UPLOAD = "data/personal";
	@Override
	@Transactional(readOnly = true)
	public Page<Personal> buscarPersonalArea(String cod00, Pageable pageable) {

		return personalDao.findByCod00(cod00, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Personal buscarUsuario(String dni) {
		
		return personalDao.findByDni(dni);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Personal> listarCumpleanos() {
		
		return personalDao.listarCumpleanos();
	}

	@Override
	public Personal crearPersonal(Personal dto) {
		
		return personalDao.save(dto);
	}
	
	



	@Override
	@Transactional
	public void deleteById(String dni) {
		
		personalDao.deleteById(dni);
		
		functionsRepository.deleteByUser(userRepository.findByUsername(dni));
		userRepository.deleteByUsername(dni);
		
	}

	@Override
	public List<Rol> listarRoles() {

		return personalDao.listarRoles();
	}

	@Override
	public List<Personal> listarTodos() {
		return (List<Personal>) personalDao.findAll();
	}
	@Override
	public void cambiarclave() {
		personalDao.findAll().stream().map(p->{
		
			return personalDao.save(p);
		}).collect(Collectors.toList());
	}

	@Override
	public JasperPrint reportePersonal() {
		Connection conn;
		JasperPrint jasperprint = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			File jasper = getPath("reportePersonal.jasper").toFile();
			Map<String, Object> parametros = new HashMap<String, Object>();
	        jasperprint = JasperFillManager.fillReport(jasper.getPath(), parametros, conn);
			conn.close();
			
		} catch (SQLException | JRException e) {
			e.printStackTrace();
		}
		
		return jasperprint;
		
	}
	
	public Path getPath(String template) {
		
		return Paths.get(DIRECTORIO_UPLOAD).resolve(template).toAbsolutePath();
	}

	@Override
	public boolean cargarArchivoBoletaPago(MultipartFile file)  {
		boolean estado=false;
		try {
			CargarBoletaPago servicio = new CargarBoletaPago();
		  
			servicio.parsePDFDocument(file).forEach(p->{
				 
				if(!boletaPagoRepository.findByAnnoAndMesAndDni(p.getAnno(),p.getMes(),p.getDni()).isPresent()) {
					boletaPagoRepository.save(new BoletaPago(null,p.getDni(), p.getAnno(), p.getMes(), p.getFile(), new Date()));
				}
				
			});
				if(!servicio.parsePDFDocument(file).isEmpty()) {
					estado=true;
				}

		} catch (Exception e) {
		}
		return estado;
	}

	@Override
	public Resource descargarManual() {
		Path rutaArchivo = getPath("Manual- V-2.0. Final.pdf");
		
		Resource recurso = null;
		try {
			recurso = new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return recurso;
	}

	@Override
	public Page<Personal> buscarporFiltro(String filtro,String ceco, Pageable pageable) {
		
		return personalDao.findByFilter("%"+filtro+"%", ceco,pageable);
	}

	@Override
	public List<Personal> buscarporFiltro2(String filtro) {
		return personalDao.findByFilter2("%"+filtro+"%");
	}
	

}
