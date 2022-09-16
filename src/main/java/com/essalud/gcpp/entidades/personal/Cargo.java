package com.essalud.gcpp.entidades.personal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CARGO")
public class Cargo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String codcargo;
	@Column(name = "descripcion_plaza")
	private String descripcionPlaza;
	private String descripcionBasica;
	private String descripcionProfesion;

	
	
	
	


}
