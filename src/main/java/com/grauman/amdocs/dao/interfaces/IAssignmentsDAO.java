package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.vm.AssignmentHistoryVM;
import com.grauman.amdocs.models.vm.AssignmentRequestVM;
import com.grauman.amdocs.models.vm.EmployeeAssignmentVM;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IAssignmentsDAO extends IDAO<Assignment> {
    List<AssignmentHistoryVM> getAssignmentsByUserID(int id, int currentPage, int limit) throws SQLException, ResultsNotFoundException;

    List<AssignmentRequestVM> getAssignmentsRequestByManagerID(int managerID, int currentPage, int limit) throws SQLException, ResultsNotFoundException;

    List<EmployeeAssignmentVM> getDoneAssignments(Integer managerID, Date requestedDate, Integer currentPage, Integer limit) throws SQLException;

    String updatePendingApprovalStatus(Assignment assignmentID, boolean response) throws SQLException;

}
