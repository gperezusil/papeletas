package com.essalud.gcpp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
	private int id;
	private String username;
	private String displayname;
	private String subtitle;
	private String gender;
	private String area;
	private String regimen;
	private boolean enable;
	private String role;
}
