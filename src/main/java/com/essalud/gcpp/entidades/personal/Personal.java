package com.essalud.gcpp.entidades.personal;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "GCPP_PERSONAL")
public class Personal implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String dni;
	@Column(name = "codpllas")
	private String codigoPlanilla;
	private String coningreso;
	private String apenombres;
	private String codcargo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "f_ingreso")
	private Date fIngreso;
	private String nivel;
	private String cod00;
	private String profesion;
	@Column(name = "numero_plaza")
	private String numeroPlaza;
	private String anexo;
	private String celular;
	@Temporal(TemporalType.DATE)
	@Column(name = "fech_naci")
	private Date fechNaci;
	private String correo;
	@Column(name = "estado_plaza")
	private boolean estadoPlaza; 
	private String cargo;
	private String correoPer;
			
	
	
	
	

}
