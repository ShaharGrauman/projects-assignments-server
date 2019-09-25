package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.vm.ProjectVM;

import java.sql.SQLException;
import java.util.List;

public interface IProjectsDAO extends IDAO<ProjectVM>{

    /**
     *
     * @param managerID
     * @return list of projects that a manager employees are working on
     * @throws SQLException
     */
    public List<ProjectVM> getProjectsByManagerID(Integer managerID, Integer currentPage, Integer limit) throws SQLException, ResultsNotFoundException;

    /**
     *
     * @param projectName
     * @return search projects by name
     * @throws SQLException
     */
    public List<ProjectVM> searchProjectByProjectName(String projectName, Integer currentPage, Integer limit) throws SQLException;

    /**
     *
     * @param userID
     * @return list of projects that an employee are working on by his/her ID
     * @throws SQLException
     */
    public List<ProjectVM> getProjectsByUserID(Integer userID, Integer currentPage, Integer limit) throws SQLException, ResultsNotFoundException;

    /**
     *
     * @param userName
     * @return list of projects that an employee are working on by his/her name
     * @throws SQLException
     */
    public List<ProjectVM> getProjectsByUserName(String userName, Integer currentPage, Integer limit) throws SQLException, ResultsNotFoundException;

}
