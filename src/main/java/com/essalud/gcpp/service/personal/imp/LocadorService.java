package com.essalud.gcpp.service.personal.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.essalud.gcpp.entidades.personal.Locador;
import com.essalud.gcpp.entidades.personal.OrdenesLocador;
import com.essalud.gcpp.repository.personal.ILocadorDao;
import com.essalud.gcpp.repository.personal.OrdenesLocadoresRepository;
import com.essalud.gcpp.service.personal.ILocadorService;


@Service
public class LocadorService implements ILocadorService {

	@Autowired
	private ILocadorDao locadorDao;
	@Autowired
	private OrdenesLocadoresRepository ordenesRepository;
	
	@Override
	@Transactional(readOnly = true)
	public Page<Locador> listarLocadores(String ceco,Pageable pageable) {
		// TODO Auto-generated method stub
		return locadorDao.findByCeco(ceco, pageable);
	}

	@Override
	public Locador save(Locador locador) {
		// TODO Auto-generated method stub
		return locadorDao.save(locador);
	}

	@Override
	public Locador buscar(String ruc) {
		
		return locadorDao.findByRuc(ruc);
	}

	@Override
	public Page<OrdenesLocador> listarOrdenesLocadores(String ruc, Pageable page) {
		// TODO Auto-generated method stub
		return ordenesRepository.findByRuc(ruc,page);
	}

	@Override
	public OrdenesLocador saveOrden(OrdenesLocador orden) {
		// TODO Auto-generated method stub
		return ordenesRepository.save(orden);
	}

}
