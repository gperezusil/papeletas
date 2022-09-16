package com.essalud.gcpp.service.personal;

import java.util.List;

import com.essalud.gcpp.entidades.ConfiguracionAnno;
import com.essalud.gcpp.entidades.personal.Cargo;
import com.essalud.gcpp.entidades.personal.Ceco;
import com.essalud.gcpp.entidades.personal.CentroCosto;


public interface ICecoService {
	
     List<CentroCosto> listarCentroCosto();
     List<Ceco> listarSubgerencia();
     List<Cargo> listarCargos(String filtro);
     Cargo findByCod(String cod);
     List<ConfiguracionAnno> findAll();
    

}
