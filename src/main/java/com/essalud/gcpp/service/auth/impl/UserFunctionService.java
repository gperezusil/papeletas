package com.essalud.gcpp.service.auth.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.dto.SubMenuDTO;
import com.essalud.gcpp.dto.SubMenuUpdateDTO;
import com.essalud.gcpp.dto.UserFunctionDTO;
import com.essalud.gcpp.dto.UserUpdateDTO;
import com.essalud.gcpp.entidades.MenuRol;
import com.essalud.gcpp.entidades.UserFunctions;
import com.essalud.gcpp.repository.IUserFunctionsRepository;
import com.essalud.gcpp.repository.IUserRepository;
import com.essalud.gcpp.repository.MenuRolRepository;
import com.essalud.gcpp.service.auth.IUserFunctionService;

@Service
public class UserFunctionService implements IUserFunctionService{

	@Autowired private IUserFunctionsRepository userFunctionRepository;
	@Autowired private IUserRepository userRepository;
	@Autowired private MenuRolRepository menuRolRepository;
	
	@Override
	public UserFunctionDTO save(UserFunctionDTO roles) {
		
		UserFunctions u= userFunctionRepository.save(new UserFunctions(null, userRepository.findByUsername(roles.getDni()),
					roles.getRol(), null));
		
		
		return new UserFunctionDTO(u.getRole(),null);
	}

	@Override
	public void deleteRol(String rol , String dni) {
		
		userFunctionRepository.deleteById(userFunctionRepository.obtenerRolUsuario(dni, rol).getId());
	}

	@Override
	public List<UserFunctionDTO> getRoles(String dni) {
		
		
		return userFunctionRepository.findbyDni(dni).stream().map(c->
		new UserFunctionDTO(c.getRole(),c.getUser().getUsername())).collect(Collectors.toList()) ;
	}

	@Override
	public OperationResponse actualizarRolMenu(List<SubMenuUpdateDTO> menus) {
		OperationResponse response =null;
		try {
			menus.forEach(m->{
				MenuRol menu = menuRolRepository.findByRoleAndCodeMenu(m.getUser().toUpperCase(), m.getCode()).orElse(null);
				
				if(m.isSelected()) {
					if(menu==null) {
						menuRolRepository.save(new MenuRol(null, m.getUser().toUpperCase(), m.getCode()));
					}
				}else {
					if(menu!=null) {
						menuRolRepository.deleteById(menu.getId());
					}
				}
				
			});
			response = new OperationResponse(true, "Exito al actualizar");
		}catch (Exception e) {
			response = new OperationResponse(false, e.getMessage());
		}
		
		
		
		return response;
	}

}
