package com.essalud.gcpp.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "GCPP_ROL_MENU")
@AllArgsConstructor
public class MenuRol {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "rolmenu_seq")
	@SequenceGenerator(sequenceName = "AUTO_MENUROL" , allocationSize = 1,name ="rolmenu_seq" )
	private Integer id;
	private String role;
	private String codeMenu;

}
