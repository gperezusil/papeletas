package com.essalud.gcpp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MenuGroup {
	private String id;
	private String title;
	private String type;
	private List<MenuDTO> children;

}
