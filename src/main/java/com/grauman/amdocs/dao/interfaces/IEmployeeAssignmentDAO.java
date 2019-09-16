package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.models.vm.EmployeeAssignmentVM;

import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

public interface IEmployeeAssignmentDAO extends IDAO<EmployeeAssignmentVM> {
    public List<EmployeeAssignmentVM> getDoneAssignments(Integer managerID, Date requestedDate, Integer pageNumber, Integer limit)throws SQLException;
}
