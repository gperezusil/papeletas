package com.essalud.gcpp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestLogin {
	private String username;
	private String password;
}
