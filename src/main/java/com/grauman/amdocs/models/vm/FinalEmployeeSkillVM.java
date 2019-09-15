package com.grauman.amdocs.models.vm;

import java.sql.Date;

import com.grauman.amdocs.models.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class FinalEmployeeSkillVM {
//pojo for last update for each skill
	
	private int employeeSkillId;
	private String skillName;
	private Date date;
	private int level;
	private String comment;
	private Status status;

	public FinalEmployeeSkillVM(int employeeSkillId, String employeeSkillName, int level){
		this.employeeSkillId=employeeSkillId;
		this.skillName=employeeSkillName;
		this.level=level;
	}
	
	

}
