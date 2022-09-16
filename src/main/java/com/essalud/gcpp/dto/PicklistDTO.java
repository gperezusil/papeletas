package com.essalud.gcpp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PicklistDTO {
	public int id;
	public String code;
	public String name;
	public String filter;
}
