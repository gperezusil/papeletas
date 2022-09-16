package com.essalud.gcpp.entidades.inventario;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "GCPP_DETALLE_ORDEN")
public class DetalleOrdenProveedor {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "deo_seq")
	@SequenceGenerator(sequenceName = "ID_DETALLE_ORDEN" , allocationSize = 1,name ="deo_seq" )
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "id_orden")
	private OrdenProveedor orden;
	@ManyToOne
	@JoinColumn(name = "id_producto")
	private Inventario producto;
	private Integer cantidad;
	private Integer monto;

}
