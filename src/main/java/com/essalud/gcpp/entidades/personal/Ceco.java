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
@Table(name = "CECO")
public class Ceco implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long codred;
	private String codcen;
	@Id
	@Column(name = "ceco")
	private String codigo;
	@Column(name = "descripcion_ceco")
	private String descripcionCeco;
	@Column(name = "area_jerarquia")
	private String areaJerarquia;
	private String sociedad;
	private Long division;
	private String status;
	private String padre ;
	
}
