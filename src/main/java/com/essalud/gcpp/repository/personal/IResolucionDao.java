package com.essalud.gcpp.repository.personal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.essalud.gcpp.entidades.personal.Resolucion;



public interface IResolucionDao extends JpaRepository<Resolucion, Long> {
	
	List<Resolucion> findByDni(String dni);

}
