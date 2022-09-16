package com.essalud.gcpp.dto.papeletas;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeconDashDTO {
	
	private String name;
	private List<DashPapeletaDTO> series;

}
