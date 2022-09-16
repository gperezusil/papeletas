package com.essalud.gcpp.entidades.inventario;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "GCPP_INVENTARIO")
public class Inventario {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "in_seq")
	@SequenceGenerator(sequenceName = "AUTOINCREMENT_INVENTARIO" , allocationSize = 1,name ="in_seq" )
	private Integer id;
	private String nombre;
	private String descripcion;
	private Integer stockActual;
	private Integer stockMin;
	private String marca;
	
	
	
	

}
