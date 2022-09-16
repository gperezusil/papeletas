package com.essalud.gcpp.entidades.vacaciones;

import java.io.Serializable;
import java.util.Date;

public class Vacaciones2 implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	 private String dni;
	 private Date f_inicio;
	 private Date f_fin;
	 private Integer total_dias;
	 private String motivo;
	 private String carta;
	 private String estado;
	 
	 private String periodo;
	 
	 
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public Date getF_inicio() {
		return f_inicio;
	}
	public void setF_inicio(Date f_inicio) {
		this.f_inicio = f_inicio;
	}
	public Date getF_fin() {
		return f_fin;
	}
	public void setF_fin(Date f_fin) {
		this.f_fin = f_fin;
	}
	public Integer getTotal_dias() {
		return total_dias;
	}
	public void setTotal_dias(Integer total_dias) {
		this.total_dias = total_dias;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getCarta() {
		return carta;
	}
	public void setCarta(String carta) {
		this.carta = carta;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	 
	 

}
