package com.essalud.gcpp.service.personal.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.essalud.gcpp.entidades.Users;
import com.essalud.gcpp.entidades.personal.BoletaPago;
import com.essalud.gcpp.repository.personal.BoletaPagoRepository;
import com.essalud.gcpp.service.personal.IBoletaPagoService;

@Service
public class BoletaPagoService implements IBoletaPagoService{

	@Autowired private BoletaPagoRepository boletaPagoRepository;
	
	@Override
	public void save(BoletaPago boletapago) {
		boletaPagoRepository.save(boletapago);
		
	}

	@Override
	public List<BoletaPago> listarBoletaPagoAnno(String anno,Users user) {
		
		return boletaPagoRepository.findByAnnoAndDni(anno, user.getUsername());
	}

}
