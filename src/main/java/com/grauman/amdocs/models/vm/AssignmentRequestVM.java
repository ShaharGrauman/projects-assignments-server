package com.grauman.amdocs.models.vm;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class AssignmentRequestVM extends AssignmentHistoryVM {
    String employeeName;

    public AssignmentRequestVM(int id, String projectName, int projectID, int employeeID, String employeeName , Date startDate, Date endDate, int requestFromManagerID, int requestToManagerID, String status) {
        super(id, projectName, projectID, employeeID, startDate, endDate, requestFromManagerID, requestToManagerID, status);
        this.employeeName=employeeName;
    }
}
