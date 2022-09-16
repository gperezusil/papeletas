package com.essalud.gcpp.repository.vacaciones;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.essalud.gcpp.entidades.personal.Personal;
import com.essalud.gcpp.entidades.vacaciones.VacacionesDias;



public interface Vacaciones_DiasDao extends JpaRepository<VacacionesDias, Long> {
	
	@Query("select b from VacacionesDias b where b.dni=?1 and b.dias>0 ORDER BY b.periodo desc")
	public List<VacacionesDias> buscarVacacionesPeriodoDisponibles(String dni);
	
	@Query("select p from Personal p where p.dni not in  (select v.dni from  VacacionesDias v where v.periodo=?1) and p.estadoPlaza=1")
	List<Personal> getlistPersonalProgramacionAnual(String anno);

}
