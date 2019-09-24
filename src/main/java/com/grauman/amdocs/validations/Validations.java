package com.grauman.amdocs.validations;


import com.grauman.amdocs.models.vm.ProjectVM;

import java.util.HashMap;
import java.util.Map;

class MagicNumber {
    public static int MIN_LEVEL = 1;
    public static int MAX_LEVEL = 5;
}

public class Validations {

    public static Map<String, String> check(ProjectVM project) {
        Map<String, String> projectError = new HashMap<>();


        if (project.getStartDate().getTime() < new java.util.Date().getTime() || project.getStartDate() == null || project.getStartDate().equals(""))
            projectError.put(String.valueOf(project.getStartDate()), "Incorret Date");

        if (project.getName() == null || project.getName().equals(""))
            projectError.put("Project name", "Field that should be not empty");

        if (project.getDescription() == null || project.getDescription().equals(""))
            projectError.put("Description", "Field that should be not empty");

        for (int i = 0; i < project.getTechnicalSkill().size(); i++)
            if (MagicNumber.MIN_LEVEL > project.getTechnicalSkill().get(i).getLevel() || project.getTechnicalSkill().get(i).getLevel() > MagicNumber.MAX_LEVEL)
                projectError.put(String.valueOf(project.getTechnicalSkill().get(i).getName()), "level should be  between 1 to 5 ");

        for (int i = 0; i < project.getProductSkill().size(); i++)
            if (MagicNumber.MIN_LEVEL > project.getProductSkill().get(i).getLevel() || project.getProductSkill().get(i).getLevel() > MagicNumber.MAX_LEVEL)
                projectError.put(String.valueOf(project.getProductSkill().get(i).getName()), "level should be  between 1 to 5 ");

        return projectError;
    }
}
