package com.essalud.gcpp.repository.personal;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.essalud.gcpp.entidades.personal.Cargo;
import com.essalud.gcpp.entidades.personal.Ceco;
import com.essalud.gcpp.entidades.personal.CentroCosto;



public interface ICecoDao extends CrudRepository<CentroCosto, Long> {
	
	@Query("select b from Ceco b where b.padre is not null ORDER BY b.descripcionCeco asc")
	List<Ceco> listarSubgerencias();
	@Query("select b from Cargo b where (LOWER(b.descripcionProfesion) like ?1 OR LOWER(b.descripcionBasica) like ?1 OR LOWER(b.descripcionPlaza) like ?1) and rownum <= 5 ORDER BY b.descripcionPlaza asc")
	List<Cargo> listarCargos(String filter);
	
	@Query("select b from Cargo b where b.codcargo=?1")
	Cargo findCargoByCode(String code);
}
