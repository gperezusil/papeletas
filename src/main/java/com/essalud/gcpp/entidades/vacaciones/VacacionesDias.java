package com.essalud.gcpp.entidades.vacaciones;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "GCPP_DIASVACACIONES")
public class VacacionesDias implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "vacd_seq")
	@SequenceGenerator(sequenceName = "INCR_DIASVACACIONES" , allocationSize = 1,name ="vacd_seq" )
	@Column(name = "ID_DVA")
	private Long idVacaciones;
	private String dni;
	private String periodo;
	private Integer dias;
	private Integer reprogramacion;
	

	
	
	
	


}
