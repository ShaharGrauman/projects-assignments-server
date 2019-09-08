package com.grauman.amdocs.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Login {
	
	private String username;
	private String password;
	
}
