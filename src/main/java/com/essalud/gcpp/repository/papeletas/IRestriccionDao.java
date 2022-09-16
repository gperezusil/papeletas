package com.essalud.gcpp.repository.papeletas;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.essalud.gcpp.entidades.papeletas.Restriccion;



public interface IRestriccionDao extends JpaRepository<Restriccion,Long> {
	@Query("select b from Restriccion b where b.tipo='H' order by b.cant asc")
	public List<Restriccion> listarRestriccionesBoleta();
	
	@Query("select b from Restriccion b where b.tipo='D' order by b.cant asc")
	public List<Restriccion> listarRestriccionesVacaciones();
	
	@Query("select b from Restriccion b where b.id=?1")
	public Restriccion buscarRestriccion(Long id);

}
