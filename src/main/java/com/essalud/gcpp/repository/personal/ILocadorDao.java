package com.essalud.gcpp.repository.personal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.essalud.gcpp.entidades.personal.Locador;





public interface ILocadorDao extends CrudRepository<Locador, String> {
	
	public Page<Locador> findByCeco(String ceco, Pageable pageable);
	
	public Locador findByRuc(String ruc);

}
