package com.grauman.amdocs.models.vm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class ApprovedSkillHistoryVM {
//pojo for each approved skill 
	

	private String name;
	private List<SkillUpdatesHistoryVM> updates;//level and date array
	
	
	
	
}
