package com.essalud.gcpp.service.auth.impl;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.essalud.gcpp.dto.MenuDTO;
import com.essalud.gcpp.dto.MenuGroup;
import com.essalud.gcpp.dto.MenuRolDTO;
import com.essalud.gcpp.dto.OperationResponse;
import com.essalud.gcpp.dto.RequestLogin;
import com.essalud.gcpp.dto.ResponseLogin;
import com.essalud.gcpp.dto.SubMenuDTO;
import com.essalud.gcpp.dto.UserDTO;
import com.essalud.gcpp.dto.UserUpdateDTO;
import com.essalud.gcpp.entidades.Menu;
import com.essalud.gcpp.entidades.Module;
import com.essalud.gcpp.entidades.UserFunctions;
import com.essalud.gcpp.entidades.Users;
import com.essalud.gcpp.entidades.personal.Personal;
import com.essalud.gcpp.repository.IUserFunctionsRepository;
import com.essalud.gcpp.repository.IUserRepository;
import com.essalud.gcpp.repository.MenuRepository;
import com.essalud.gcpp.repository.MenuRolRepository;
import com.essalud.gcpp.repository.ModuleRepository;
import com.essalud.gcpp.repository.personal.IEmpleadoDao;
import com.essalud.gcpp.service.auth.IUserServices;
import com.essalud.gcpp.util.JWTUtil;

@Service
public class UserServiceImp implements IUserServices {

	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private IUserRepository userRepo;
	@Autowired
	private IEmpleadoDao personalRepository;
	@Autowired
	private IUserFunctionsRepository userFunctionRepository;
	@Autowired
	private ModuleRepository moduleRepo;
	@Autowired
	private MenuRepository menuRepo;
	
	@Autowired
	private MenuRolRepository menuRolRepo;

	/*
	 * @Autowired private ISegmentRepository segrepo;
	 * 
	 * @Autowired private IFunctionRepository funcrepo;
	 */
	@Override
	public ResponseLogin login(String username, String password) {
		Users user = userRepo.findByUsername(username);
		if (user != null && encoder.matches(password, user.getPassword())) {
			if (user.isEnable()) {
				String token = jwtUtil.generateToken(user);
				return new ResponseLogin(true, token, "OK");
			} else {
				return new ResponseLogin(false, null, "Usuario inactivo");
			}
		} else {
			return new ResponseLogin(false, null, "Credenciales incorrectas");
		}
	}

	@Override
	public MenuGroup getPermits(Users user) {
		
		List<Menu> menus = menuRepo.getMenusRol(user.getAuthorities().stream().toArray()[0].toString());
		List<MenuDTO> modules = new ArrayList<>();

		MenuGroup group = new MenuGroup();
		MenuDTO home = new MenuDTO("home", "Home", "home", "item", "/sample",0, new ArrayList<>());
		modules.add(home);
		// Reconocer los padres
		for (Menu m : menus) {
			if (modules.stream().noneMatch(p -> p.getId().equals(m.getModule().getCode())))
				modules.add(new MenuDTO(m.getModule().getCode(), m.getModule().getName(), m.getModule().getIcon(),
						m.getModule().getType(),null,m.getModule().getMOrder(), new ArrayList<>()));
		}

		// Agregar hijos
		for (MenuDTO module : modules) {
			module.getChildren()
					.addAll(
							menus.stream()
							.filter(p -> p.getModule().getCode().equals(module.getId()))
							.sorted(Comparator.comparing(Menu::getMOrder))
							.map(m -> new SubMenuDTO(m.getCode(), m.getName(), m.getType(), m.getLink(),m.getMUser(),true))
							.collect(Collectors.toList()));
		}
		group.setId("aplications");
		group.setTitle("Menu");
		group.setType("group");
		group.setChildren(modules);
		return group;
	}

	@Override
	public OperationResponse save(UserDTO user) {
		OperationResponse response= null;
		try {
			Users u = new Users(user.getUsername(), encoder.encode(user.getUsername()), null, false, user.getDisplayname(), user.getArea(), 
					user.getRegimen(), user.getRole(), null, 0, null, user.getGender(), 
					false, false, null);
			u.setCreatedBy(u.getUsername());
			u.setCreatedAt(new Date());
			u= userRepo.save(u);
			userFunctionRepository.save(new UserFunctions(null, u, user.getRole(), null));
			 response = new OperationResponse(true, "Exito al crear");
		} catch (Exception e) {
			response = new OperationResponse(false, "Error al crear , ya existe el dni");
		}
		
		 return response;
	}

