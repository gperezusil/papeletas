package com.essalud.gcpp.entidades;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "GCPP_MENU")
@AllArgsConstructor
public class Menu {
	@Id
	private String code;
	private String name;
	private String icon;
	private String link;
	private Integer mOrder;
	private String type;
	private boolean enable;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "code_module")
	private Module module;
	private String mUser;

}
