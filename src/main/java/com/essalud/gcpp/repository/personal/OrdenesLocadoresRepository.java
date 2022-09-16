package com.essalud.gcpp.repository.personal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.essalud.gcpp.entidades.personal.OrdenesLocador;



public interface OrdenesLocadoresRepository  extends JpaRepository<OrdenesLocador, Long>{
	
	Page<OrdenesLocador> findByRuc(String ruc, Pageable page);
	

}
