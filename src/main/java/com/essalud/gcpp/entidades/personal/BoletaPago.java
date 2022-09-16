package com.essalud.gcpp.entidades.personal;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GCPP_BOLETA_PAGO")
public class BoletaPago {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "bopa_seq")
	@SequenceGenerator(sequenceName = "ID_BOLETA_PAGO" , allocationSize = 1,name ="bopa_seq" )
	private Integer id;
	private String dni;
	private String anno;
	private String mes;
	private String nombreArchivo;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;

}
