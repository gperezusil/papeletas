package com.essalud.gcpp.service.vacaciones;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.essalud.gcpp.entidades.vacaciones.Vacaciones;
import com.essalud.gcpp.entidades.vacaciones.VacacionesDias;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface IVacacionesService {
	
	
	Page<Vacaciones> buscarVacacionesUsuario(String dni,Pageable pageable);
	Page<Vacaciones> buscarVacacionesPasadas(String dni,Pageable pageable);
	List<VacacionesDias> listarVacacionesPeriodoDisponible(String dni);
	String reprogramarVacaciones(Long cod,String identificador);
	String denegarVacaciones(Long cod,String identificador);
	String programarVacaciones(Vacaciones vacaciones);
	String programarVacacionesAdmin(Vacaciones vacaciones);
	Page<Vacaciones> listarVacacionesRevision(Pageable pageable);
	Page<Vacaciones> listarVacacionesSubgerente(String ceco,Pageable pageable);
	Page<Vacaciones> listarVacacionesGerente(String ceco,Pageable pageable);
	Vacaciones buscarVacaciones(Long idva);
	Vacaciones guardarVacaciones(Vacaciones vaca);
	Page<Vacaciones> historialVacaciones(String dni,String anno,Pageable pageable);
	Page<Vacaciones> historialVacacionesTodo(String dni,Pageable pageable);
	void eliminarVacacionePeriodo(Long id);
	VacacionesDias actualizarDias(VacacionesDias periodo);
	VacacionesDias buscarVacacionesPeriodo(Long id);
	void eliminarHistorialVacaciones(Long id);
	List<Vacaciones> listarVacacionesCoincidencia(String area ,Date fecha);
	
	List<VacacionesDias> programacionAnualPersonal(String anno);
	Page<Vacaciones> listarVacacionesReporteGerencia(String area, Date fechaInicio, Date fechaFin,Pageable pageable);
	public JasperPrint cargar(String area,Date inicio,Date fin) throws SQLException, JRException;
	public JasperPrint reporteVacacionesPersonal(String dni) throws SQLException, JRException;
	public Path getPath(String template);
}
