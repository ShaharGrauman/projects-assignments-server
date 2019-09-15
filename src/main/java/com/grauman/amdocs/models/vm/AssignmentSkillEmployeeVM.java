package com.grauman.amdocs.models.vm;

import com.grauman.amdocs.models.vm.FinalEmployeeSkillVM;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AssignmentSkillEmployeeVM {
    private Integer id;
    private Integer managerID;
    private String name;
    private List<FinalEmployeeSkillVM> technicalSkills;
    private List <FinalEmployeeSkillVM> productSkills;
}
