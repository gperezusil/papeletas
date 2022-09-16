package com.essalud.gcpp.controller.personal;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.essalud.gcpp.entidades.personal.Cargo;
import com.essalud.gcpp.entidades.personal.Ceco;
import com.essalud.gcpp.entidades.personal.CentroCosto;
import com.essalud.gcpp.service.personal.ICecoService;





@RestController
@RequestMapping("/api/personal")
public class CecoController {
	
	@Autowired
	private ICecoService cecoService;
	
	@GetMapping("/listarcentro")
	public List<CentroCosto> listarCentro(){
		return cecoService.listarCentroCosto();
	}
	
	@GetMapping("/listarsubgerencia")
	public List<Ceco> listarSubgerencia(){
		return cecoService.listarSubgerencia();
	}
	
	
	@GetMapping("/listarcargos")
	public List<Cargo> listarcargos(@RequestParam String filtro){
		return cecoService.listarCargos(filtro);
	}
	@GetMapping("/selecargo")
	public Cargo selectCargo(@RequestParam Optional<String> cod){
		return cecoService.findByCod(cod.orElse(""));
	}
	

}
