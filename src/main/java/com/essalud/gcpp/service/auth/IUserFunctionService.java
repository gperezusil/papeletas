package com.essalud.gcpp.service.auth;

import java.util.List;

import com.essalud.gcpp.dto.OperationResponse;

import com.essalud.gcpp.dto.SubMenuUpdateDTO;
import com.essalud.gcpp.dto.UserFunctionDTO;




public interface IUserFunctionService {
	
	UserFunctionDTO save(UserFunctionDTO roles);
	void deleteRol(String rol , String dni);
	List<UserFunctionDTO> getRoles(String dni);
	OperationResponse actualizarRolMenu(List<SubMenuUpdateDTO> menus);

}
