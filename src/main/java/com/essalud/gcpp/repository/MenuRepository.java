package com.essalud.gcpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.essalud.gcpp.entidades.Menu;


public interface MenuRepository extends JpaRepository<Menu,Integer> {
	
	@Query(nativeQuery = true, value = "SELECT me.* FROM GCPP_MENU me inner join gcpp_rol_menu rom on rom.CODE_MENU=me.CODE where rom.ROLE=?1 order by m_order asc" )
	List<Menu> getMenusRol(String rol);

}
