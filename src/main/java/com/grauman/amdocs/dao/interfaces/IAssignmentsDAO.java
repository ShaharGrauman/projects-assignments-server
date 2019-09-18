package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.vm.AssignmentHistoryVM;
import com.grauman.amdocs.models.vm.AssignmentRequestVM;

import java.sql.SQLException;
import java.util.List;

public interface IAssignmentsDAO extends IDAO<Assignment>{
    List<AssignmentHistoryVM> getAssignmentsByUserID(int id, int currPage, int limit) throws SQLException, ResultsNotFoundException;
    List<AssignmentRequestVM> getAssignmentsRequestByManagerID(int managerid, int currPage, int limit) throws SQLException, ResultsNotFoundException;
    String updatePendingApprovalStatus(int assignmentID, boolean response) throws SQLException;

}
