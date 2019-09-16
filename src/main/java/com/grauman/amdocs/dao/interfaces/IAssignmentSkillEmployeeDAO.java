package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.models.vm.AssignmentSkillEmployeeVM;

import java.sql.SQLException;
import java.util.List;

public interface IAssignmentSkillEmployeeDAO extends IDAO<AssignmentSkillEmployeeVM> {
    public List<AssignmentSkillEmployeeVM> getEmployeesByManagerID (Integer managerID, Integer pageNumber, Integer limit) throws SQLException;
    public List<AssignmentSkillEmployeeVM> getEmployeesByProjectID(Integer projectid) throws SQLException;
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillID(Integer skillID, Integer pageNumber, Integer limit) throws SQLException;
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillSet(List<Integer> skillSet, Integer pageNumber, Integer limit) throws SQLException;

}
