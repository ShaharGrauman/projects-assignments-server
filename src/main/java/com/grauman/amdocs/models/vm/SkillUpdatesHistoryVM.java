package com.grauman.amdocs.models.vm;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class SkillUpdatesHistoryVM {
//POJO for approved skill history 
	
	private Integer level;
	private Date date;

}