	@Override
	public void cargaMasiva() {

		personalRepository.findAll().forEach(per -> {
			Users u = new Users();
			u.setUsername(per.getDni());
			u.setPassword(encoder.encode(per.getDni()));
			u.setDisplayName(per.getApenombres());
			u.setArea(per.getCod00());
			u.setRegimen(per.getConingreso());
			u.setSubtitle("USER");
			u.setEnable(per.isEstadoPlaza());
			u.setCreatedBy("72524812");
			u.setCreatedAt(new Date());
			userRepo.save(u);
			UserFunctions roles = new UserFunctions(null, u, "ROLE_USER", null);
			userFunctionRepository.save(roles);
		});
	}

	@Override
	public ResponseLogin changePassword(String newpass, Users u) {
		try {
			u.setPassword(encoder.encode(newpass));
			userRepo.save(u);
			return new ResponseLogin(true, null, "La contraseña se cambió correctamente.");

		} catch (Exception e) {
			return new ResponseLogin(false, null, "Se produjo un error inesperado.");
		}
	}

	@Override
	public OperationResponse actualizarUser(UserDTO user) {
		
		OperationResponse response = null;
		try {
			Users u = userRepo.findById(user.getId())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			
			
			u.setArea(user.getArea());
			u.setDisplayName(user.getDisplayname());
			u.setEnable(user.isEnable());
			u.setGender(user.getGender());
			u.setRegimen(user.getRegimen());
			UserFunctions role = userFunctionRepository.findByUser(u)
					.orElse(new UserFunctions(null, u, user.getRole(), null));
			role.setRole(user.getRole());
			userRepo.save(u);
			userFunctionRepository.save(role);
			response = new OperationResponse(true, "Exito al actualizar");
		} catch (Exception e) {
			response = new OperationResponse(false, e.getMessage());
		}

		return response;
	}

	@Override
	public ResponseLogin resetPassword(Integer id) {
		Users u = userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		try {
			u.setPassword(encoder.encode(u.getUsername()));
			userRepo.save(u);
			return new ResponseLogin(true, null, "La contraseña se cambió correctamente.");

		} catch (Exception e) {
			return new ResponseLogin(false, null, "Se produjo un error inesperado.");
		}
	}
	/*
	 * @Override public ResponseLogin acceptPrivacyPolicies(Users u) {
	 * u.setAcceptedPolicies(true); u = userRepo.save(u); return new
	 * ResponseLogin(true, jwtUtil.generateToken(u), "Cambio de token correcto") ; }
	 * 
	 * @Override public void updateNewPasswords() { List<Users> users =
	 * userRepo.findByBusinessUnit("BU00010"); users.forEach(u -> {
	 * u.setPassword(encoder.encode(u.getPassword())); userRepo.save(u); }); }
	 * 
	 * @Override public String test() { Users u = userRepo.getOne(1); return
	 * "El usuario" + u.getDisplayName() + "tiene como IDSSFF" + u.getIdssff(); }
	 * 
	 * @Override public List<ModulePermissionsDTO> getUserModules(Users u) {
	 * List<ModulePermissionsDTO> response = new ArrayList<>(); List<Modules>
	 * modules = moduleRepo.findAll();
	 * 
	 * for (Modules m: modules){ response.add( new ModulePermissionsDTO(
	 * m.getCode(), m.getName(), m.getSegments().stream() .filter(p-> p.isEnable())
	 * .map(s -> new PicklistDTO(0, s.getCode(),
	 * s.getName(),null)).collect(Collectors.toList()), m.getFunctions().stream()
	 * .filter(p-> p.isEnable()) .map(s -> new PicklistDTO( 0, s.getCode(),
	 * s.getName(),null)).collect(Collectors.toList()), m.getMenus().stream()
	 * .filter(p-> p.isEnable())
	 * .sorted(Comparator.comparing(ModuleMenus::getOrderm)) .map(s -> new
	 * PicklistDTO( 0, s.getCode(),
	 * s.getName(),s.getIcon())).collect(Collectors.toList()) ) ); }
	 * 
	 * return response; }
	 * */
	@Override
	public Page<Users> getAllUsers(String filter,Pageable page) {
		
		return userRepo.getListUsers("%"+filter+"%",page);
	}
	 

