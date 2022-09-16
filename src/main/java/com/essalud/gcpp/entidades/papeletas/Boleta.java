package com.essalud.gcpp.entidades.papeletas;


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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "GCPP_BOLETAS")
public class Boleta {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "bo_seq")
	@SequenceGenerator(sequenceName = "ID_AUTO_BOLETA" , allocationSize = 1,name ="bo_seq" )
	private Long idBo;
	private String dni;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechInicio;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechFin;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechPedido;
	private String motivo;
	private String estado;
	private String archivoBoleta;
	private String metodo;
	
	@JoinColumn (name = "dni", nullable = false , insertable = false, updatable = false)
	@ManyToOne
	private Personal personal;
	
	
		
	
	
	
}

