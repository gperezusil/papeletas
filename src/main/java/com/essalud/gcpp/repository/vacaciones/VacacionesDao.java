package com.essalud.gcpp.repository.vacaciones;



import java.util.Date;
import java.util.List;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import com.essalud.gcpp.entidades.vacaciones.Vacaciones;




@Repository
public interface VacacionesDao extends JpaRepository<Vacaciones, Long> {
	
	public Vacaciones findByIdVa(Long idva);
	
	@Query("select b from Vacaciones b where b.carta is null and b.estado=1 ORDER BY b.fechaSolicitud desc")
	public Page<Vacaciones> listarVacacionesRevision(Pageable pageable);
	
	@Query("select b from Vacaciones b where  b.estado in (0,1) and b.dni=?1  ORDER BY b.fInicio desc")
	public Page<Vacaciones> listarVacacionesPendientes(String dni,Pageable pageable);
	
	@Query("select b from Vacaciones b where b.dni=?1 and b.estado in (2,3,4) ORDER BY b.fInicio desc")
	public Page<Vacaciones> listarVacacionesPasadas(String dni,Pageable pageable);
	
	@Query("select b from Vacaciones b where b.personal.cod00=?1 and b.estado=0 and b.validacionSubge=0 ORDER BY b.fInicio desc")
	public Page<Vacaciones> listarVacacionesPersonalSubgerente(String ceco,Pageable pageable);

	@Query("select b from Vacaciones b where substr(b.personal.cod00,0,6)=?1 and b.estado=0 and b.validacionSubge=1 and b.validacionGere=0 ORDER BY b.fInicio desc")
	public Page<Vacaciones> listarVacacionesPersonalGerente(String ceco,Pageable pageable);
	
	@Query("select b from Vacaciones b where b.dni=?1 and b.vperiodo.periodo=?2  ORDER BY b.estado asc,fInicio desc")
	public Page<Vacaciones> listarHistorialVacaciones(String dni,String anno,Pageable pageable);
	
	@Query("select b from Vacaciones b where b.dni=?1 ORDER BY b.estado asc,fInicio desc")
	public Page<Vacaciones> listarHistorialVacacionesTodo(String dni,Pageable pageable);
	
	@Query("select b from Vacaciones b where b.personal.cod00=?1 and ?2 between b.fInicio and b.fFin and b.estado in (0,1) ORDER BY fInicio desc")
	public List<Vacaciones> listarVacacionesSimilares(String area, Date fechaInicio);
	
	@Query("select b from Vacaciones b where substr(b.personal.cod00,0,6)=?1 and b.fInicio>=to_date(?2,'dd/mm/yy') and b.fInicio<=to_date(?3,'dd/mm/yy') and b.estado in (0,1,4) and b.personal.estadoPlaza=1 ORDER BY b.fInicio asc")
	public Page<Vacaciones> listarVacacionesreporteGerencia(String area,  String fechaInicio, String fechaFin,Pageable pageable);
	
	
	
	
	
}
