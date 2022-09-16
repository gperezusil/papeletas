package com.essalud.gcpp.dto.inventario;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SolicitudInventarioDTO {

	private Integer producto;
	private Integer cantidad;
	
}
