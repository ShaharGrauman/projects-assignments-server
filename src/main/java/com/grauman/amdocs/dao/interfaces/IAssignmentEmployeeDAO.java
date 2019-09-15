package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.models.vm.AssignmentEmployeeVM;

import java.sql.SQLException;
import java.util.List;

public interface IAssignmentEmployeeDAO extends IDAO<AssignmentEmployeeVM> {
    public List<AssignmentEmployeeVM> getEmployeesByManagerID (Integer managerID, Integer pageNumber, Integer limit) throws SQLException;
    public List<AssignmentEmployeeVM> getEmployeesByProjectID(Integer projectid) throws SQLException;
    public List<AssignmentEmployeeVM> searchEmployeesBySkillID(Integer skillID, Integer pageNumber, Integer limit) throws SQLException;
    public List<AssignmentEmployeeVM> searchEmployeesBySkillSet(List<Integer> skillSet, Integer pageNumber, Integer limit) throws SQLException;

}
