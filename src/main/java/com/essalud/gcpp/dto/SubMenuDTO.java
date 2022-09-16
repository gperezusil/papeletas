package com.essalud.gcpp.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SubMenuDTO {
	
	private String id;
	private String title;
	private String type;
	private String url;
	private String user;
	private boolean enable;

}
