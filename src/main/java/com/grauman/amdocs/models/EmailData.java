package com.grauman.amdocs.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailData {

	private String toEmail;
	private String firstName;
	private String subject;
	private String text;
}
