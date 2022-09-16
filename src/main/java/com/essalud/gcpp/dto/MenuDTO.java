package com.essalud.gcpp.dto;

import java.util.ArrayList;
import java.util.List;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MenuDTO {
	private String id;
	private String title;
	private String icon;
	private String type; 
	private String url;
	private Integer order;
	private List<SubMenuDTO> children = new ArrayList<>();
}
