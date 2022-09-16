package com.essalud.gcpp.service.personal;

import java.util.List;

import com.essalud.gcpp.entidades.Users;
import com.essalud.gcpp.entidades.personal.BoletaPago;

public interface IBoletaPagoService {
	
	void save(BoletaPago boletapago);
	List<BoletaPago> listarBoletaPagoAnno(String anno, Users u);

}
