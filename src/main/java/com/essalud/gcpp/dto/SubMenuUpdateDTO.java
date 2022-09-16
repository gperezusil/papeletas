package com.essalud.gcpp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SubMenuUpdateDTO {
	
	private String code;
	private String user;
	private boolean selected;

}
