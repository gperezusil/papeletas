package com.essalud.gcpp.service.papeletas;


import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.dto.papeletas.DashPapeletaDTO;
import com.essalud.gcpp.dto.papeletas.PadreDashDTO;
import com.essalud.gcpp.entidades.papeletas.Boleta;
import com.essalud.gcpp.entidades.papeletas.Restriccion;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;



public interface IBoletaService {
	
	public Page<Boleta> findAll(Pageable pageable);
	public Boleta findbyId(Long id);
	public Page<Boleta> findByEstado(Pageable pageable);
	public String grabar(Boleta boleta);
	public void deleteById(Long id);
	public Page<Boleta> listarBoletasUsuarioPorMes(Pageable pageable,String dni,String mes,String anno);
	public List<Boleta> listarBoletasporMesyAnno(String dni,String mes,String anno);
	public JasperPrint cargar(Long id) throws SQLException, JRException;
	public Path getPath(String template);
	public List<Restriccion> listarRestriccionesBoleta();
	public List<Restriccion> listarRestriccionesVacaciones();
	public List<Boleta> listarDashPapeletas(String fecha);
	public List<PadreDashDTO> listarDashPapeleta(String data);
	public PadreDashDTO listarDashPapeletaTotal(String data);
	public Restriccion buscarRestriccion(Long id);
	public Restriccion nuevaRestriccion(Restriccion data);
	public Boleta actualizarPapeleta(Boleta bo );
	public Page<Boleta> listarBoletasReporte(String area, Date inicio , Date fin,Pageable pageable);
	public JasperPrint reportePapeletaGerencia(String area, Date inicio , Date fin) throws SQLException, JRException;
	
	public OperationResponse actualizarRestricciones(List<Restriccion> restricciones);
}
