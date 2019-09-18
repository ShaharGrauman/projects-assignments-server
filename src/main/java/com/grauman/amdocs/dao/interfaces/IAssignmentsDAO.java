package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.vm.AssignmentHistoryVM;
import com.grauman.amdocs.models.vm.AssignmentRequest;
import com.grauman.amdocs.models.vm.EmployeeAssignmentVM;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IAssignmentsDAO extends IDAO<Assignment>{
    List<AssignmentHistoryVM> getAssignmentsByUserID(int id, int currPage, int limit) throws SQLException, ResultsNotFoundException;
    List<AssignmentRequest> getAssignmentsRequestByManagerID(int managerid, int currPage, int limit) throws SQLException, ResultsNotFoundException;
    List<EmployeeAssignmentVM>  getAssignmentsbystatus(Integer managerID, Date requestedDate, Integer pageNumber, Integer limit) throws SQLException;
}