	@Override
	public List<MenuDTO> getAllModules() {
		List<Module> modules = moduleRepo.findAll();
		List<Menu> menus = menuRepo.findAll();
		return modules.stream()
				.map(m -> new MenuDTO(m.getCode(), m.getName(), m.getIcon(), m.getType(), null,m.getMOrder(),
						menus.stream().filter(p -> p.getModule().getCode().equals(m.getCode()))
								.map(x -> new SubMenuDTO(x.getCode(), x.getName(), x.getType(), x.getLink(),x.getMUser(),true))
								.collect(Collectors.toList())))
				.collect(Collectors.toList());

	}

	@Override
	public List<MenuRolDTO> getRolesMenu(String rol) {
		
		return menuRolRepo.findByRole(rol).stream().map(c->new MenuRolDTO(c.getRole(),c.getCodeMenu())).collect(Collectors.toList());
	}

	/*
	 * @Override public OperationResponse saveModule(MenuDTO dto, Users u) { Modules
	 * xm = moduleRepo.findById(dto.getCode()).orElse(null); Modules m; // AGREGAR
	 * if (dto.getModule().equals("ADD")) { if (xm != null) { return new
	 * OperationResponse(false, "Este código ya está en uso"); } m = new
	 * Modules(dto.getCode(), dto.getText(), dto.getIcon(), dto.getOrder(), true, 0,
	 * 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()); // EDITAR }else
	 * { if (xm == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND); m
	 * = xm;
	 * 
	 * m.setName(dto.getText()); m.setEnable(dto.isEnable());
	 * m.setIcon(dto.getIcon()); m.setOrder(dto.getOrder()); } try {
	 * moduleRepo.save(m); return new OperationResponse(true, "OK"); } catch
	 * (Exception e) { return new OperationResponse(false, e.getMessage()); } }
	 * 
	 * 
	 * @Override public OperationResponse saveMenu(MenuDTO dto, Users u) { //
	 * DEFINIENDO CÓDIGO if (dto.getCode() == null) { Modules m =
	 * moduleRepo.findById(dto.getModule()).orElseThrow(()-> new
	 * ResponseStatusException(HttpStatus.NOT_FOUND) );
	 * m.setAutoincmenu(m.getAutoincmenu() + 1); m = moduleRepo.save(m);
	 * dto.setCode( m.getCode() + "-" + String.format("%03d", m.getAutoincmenu() ));
	 * }
	 * 
	 * ModuleMenus mm = new ModuleMenus(dto.getCode(), dto.getText(), dto.getIcon(),
	 * dto.getLink(), dto.getOrder(), "CHILD", moduleRepo.getOne(dto.getModule()) ,
	 * dto.isEnable()); try { menuRepo.save(mm); return new OperationResponse(true,
	 * "OK"); } catch (Exception e) { return new OperationResponse(false,
	 * e.getMessage()); } }
	 * 
	 * @Override public List<ModuleDetailsDTO> getAllModuleSegments() {
	 * List<Modules> modules = moduleRepo.findAll(); List<ModuleSegments> segments =
	 * segrepo.findAll();
	 * 
	 * return modules.stream() .map( m->new ModuleDetailsDTO( m.getCode(),
	 * m.getName(), m.getOrder(), m.getIcon(), m.isEnable(),
	 * segments.stream().filter( p-> p.getModule().equals( m.getCode() )) .map( x ->
	 * new SegmentDTO( x.getCode(), x.isEnable(), x.getName(), x.getDatafilter(),
	 * x.getModule() ) ) .collect( Collectors.toList() ), null) ) .collect(
	 * Collectors.toList() ); }
	 * 
	 * @Override public List<ModuleDetailsDTO> getAllModuleFunctions() {
	 * List<Modules> modules = moduleRepo.findAll(); List<ModuleFunctions> functions
	 * = funcrepo.findAll(); return modules.stream() .map( m->new ModuleDetailsDTO(
	 * m.getCode(), m.getName(), m.getOrder(), m.getIcon(), m.isEnable(),
	 * functions.stream().filter( p-> p.getModule().equals( m.getCode() )) .map( x
	 * -> new SegmentDTO( x.getCode(), x.isEnable(), x.getName(), null,
	 * x.getModule() ) ) .collect( Collectors.toList() ), null) ) .collect(
	 * Collectors.toList() ); }
	 * 
	 * @Override public OperationResponse saveSegment(SegmentDTO dto) { //
	 * DEFINIENDO CÓDIGO if (dto.getCode() == null) { Modules m =
	 * moduleRepo.findById(dto.getModule()).orElseThrow( ()-> new
	 * ResponseStatusException(HttpStatus.NOT_FOUND) );
	 * m.setAutoincseg(m.getAutoincseg() + 1); m = moduleRepo.save(m); dto.setCode(
	 * m.getCode() + "-" + String.format("%03d", m.getAutoincseg() )); }
	 * 
	 * ModuleSegments ms = new ModuleSegments(dto.getCode(), dto.isEnable(),
	 * dto.getName(), dto.getDatafilter(), dto.getModule()); try { segrepo.save(ms);
	 * return new OperationResponse(true, "OK"); } catch (Exception e) { return new
	 * OperationResponse(false, e.getMessage()); } }
	 * 
	 * @Override public OperationResponse saveFunction(SegmentDTO dto) {
	 * ModuleFunctions mf = new ModuleFunctions(dto.getCode(), dto.isEnable(),
	 * dto.getName(), dto.getModule()); try { funcrepo.save(mf); return new
	 * OperationResponse(true, "OK"); } catch (Exception e) { return new
	 * OperationResponse(false, e.getMessage()); } }
	 * */
	@Override
	public UserDTO searchUsers(Integer id) {
		Users u = userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return new UserDTO(u.getId(), u.getUsername(), u.getDisplayName(), u.getSubtitle(), u.getGender(),
				u.getArea(), u.getRegimen(), u.isEnabled(),userFunctionRepository.findByUser(u).orElse(new UserFunctions()).getRole());
	}
	 /* 
	 * @Override public OperationResponse assignPermissions(PermissionRequestDTO
	 * dto, Users u) {
	 * 
	 * for (int usid:dto.getUsers()) { // MENUS for (String mcode : dto.getMenus())
	 * { menuRepo.addUserMenu(usid, mcode); } // TARGETS for (String scode:
	 * dto.getSegments()) { segrepo.addUserSegment(usid, scode); } // FUNCIONES for
	 * (RoleSelectorDTO rs: dto.getFunctions()) { funcrepo.addUserFunction(usid,
	 * rs.getCode(), rs.getLvl()); } } return new OperationResponse(true, null);
	 * 
	 * }
	 */

