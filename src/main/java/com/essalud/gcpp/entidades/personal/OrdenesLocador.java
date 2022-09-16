package com.essalud.gcpp.entidades.personal;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "GCPP_ORDENES_LOCADORES")
public class OrdenesLocador implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "orl_seq")
	@SequenceGenerator(sequenceName = "ID_AUTO_GCPP_ORDENES" , allocationSize = 1,name ="orl_seq" )
	private Long id;
	private String ruc; 
	@Column(name = "n_orden")
	private String nOrden; 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_inicio")
	private Date fechaInicio; 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_fin")
	private Date fechaFin; 
	private boolean estado;
	@Column(name = "Monto")
	private Double sueldo;
	
	
	
	
	
	
}
