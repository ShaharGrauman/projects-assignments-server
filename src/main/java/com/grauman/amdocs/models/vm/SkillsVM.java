package com.grauman.amdocs.models.vm;


import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SkillsVM {
	//pojo skills for assignment
	private List<SkillProjectVM> technicalSkills;
	private List<SkillProjectVM> productSkills;

}
