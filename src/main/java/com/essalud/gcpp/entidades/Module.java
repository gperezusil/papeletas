package com.essalud.gcpp.entidades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "GCPP_MODULE")
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
public class Module {
	@Id
	private String code;
	private String name;
	private String icon;
	private Integer mOrder;
	private boolean enable;
	private String type;
	
	@OneToMany(
	        mappedBy = "module",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	private List<Menu> menus = new  ArrayList<>();


}
