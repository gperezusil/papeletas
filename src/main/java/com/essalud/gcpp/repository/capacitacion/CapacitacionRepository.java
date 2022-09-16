package com.essalud.gcpp.repository.capacitacion;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.essalud.gcpp.entidades.capacitacion.Capacitacion;




public interface CapacitacionRepository extends JpaRepository<Capacitacion, Long>{
	/*Nuevo*/
	@Query("select b.entidad,b.nombreCurso,b.fecha,count(b.personal.dni) from Capacitacion b group by b.entidad,b.nombreCurso,b.fecha order by b.fecha desc")
	Page<Object[]> listarCapacitaciones(Pageable page);
	
	@Query("select b from Capacitacion b where b.personal.dni=?1 order by b.fecha asc")
	Page<Capacitacion> findByPersonal(String dni,Pageable page);
	

}
