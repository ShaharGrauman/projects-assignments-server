package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.models.vm.EmployeeAssignmentVM;

import java.sql.SQLException;
import java.util.List;

public interface IEmployeeAssignmentDAO extends IDAO<EmployeeAssignmentVM> {
    public List<EmployeeAssignmentVM> getDoneAssignments(Integer managerID, Integer numberOfMonths, Integer pageNumber, Integer limit)throws SQLException;
}
