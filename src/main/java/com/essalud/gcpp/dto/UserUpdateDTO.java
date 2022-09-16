package com.essalud.gcpp.dto;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserUpdateDTO {
	private int id;
	private String dni;
	private String gender;
	private Date fechaNacimiento;
	private String celular;
	private String profesion;
	private String correoInstitucional;
	private String correoPersonal;
	private String anexo;
	

}
