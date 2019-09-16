package com.grauman.amdocs.models.vm;

import com.grauman.amdocs.models.Assignment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
//@AllArgsConstructor
public class EmployeeAssignmentVM extends Assignment {
    String employeeName;
    String projectName;

    public EmployeeAssignmentVM(Integer id, Integer projectID, Integer employeeID, Date startDate, Date endDate, Integer requestFromManagerID, Integer requestToManagerID, String status, String employeeName, String projectName) {
        super(id, projectID, employeeID, startDate, endDate, requestFromManagerID, requestToManagerID, status);
        this.employeeName = employeeName;
        this.projectName = projectName;
    }
}
