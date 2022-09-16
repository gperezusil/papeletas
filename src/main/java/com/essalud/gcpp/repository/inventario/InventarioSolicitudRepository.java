package com.essalud.gcpp.repository.inventario;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.essalud.gcpp.entidades.inventario.Solicitud;
import com.essalud.gcpp.entidades.personal.Personal;

public interface InventarioSolicitudRepository extends JpaRepository<Solicitud, Integer>{
	
	@Query(nativeQuery = true, value= "select to_char(fecha,'dd/MM/yyyy') fecha ,u.display_name, u.username,count(*)as cantidad from GCPP_INVENTARIO_SOLICITUD so inner join users u on u.username=so.dni where so.estado=0 and (LOWER(u.display_name) like ?1 or LOWER(u.username) like ?1) group by to_char(fecha,'dd/MM/yyyy'),u.display_name, u.username")
	Page<Object[]> listarSolicitudInventario(String filtro,Pageable pageable);
	@Query(nativeQuery = true, value= "select to_char(fecha,'dd/MM/yyyy') fecha ,u.display_name, u.username,count(*)as cantidad from GCPP_INVENTARIO_SOLICITUD so inner join users u on u.username=so.dni where u.username=?2 and (LOWER(u.display_name) like ?1 or LOWER(u.username) like ?1) group by to_char(fecha,'dd/MM/yyyy'),u.display_name, u.username")
	Page<Object[]> listarSolicitudInventarioPersonal(String filtro,String dni,Pageable pageable);
	@Query(nativeQuery = true, value= "select to_char(fecha,'dd/MM/yyyy') fecha ,u.display_name, u.username,count(*)as cantidad from GCPP_INVENTARIO_SOLICITUD so inner join users u on u.username=so.dni where so.estado!=0 and (LOWER(u.display_name) like ?1 or LOWER(u.username) like ?1) group by to_char(fecha,'dd/MM/yyyy'),u.display_name, u.username")
	Page<Object[]> listarSolicitudInventarioHistorial(String filtro,Pageable pageable);
	@Query("select i from Solicitud i where i.dni=?1 and to_char(i.fecha,'dd/MM/yyyy')=?2")
	Page<Solicitud> findByPersonalAndFecha(String dni ,String fecha,Pageable pageable);
	
	
	@Query("select i from Solicitud i where i.estado in(1,2)")
	Page<Solicitud> listarHistorialSolicitud(Pageable pageable );
	
	@Query("select i from Solicitud i where i.dni=?1")
	Page<Solicitud> listarMisSolicitudes(String dni,Pageable pageable );
	
	@Query("select i from Solicitud i where i.estado in (1,2)")
	List<Solicitud> listarHistorialTodo();

}
