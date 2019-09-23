package com.grauman.amdocs.models.vm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SkillsLevelVM {
    private Integer id;
    private String name;
    private Integer level = 1 ;
}
