package com.essalud.gcpp.entidades;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class UserFunctions{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ro_seq")
	@SequenceGenerator(sequenceName = "USERFUNCTION" , allocationSize = 1,name ="ro_seq" )
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_user")
	private Users user;
	
	private String role;
	private String accessLvl;
	
}
