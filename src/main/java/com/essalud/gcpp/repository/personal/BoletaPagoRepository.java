package com.essalud.gcpp.repository.personal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.essalud.gcpp.entidades.personal.BoletaPago;

public interface BoletaPagoRepository  extends JpaRepository<BoletaPago, Integer>{
	
	List<BoletaPago> findByAnnoAndDni(String anno,String dni);
	
	Optional<BoletaPago> findByAnnoAndMesAndDni(String anno,String mes,String dni);

}
