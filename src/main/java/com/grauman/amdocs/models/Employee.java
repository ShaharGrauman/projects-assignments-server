package com.grauman.amdocs.models;

import java.sql.Date;

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
public class Employee {
	private Integer id;
	private Integer number;
	private String firstName;
	private String lastName;
	private String email;
	private Integer managerId;
	private String department;
	private WorkSite worksite;
	private Country country;
	private String phone;
	private Boolean loginStatus;
	private Boolean locked;
	private Boolean deactivated;
	private String password;
	private String image;
	
	public Employee(Integer id) {
		this.id=id;
	}
	public Employee(Integer id, String firstName) {
		this(id);
		this.firstName = firstName;
	}
	public Employee(Integer id,Integer number,String firstName,String lastName) {
		this.id=id;
		this.number=number;
		this.firstName=firstName;
		this.lastName=lastName;
	}
	public Employee(Integer id,Integer number,String firstName,String lastName,String department,WorkSite worksite) {
		this(id,number,firstName,lastName);
		this.department=department;
		this.worksite=worksite;
	}
	public Employee(Integer id,Integer number,String firstName,String lastName,String department,WorkSite worksite,Country country) {
		this(id,number,firstName,lastName,department,worksite);
		this.country=country;
	}
	public Employee(Integer id,Integer number,String firstName,String lastName,Integer managerId,String department,
			WorkSite worksite,Country country,Boolean locked,Boolean deactivated) {
		this(id,number,firstName,lastName,department,worksite,country);
		this.managerId=managerId;
		this.locked=locked;
		this.deactivated=deactivated;
	}
	public Employee(Integer id,Integer number,String firstName,String lastName,String email,Integer managerId,
			String department,WorkSite worksite,String phone,Boolean loginStatus,Boolean locked,Boolean deactivated) {
		this.id=id;
		this.number=number;
		this.firstName=firstName;
		this.lastName=lastName;
		this.email=email;
		this.managerId=managerId;
		this.department=department;
		this.worksite=worksite;
		this.phone=phone;
		this.loginStatus=loginStatus;
		this.locked=locked;
		this.deactivated=deactivated;
		
	}
	public Employee(Integer id,Integer number,String firstName,String lastName,String email,Integer managerId,
			String department,WorkSite worksite,String phone,Boolean loginStatus,Boolean locked,String image,Boolean deactivated) {
		this.id=id;
		this.number=number;
		this.firstName=firstName;
		this.lastName=lastName;
		this.email=email;
		this.managerId=managerId;
		this.department=department;
		this.worksite=worksite;
		this.phone=phone;
		this.loginStatus=loginStatus;
		this.locked=locked;
		this.deactivated=deactivated;
		this.image=image;
	}
	public Employee(Integer id,Integer number,String firstName,String lastName,String email,Integer managerId,
			String department,WorkSite worksite,String phone,Boolean loginStatus,Boolean locked,
			Boolean deactivated,String password) {
		this.id=id;
		this.number=number;
		this.firstName=firstName;
		this.lastName=lastName;
		this.email=email;
		this.managerId=managerId;
		this.department=department;
		this.worksite=worksite;
		this.phone=phone;
		this.loginStatus=loginStatus;
		this.locked=locked;
		this.deactivated=deactivated;
		this.password=password;
	}

	public Employee(Integer id, Integer number, String firstName, String lastName, String email){
		this.id=id;
		this.number=number;
		this.firstName=firstName;
		this.lastName=lastName;
		this.email=email;
	}

}
