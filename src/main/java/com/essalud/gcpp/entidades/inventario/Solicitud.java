package com.essalud.gcpp.entidades.inventario;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GCPP_INVENTARIO_SOLICITUD")
public class Solicitud {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "sol_seq")
	@SequenceGenerator(sequenceName = "ID_SOLI_INVEN" , allocationSize = 1,name ="sol_seq" )
	private Integer id;
	
	@JoinColumn (name = "id_producto")
	@ManyToOne
	private Inventario producto;
	private Integer cantidadSol;
	private Integer cantEntr;
	
	private String dni;
	@Temporal(TemporalType.DATE)
	private Date fecha;
	private Integer estado;
	

}
