package com.grauman.amdocs.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class ApprovedSkillHistory {
//pojo for each approved skill 
	

	private String name;
	private List<SkillUpdatesHistory> updates;//level and date array
	
	
	
	
}
