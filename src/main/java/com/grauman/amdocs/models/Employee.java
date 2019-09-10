package com.grauman.amdocs.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Employee {
	private Integer id;
	private Integer number;
	private String name;// Concatenate firstName+lastName
	private List<Role> roles;
	private String worksite;
	// concatenated with country
	private String department;
	
	public Employee(Integer id, Integer number, String name, String worksite,String department) {
		this.id = id;
		this.number = number;
		this.name = name;
		this.worksite = worksite;
		this.department = department;
	}

}
