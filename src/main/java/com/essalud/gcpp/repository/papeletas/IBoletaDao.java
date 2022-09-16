package com.essalud.gcpp.repository.papeletas;






import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.essalud.gcpp.entidades.papeletas.Boleta;




public interface IBoletaDao extends JpaRepository<Boleta, Long> {
	
	public Boleta findByIdBo(Long codBoleta);
	
	@Query("select b from Boleta b where b.estado=?1 ORDER BY b.fechInicio desc")
	public Page<Boleta> listarPendienteRevision(Pageable pageable,String estado);
	
	@Query("select b from Boleta b where b.dni=?1 and to_char(b.fechInicio,'mm')=?2 and to_char(b.fechInicio,'YYYY')=?3")
	public Page<Boleta> buscarBoletaporMesAnno(Pageable pageable,String dni,String mes,String anno);
	
	@Query("select b from Boleta b where b.dni=?1 and to_char(b.fechInicio,'mm')=?2 and to_char(b.fechInicio,'YYYY')=?3")
	public List<Boleta> buscarBoletaspormesyAnno(String dni,String mes,String anno);
	
	@Query(nativeQuery=true,value="SELECT FUNCION_GENERAR_BOLETA(:dni,:fecha_inicio,:fecha_fin,:motivo,:metodo) FROM dual")
	String generarBoleta(@Param("dni")String dni,@Param("fecha_inicio")String fecha_inicio,@Param("fecha_fin")String fecha_fin,
						@Param("motivo")String motivo,@Param("metodo")String metodo);
	
	
	@Query("select b from Boleta b where b.metodo='0' and TO_CHAR(b.fechInicio,'mm/yyyy')=?1")
	public List<Boleta> listarDashBoletas(String fecha);
	
	@Query("select b from Boleta b where substr(b.personal.cod00,0,6)=?1 and b.fechInicio>=to_date(?2,'dd/mm/yy') and b.fechInicio<=to_date(?3,'dd/mm/yy') order by b.fechInicio asc")
	public Page<Boleta> listarBoletaReporte(String area,String inicio,String fin,Pageable page);

	
	
	
	
	

}
