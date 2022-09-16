package com.essalud.gcpp.entidades.personal;


import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "GCPP_LOCADORES")
public class Locador  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String ruc;
	@Column(name = "razon_social")
	private String razonSocial;
	private String ceco;
	private String profesion;
	private String celular;
	private String correo;
	private String foto;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ruc" )
	private List<OrdenesLocador> ordenes;
	
	
	
	

}
