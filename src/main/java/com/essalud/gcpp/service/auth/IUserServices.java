package com.essalud.gcpp.service.auth;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.essalud.gcpp.dto.MenuDTO;
import com.essalud.gcpp.dto.MenuGroup;
import com.essalud.gcpp.dto.MenuRolDTO;
import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.dto.PageResponseDTO;
import com.essalud.gcpp.dto.PicklistDTO;
import com.essalud.gcpp.dto.ResponseLogin;
import com.essalud.gcpp.dto.UserDTO;
import com.essalud.gcpp.dto.UserUpdateDTO;
import com.essalud.gcpp.entidades.Users;
import com.essalud.gcpp.entidades.personal.Personal;



public interface IUserServices {
	public ResponseLogin login(String username, String password);
	public ResponseLogin changePassword(String newpass, Users u);
	public MenuGroup getPermits(Users user);
	public OperationResponse save(UserDTO user) ;
	public void cargaMasiva();
	public OperationResponse actualizarUser(UserDTO user);
	public OperationResponse deleteuser(int id);
	public ResponseLogin resetPassword(Integer id);
	public List<MenuRolDTO> getRolesMenu(String rol);
	public OperationResponse actualizarDatosUsuario(UserUpdateDTO user);
	/*public ResponseLogin acceptPrivacyPolicies(Users u);
	public void updateNewPasswords();
	public String test();
	
	// AUTHORITIES SISTEM
	public List<ModulePermissionsDTO> getUserModules(Users u);
	*/
	// USUARIOS
	public Page<Users>  getAllUsers(String filter,Pageable page);	
	public UserDTO searchUsers(Integer id);
	/*public OperationResponse assignPermissions(PermissionRequestDTO dto, Users u);
	
	*/
	// MODULOS Y MENUS
	public List<MenuDTO> getAllModules();
	/*public OperationResponse saveModule(MenuDTO dto, Users u);
	public OperationResponse saveMenu(MenuDTO dto, Users u);
	
	// SEGMENTOS
	public List<ModuleDetailsDTO> getAllModuleSegments();
	public List<ModuleDetailsDTO> getAllModuleFunctions();
	public OperationResponse saveSegment(SegmentDTO dto);
	public OperationResponse saveFunction(SegmentDTO dto);*/
	
	
	
}

