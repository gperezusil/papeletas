package com.essalud.gcpp.entidades;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CONFIGURACIONANNO")
public class ConfiguracionAnno {
	@Id
	private Integer id;
	private String anno;
}
