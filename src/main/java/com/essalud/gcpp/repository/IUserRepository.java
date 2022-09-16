package com.essalud.gcpp.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.essalud.gcpp.entidades.Users;



@Repository
public interface IUserRepository extends JpaRepository<Users, Integer>{
	public Users findByUsername(String username);
	public List<Users> findByEnable(boolean enable);
	public List<Users> findByArea(String area);
	public void deleteByUsername(String username);
	@Query("select u from Users u where LOWER(u.displayName) like ?1")
	Page<Users> getListUsers(String filter,Pageable page);
	
}
