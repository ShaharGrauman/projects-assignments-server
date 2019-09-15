package com.grauman.amdocs.models;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class EmployeeSkill {
	private int id;
	private int employeeId;
	private int managerId;
	private int skillId;
	private Date date;
	private int level;
	private String comment;
	private Status status;
	

}
