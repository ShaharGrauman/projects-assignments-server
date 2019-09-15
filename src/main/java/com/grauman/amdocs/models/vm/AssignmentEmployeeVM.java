package com.grauman.amdocs.models.vm;

import com.grauman.amdocs.models.FinalEmployeeSkill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AssignmentEmployeeVM {
    private Integer id;
    private Integer managerID;
    private String name;
    private List<FinalEmployeeSkill> technicalSkills;
    private List <FinalEmployeeSkill> productSkills;
}
