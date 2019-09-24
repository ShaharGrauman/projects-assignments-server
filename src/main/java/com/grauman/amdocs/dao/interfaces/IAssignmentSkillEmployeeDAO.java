package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.models.vm.AssignmentSkillEmployeeVM;
import com.grauman.amdocs.models.vm.SkillsLevelVM;

import java.sql.SQLException;
import java.util.List;

public interface IAssignmentSkillEmployeeDAO extends IDAO<AssignmentSkillEmployeeVM> {
    /**
     *
     * @param managerID
     * @param currentPage
     * @param limit
     * @return manager team
     * @throws SQLException
     */
    public List<AssignmentSkillEmployeeVM> getEmployeesByManagerID (Integer managerID, Integer currentPage, Integer limit) throws SQLException;

    /**
     *
     * @param projectID
     * @param currentPage
     * @param limit
     * @return workers on project
     * @throws SQLException
     */
    public List<AssignmentSkillEmployeeVM> getEmployeesByProjectID(Integer projectID, Integer currentPage, Integer limit) throws SQLException;
    /**
     *
     * @param employeeName
     * @param currentPage
     * @param limit
     * @return employees with similar first/last name as in the search
     * @throws SQLException
     */
    public List<AssignmentSkillEmployeeVM> getEmployeesByEmployeeName(String employeeName, Integer currentPage, Integer limit) throws SQLException;

    /**
     *
     * @param skillID
     * @param currentPage
     * @param limit
     * @return employees who have a skill ID as in the search
     * @throws SQLException
     */
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillID(Integer skillID, Integer currentPage, Integer limit) throws SQLException;

    /**
     *
     * @param skillSet
     * @param currentPage
     * @param limit
     * @return employees who have skills as in the search
     * @throws SQLException
     */
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillSet(List<SkillsLevelVM> skillSet ,Integer currentPage,Integer limit) throws SQLException;

    /**
     *
     * @param skillName
     * @param currentPage
     * @param limit
     * @return employees who have a skill name as in the search
     * @throws SQLException
     */
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillName(String skillName, Integer currentPage, Integer limit) throws SQLException;
    /**
     *
     * @param skillVM
     * @param currentPage
     * @param limit
     * @return employees who have a skill name with level bigger or equal to the level in the search
     * @throws SQLException
     */
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillNameLevel(SkillsLevelVM skillVM, Integer currentPage, Integer limit) throws SQLException ;

    }
