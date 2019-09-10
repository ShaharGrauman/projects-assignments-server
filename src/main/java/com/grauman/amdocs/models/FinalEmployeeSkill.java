package com.grauman.amdocs.models;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class FinalEmployeeSkill {
//pojo for last update for each skill
	
	private int employeeSkillId;
	private String skillName;
	private Date date;
	private int level;
	private String comment;
	private Status status;
	
	

}
