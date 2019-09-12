package com.grauman.amdocs.models;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class SkillUpdatesHistory {
//POJO for approved skill history 
	
	private Integer level;
	private Date date;

}
