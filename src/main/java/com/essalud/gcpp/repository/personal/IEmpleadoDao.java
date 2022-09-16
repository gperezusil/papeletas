package com.essalud.gcpp.repository.personal;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.essalud.gcpp.entidades.personal.Personal;
import com.essalud.gcpp.entidades.personal.Rol;






public interface IEmpleadoDao extends JpaRepository<Personal, String> {
	
	public Personal findByDni(@Param("dni") String dni);
	public Page<Personal> findByCod00(String cod00, Pageable pageable);
	
	@Query("select b from Personal b where b.estadoPlaza=1 and b.fechNaci is not null")
	List<Personal> listarCumpleanos();
	@Query("select b from Rol b where b.id not in (1)")
	List<Rol> listarRoles();
	@Query("select p from Personal  p where (LOWER(p.dni) like ?1 or LOWER(p.apenombres) like ?1 or LOWER(p.codigoPlanilla) like ?1) and p.cod00= all(select c from Ceco c where c.codigo=?2)")
	public Page<Personal> findByFilter(String filter,String ceco, Pageable pageable);
	@Query("select p from Personal  p where (LOWER(p.dni) like ?1 or LOWER(p.apenombres) like ?1 or LOWER(p.codigoPlanilla) like ?1) and ROWNUM < 5")
	public List<Personal> findByFilter2(String filter);
}
