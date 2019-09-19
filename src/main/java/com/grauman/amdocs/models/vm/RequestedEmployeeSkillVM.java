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
	private Integer employeeSkillId;
	private Integer employeeId;
	private Integer skillId;
	private String skillName;
	private Date date;
	private Integer level;
	private SkillType type;
	
}
