package com.grauman.amdocs.validations;

import com.grauman.amdocs.models.Assignment;

import java.util.HashMap;
import java.util.Map;

public class Validations {

    public static Map<String, String> check(Assignment assignment) {
        Map<String,String> assigmentError = new HashMap<>();

        if(assignment.getId() < 0)
            assigmentError.put(String.valueOf(assignment.getId()),"ID must be positive number");
        if(assignment.getEmployeeID() < 0)
            assigmentError.put(String.valueOf(assignment.getEmployeeID()),"EmployeeID must be positive number");
        if(assignment.getProjectID() < 0)
            assigmentError.put(String.valueOf(assignment.getProjectID()),"ProjectID must be positive number");
        return assigmentError;
    }
}
