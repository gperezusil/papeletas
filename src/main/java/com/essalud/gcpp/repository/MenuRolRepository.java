package com.essalud.gcpp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.essalud.gcpp.entidades.MenuRol;

public interface MenuRolRepository extends JpaRepository<MenuRol, Integer> {
	
	List<MenuRol> findByRole(String rol);
	Optional<MenuRol> findByRoleAndCodeMenu(String role,String codemenu);

}
