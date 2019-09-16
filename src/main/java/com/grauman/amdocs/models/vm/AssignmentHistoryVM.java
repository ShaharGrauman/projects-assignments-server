package com.grauman.amdocs.models.vm;

import com.grauman.amdocs.models.Assignment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
@Getter
@Setter
public class AssignmentHistoryVM extends Assignment {
    private String projectName;

    public AssignmentHistoryVM(int id, String projectName , int projectID, int employeeID, Date startDate, Date endDate, int requestFromManagerID, int requestToManagerID, String status) {
        super(id, projectID, employeeID, startDate, endDate, requestFromManagerID, requestToManagerID, status);
        this.projectName=projectName;
    }
}