	@Override
	@Transactional
	public OperationResponse deleteuser(int id) {
		OperationResponse response =null;
		try {
			Users u = userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); 
			userFunctionRepository.deleteByUser(u);
			userRepo.deleteById(id);
			response = new OperationResponse(true, "Exito al eliminar");
		} catch (Exception e) {
			response = new OperationResponse(false,"Error al eliminar");
		}
		return response;
	}

	@Override
	public OperationResponse actualizarDatosUsuario(UserUpdateDTO user) {
		OperationResponse response = null ;
		Users u = userRepo.findById(user.getId()).orElse(null);
		if(u!=null) {
			u.setGender(user.getGender());
			u.setAcceptedPolicies(true);
			userRepo.save(u);
			Personal personal = personalRepository.findByDni(user.getDni());
			if(personal!=null) {
				personal.setFechNaci(user.getFechaNacimiento());
				personal.setCelular(user.getCelular());
				personal.setProfesion(user.getProfesion());
				personal.setCorreo(user.getCorreoInstitucional());
				personal.setCorreoPer(user.getCorreoPersonal());
				personal.setAnexo(user.getAnexo());
				personalRepository.save(personal);
			}
			response= new OperationResponse(true, "Exito al actualizar");
		}else {
			response= new OperationResponse(false, "Error al actualizar");
		}
	
		return response;
	}

}
