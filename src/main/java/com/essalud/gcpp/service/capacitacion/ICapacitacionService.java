package com.essalud.gcpp.service.capacitacion;

import java.nio.file.Path;
import java.sql.SQLException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.dto.capacitacion.CapacitacionDTO;
import com.essalud.gcpp.entidades.capacitacion.Capacitacion;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;





public interface ICapacitacionService {
	
	Page<Capacitacion> listarCapacitacionPorUsuario(String dni,Pageable page);
	OperationResponse save(CapacitacionDTO capa);
	Page<Object[]> listarCapacitaciones(Pageable page);
	
	public JasperPrint reporteCapacitacion(String entidad, String curso) throws SQLException, JRException ;

	public Path getPath(String template) ;

}
