package com.grauman.amdocs.models.vm;

import java.sql.Date;

import com.grauman.amdocs.models.SkillType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class RequestedEmployeeSkillVM {
//pending skill for single employee

	private String employeeName;
	private int employeeSkillId;
	private int employeeId;
	private int skillId;
	private String skillName;
	private Date date;
	private int level;
	private SkillType type;
	
}
