package com.grauman.amdocs.models;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Audit {
	private Integer id;
	private Integer employeeNumber;
	private String firstName;
	private String lastName;
	private Date dateTime;
	private Integer userId;
	private String activity;
	private List<Role> roles;
	
	public Audit(Integer id,Integer employeeNumber, Date dateTime,Integer userId,String activity) {
        this.id=id;
        this.employeeNumber=employeeNumber;
        this.dateTime=dateTime;
        this.userId=userId;
        this.activity=activity;
    }
    public Audit(Integer id,Integer employeeNumber, Date dateTime,String activity) {
        this.id=id;
        this.employeeNumber=employeeNumber;
        this.dateTime=dateTime;
        this.activity=activity;
    }
    public Audit(Integer id,Integer employeeNumber,Integer userId,String firstName,String lastName, Date dateTime,String activity,List<Role> roles) {
        this.id=id;
        this.employeeNumber=employeeNumber;
        this.userId=userId;
        this.firstName=firstName;
        this.lastName=lastName;
        this.dateTime=dateTime;
        this.activity=activity;
        this.roles=roles;
    }
	
}
