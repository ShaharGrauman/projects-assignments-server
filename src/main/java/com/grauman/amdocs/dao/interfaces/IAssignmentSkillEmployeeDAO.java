package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.models.vm.AssignmentSkillEmployeeVM;
import com.grauman.amdocs.models.vm.SkillsLevelVM;

import java.sql.SQLException;
import java.util.List;

public interface IAssignmentSkillEmployeeDAO extends IDAO<AssignmentSkillEmployeeVM> {
    public List<AssignmentSkillEmployeeVM> getEmployeesByManagerID (Integer managerID, Integer currentPage, Integer limit) throws SQLException;
    public List<AssignmentSkillEmployeeVM> getEmployeesByProjectID(Integer projectID, Integer currentPage, Integer limit) throws SQLException;
    public List<AssignmentSkillEmployeeVM> getEmployeesByEmployeeName(String employeeName, Integer currentPage, Integer limit) throws SQLException;
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillID(Integer skillID, Integer currentPage, Integer limit) throws SQLException;
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillSet(List<SkillsLevelVM> skillSet ,Integer currentPage,Integer limit) throws SQLException;
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillName(String skillName, Integer pageNumber, Integer limit) throws SQLException;
}
