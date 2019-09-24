package com.grauman.amdocs.models;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeData {
	private Employee employee;
	private String managerName;
	private Date lastLogin;
	private List<Role> roles;
	
	public EmployeeData(Employee employee) {
		this.employee=employee;
	}
	public EmployeeData(Employee employee,String managerName) {
		this(employee);
		this.managerName = managerName;
	}
	public EmployeeData(Employee employee,List<Role>roles) {
		this(employee);
		this.roles=roles;
	}
	public EmployeeData(Employee employee,String managerName,List<Role>roles) {
		this.employee=employee;
		this.managerName=managerName;
		this.roles=roles;
	}



}
