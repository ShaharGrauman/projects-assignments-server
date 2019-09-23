package com.grauman.amdocs.models.vm;

import com.grauman.amdocs.models.Assignment;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class AssignmentVM extends Assignment {
    private String employeeName;
    private String toManagerName;
    private String fromManagerName;
    private String projectName;
    public AssignmentVM(int id, String projectName, int projectID, int employeeID, String employeeName , Date startDate, Date endDate, int requestFromManagerID, int requestToManagerID, String status, String fromManagerName, String toManagerName) {
        super(id, projectID, employeeID, startDate, endDate, requestFromManagerID, requestToManagerID, status);
        this.employeeName=employeeName;
        this.fromManagerName=fromManagerName;
        this.toManagerName=toManagerName;
        this.projectName=projectName;
    }
}
