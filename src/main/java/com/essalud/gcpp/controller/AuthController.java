package com.essalud.gcpp.controller;




import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.essalud.gcpp.dto.MenuDTO;
import com.essalud.gcpp.dto.MenuGroup;
import com.essalud.gcpp.dto.MenuRolDTO;
import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.dto.PageResponseDTO;
import com.essalud.gcpp.dto.PicklistDTO;
import com.essalud.gcpp.dto.RequestLogin;
import com.essalud.gcpp.dto.ResponseLogin;
import com.essalud.gcpp.dto.SubMenuDTO;
import com.essalud.gcpp.dto.SubMenuUpdateDTO;
import com.essalud.gcpp.dto.UserDTO;
import com.essalud.gcpp.dto.UserFunctionDTO;
import com.essalud.gcpp.dto.UserUpdateDTO;
import com.essalud.gcpp.entidades.Users;
import com.essalud.gcpp.entidades.personal.Personal;
import com.essalud.gcpp.service.auth.IUserFunctionService;
import com.essalud.gcpp.service.auth.IUserServices;



@RestController
public class AuthController {
	
	//@Autowired private BCryptPasswordEncoder encoder;
	@Autowired private IUserServices userServ;
	@Autowired private IUserFunctionService userfunction;
	
	@GetMapping("/menus")
	public MenuGroup getMenus(Authentication auth) {
		Users u = (Users) auth.getPrincipal();
		return  userServ.getPermits(u);
	}
	
	@PostMapping("/login")
	public ResponseLogin login(@RequestBody RequestLogin loginData) {
		return  userServ.login(loginData.getUsername(), loginData.getPassword());
	}
	
	@PostMapping("/saveRol")
	public UserFunctionDTO agregarRol(@RequestBody UserFunctionDTO userfuction) {
		return  userfunction.save(userfuction);
	}
	
	@GetMapping("/getRoles/{dni}")
	public List<UserFunctionDTO> obtenerRoles(@PathVariable String dni) {
		return  userfunction.getRoles(dni);
	}
	
	@DeleteMapping("/deleteRol/{rol}/{dni}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable String rol,@PathVariable String dni) {
		userfunction.deleteRol(rol,dni);
	}
	
	@PostMapping("/newuser")
	public OperationResponse newUser(@RequestBody UserDTO u) {
		
		return  userServ.save(u);
	}
	@PutMapping("/updatefirst")
	public OperationResponse updateUserFirst(@RequestBody UserUpdateDTO u) {
		
		return  userServ.actualizarDatosUsuario(u);
	}
	
	@PutMapping("/updateUser")
	public OperationResponse updateUser(@RequestBody UserDTO user) {
		
		return  userServ.actualizarUser(user);
	}
	@DeleteMapping("/deleteuser")
	public OperationResponse deleteUser(@RequestParam int id) {
		
		return  userServ.deleteuser(id);
	}
	@PostMapping("/updaterolmenu")
	public OperationResponse updaterolmenu(@RequestBody List<SubMenuUpdateDTO> menu) {
		
		return  userfunction.actualizarRolMenu(menu);
	}
	
	/*@GetMapping("/acceptpolicies")
	public ResponseLogin acceptPolicies(Authentication auth) {
		Users u = (Users) auth.getPrincipal();
		return  userServ.acceptPrivacyPolicies(u);
	}
	*/
	@PostMapping("/changepassword")
	public ResponseLogin changePassword(@RequestBody  RequestLogin contra, Authentication auth) {
		Users u = (Users) auth.getPrincipal();
		return  userServ.changePassword(contra.getPassword(), u);
	}
	@GetMapping("/resetpassword")
	public ResponseLogin resetPassword(@RequestParam  Integer id) {

		return  userServ.resetPassword(id);
	}

	@GetMapping("/test")
	public void test() {		
		userServ.cargaMasiva();
	}

	// PERMISSIONS
	
	/*@GetMapping("/appmods")
	public List<ModulePermissionsDTO> getWebModules(Authentication auth){
		Users u = (Users) auth.getPrincipal();
		return userServ.getUserModules(u);
	}*/
	
	// USUARIOS
	@GetMapping("/users")
	public Page<Users> getUsers(
			@RequestParam int page, 
			@RequestParam Optional<String> filter){
		System.out.println(filter.orElse("hola"));
		Pageable pageable = PageRequest.of(page,15,Sort.by("displayName").ascending());
		return userServ.getAllUsers(filter.orElse(""), pageable);
	}
	// BUSCADOR DE USUARIOS
	@GetMapping("/search/users")
	public UserDTO searchUsers(@RequestParam Integer id){
		return userServ.searchUsers(id);
	}
	
	// LISTA DE MODULOS CON MENUS
	@GetMapping("/modules")
	public List<MenuDTO> getAllModules(){
		return userServ.getAllModules();
	}
	@GetMapping("/rolesmenu")
	public List<MenuRolDTO> getRolesMenu(@RequestParam String rol){
		return userServ.getRolesMenu(rol);
	}
/*
	// LISTA DE MODULOS CON FUNCIONES
	@GetMapping("/modulefunctions")
	public List<ModuleDetailsDTO> getAllModulesFunctions(){
		return userServ.getAllModuleFunctions();
	}
	
	// LISTA DE MODULOS CON SEGMENTOS
	@GetMapping("/modulesegments")
	public List<ModuleDetailsDTO> getAllModulesSegments(){
		return userServ.getAllModuleSegments();
	}
	
	// AGREGAR O EDITAR MODULO
	@PostMapping("/module")
	public OperationResponse saveModule(@RequestBody MenuDTO dto, Authentication auth) {
		Users u = (Users) auth.getPrincipal();
		return userServ.saveModule(dto, u);
	}
	
	// AGREGAR O EDITAR MODULO
	@PostMapping("/addpermissions")
	public OperationResponse addUserPermissions(@RequestBody PermissionRequestDTO dto, Authentication auth) {
		Users u = (Users) auth.getPrincipal();
		return userServ.assignPermissions(dto, u);
	}
	
	// AGREGAR O EDITAR MENÚ
	@PostMapping("/menu")
	public OperationResponse saveMenu(@RequestBody MenuDTO dto, Authentication auth) {
		Users u = (Users) auth.getPrincipal();
		return userServ.saveMenu(dto, u);
	}
	
	// AGREGAR O EDITAR FUNCIÓN
	@PostMapping("/modulefunctions")
	public OperationResponse saveModuleFunction(@RequestBody SegmentDTO dto, Authentication auth) {
		return userServ.saveFunction(dto);
	}
		
	// AGREGAR O EDITAR SEGMENTO
	@PostMapping("/modulesegments")
	public OperationResponse saveModuleSegment(@RequestBody SegmentDTO dto, Authentication auth) {
		return userServ.saveSegment(dto);
	}*/
	
}
