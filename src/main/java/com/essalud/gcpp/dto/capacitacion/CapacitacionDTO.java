package com.essalud.gcpp.dto.capacitacion;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CapacitacionDTO {
	private String nombreEntidad;
	private String curso;
	private Date fecha;
	private List<String> dni;

}
