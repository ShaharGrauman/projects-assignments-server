package com.grauman.amdocs.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResetPasswordVM {
	private String email;
	private int employeeNumber;
}
