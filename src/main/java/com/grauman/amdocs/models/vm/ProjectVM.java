package com.grauman.amdocs.models.vm;

import com.grauman.amdocs.models.vm.SkillsProjectVM;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectVM {
    private Integer id;
    private String name;
    private String description;
    private Date startDate;
    private List<SkillsProjectVM> technicalSkill;
    private List<SkillsProjectVM> productSkill;
    private Integer managerID;
}
