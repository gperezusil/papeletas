package com.essalud.gcpp.service.personal.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.essalud.gcpp.entidades.ConfiguracionAnno;
import com.essalud.gcpp.entidades.personal.Cargo;
import com.essalud.gcpp.entidades.personal.Ceco;
import com.essalud.gcpp.entidades.personal.CentroCosto;
import com.essalud.gcpp.repository.ConfiguracionAnnoRepository;
import com.essalud.gcpp.repository.personal.ICecoDao;
import com.essalud.gcpp.service.personal.ICecoService;


@Service
public class CecoService implements ICecoService {

	@Autowired
	private ICecoDao cecoDao;
	@Autowired
	private ConfiguracionAnnoRepository configuracionAnno;
	

	
	@Override
	@Transactional(readOnly = true)
	public List<CentroCosto> listarCentroCosto() {
		
		return (List<CentroCosto>) cecoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Ceco> listarSubgerencia() {

		return cecoDao.listarSubgerencias();
	}

	@Override
	public List<Cargo> listarCargos(String filtro) {

		return cecoDao.listarCargos("%" + filtro.replace(" ", "%") + "%");
	}

	@Override
	public List<ConfiguracionAnno> findAll() {
		return configuracionAnno.findAll();
	}

	@Override
	public Cargo findByCod(String cod) {
		System.out.println(cod);
		return cecoDao.findCargoByCode(cod);
	}

	

}
