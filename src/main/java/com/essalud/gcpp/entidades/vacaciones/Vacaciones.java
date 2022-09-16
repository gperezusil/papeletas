package com.essalud.gcpp.entidades.vacaciones;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
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
@Table(name = "GCPP_HISTORIAL_VACACIONES")
public class Vacaciones implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "vac_seq")
	@SequenceGenerator(sequenceName = "INCRE_HIS_VACA" , allocationSize = 1,name ="vac_seq" )
	@Column(name = "id")
	private Long idVa;
	private String dni;
	private Date fInicio;
	private Date fFin;
	private int validacionSubge;
	private int validacionGere;
	private String estado;
	private String motivo;
	private String codSubge;
	private String codGere;
	@Column(name = "id_dva")
	private String idDva;
	private String carta;
	private String tipo;
	private String revisado;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaSolicitud;
	
	@JoinColumn (name = "dni", nullable = false , insertable = false, updatable = false)
	@ManyToOne
	private Personal personal;
	
	@JoinColumn (name = "ID_DVA", nullable = false , insertable = false, updatable = false)
	@ManyToOne
	private VacacionesDias vperiodo;
	
	private Integer intentos;
	
	
	
	public String getEstado() {
		switch(estado) {
		case "0": estado="Pendiente de Aprobacion"; break;
		case "1": estado="Pendiente de Ejecutar";break;			
		case "2": estado="Denegado"; break;
		case "3": estado="Reprogramado"; break;
		case "4": estado="Ejecutado";break;
		}
		return estado;
	}
	
}
