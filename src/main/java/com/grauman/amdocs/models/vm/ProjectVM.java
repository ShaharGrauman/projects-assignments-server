package com.grauman.amdocs.models.vm;

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
    private List<SkillsLevelVM> technicalSkill;
    private List<SkillsLevelVM> productSkill;
    private Integer managerID;
}
