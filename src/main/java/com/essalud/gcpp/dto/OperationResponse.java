package com.essalud.gcpp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OperationResponse {
	private boolean status;
	private String message;
	private String previous;
	
	public OperationResponse(boolean status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	
}
