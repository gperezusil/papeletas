package com.essalud.gcpp.entidades.descanso;

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
@Table(name = "GCPP_DESCANSO_MEDICO")
public class DescansoMedico {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "descanso_seq")
	@SequenceGenerator(sequenceName = "AUTO_DESCANSO" , allocationSize = 1,name ="descanso_seq" )
	private Integer id;
	@JoinColumn (name = "dni")
	@ManyToOne
	private Personal personal;
	private String nCita;
	@Temporal(TemporalType.DATE)
	private Date fechaInicio;
	@Temporal(TemporalType.DATE)
	private Date fechaFin;
	private String archivo;
	private String createBy;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createAt;
	

}
