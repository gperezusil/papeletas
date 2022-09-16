package com.essalud.gcpp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.essalud.gcpp.entidades.UserFunctions;
import com.essalud.gcpp.entidades.Users;

public interface IUserFunctionsRepository extends JpaRepository<UserFunctions, Integer>{
	@Query("select u from UserFunctions u where u.user.username=?1 and u.role=?2")
	UserFunctions obtenerRolUsuario(String dni,String role);
	
	@Query("select u from UserFunctions u where u.user.username=?1")
	List<UserFunctions> findbyDni(String dni);
	
	Optional<UserFunctions> findByUser(Users u);
	
	
	void deleteByUser(Users users);

}
