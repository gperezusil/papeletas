package com.essalud.gcpp.entidades.capacitacion;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.essalud.gcpp.entidades.personal.Personal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GCPP_CAPACITACIONES")
public class Capacitacion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ca_seq")
	@SequenceGenerator(sequenceName = "ID_CAPACITACION" , allocationSize = 1,name ="ca_seq" )
	private Long id;
	
	@JoinColumn (name = "dni")
	@ManyToOne
	private Personal personal;
	
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	private String entidad;
	
	private String nombreCurso;
	
	
	
	

}
