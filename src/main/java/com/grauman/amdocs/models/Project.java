package com.grauman.amdocs.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Project {
    private int id;
    private String name;
    private String description;
    private Date startDate;
    private List<SkillsProject> technicalSkill;
    private List<SkillsProject> productSkill;
    private int managerID;
}
