package com.essalud.gcpp.service.personal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.essalud.gcpp.entidades.personal.Locador;
import com.essalud.gcpp.entidades.personal.OrdenesLocador;





public interface ILocadorService {

	Page<Locador> listarLocadores(String ceco,Pageable page);
	Page<OrdenesLocador> listarOrdenesLocadores(String ruc,Pageable page);
	Locador save(Locador locador);
	Locador buscar(String ruc);
	OrdenesLocador saveOrden(OrdenesLocador orden);
}
