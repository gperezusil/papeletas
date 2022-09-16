package com.essalud.gcpp.entidades.personal;

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
@Table(name = "GCPP_RESOLUCION")
public class Resolucion {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "reso_seq")
	@SequenceGenerator(sequenceName = "ID_AUTO_RESOLUCION" , allocationSize = 1,name ="reso_seq" )
	private Long id;
	private String dni;
	@Temporal(TemporalType.DATE)
	private Date fecha;
	@Column(name = "nombre_archivo")
	private String nombreArchivo;
	
	
	

}
