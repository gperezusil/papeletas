package com.essalud.gcpp.repository.descanso;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.essalud.gcpp.entidades.descanso.DescansoMedico;

public interface DescansoMedicoRepository extends JpaRepository<DescansoMedico, Integer>{
	@Query("select p from DescansoMedico  p where (LOWER(p.personal.dni) like ?1 or LOWER(p.personal.apenombres) like ?1 or LOWER(p.personal.codigoPlanilla) like ?1) order by p.createAt desc ") 
	Page<DescansoMedico> findAll(String filter, Pageable page);
}
