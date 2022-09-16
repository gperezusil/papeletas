package com.essalud.gcpp.entidades.inventario;

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
@Table(name = "GCPP_ORDEN_PROVEEDOR")
public class OrdenProveedor {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "or_seq")
	@SequenceGenerator(sequenceName = "ID_ORDENPROVEEDOR" , allocationSize = 1,name ="or_seq" )
	private Integer id;
	private String rucProveedor;
	private String nombreProveedor;
	private String contacto;
	private String nota;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	private String archivoGuia;
	private String guiaRemision;
	private String facturaProveedor;
	private String archivoFactura;
	private Integer cantTotal;
	private Integer montoTotal;


}
