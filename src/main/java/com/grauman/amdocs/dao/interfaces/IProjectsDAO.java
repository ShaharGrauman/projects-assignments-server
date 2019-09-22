package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.vm.ProjectVM;

import java.sql.SQLException;
import java.util.List;

public interface IProjectsDAO extends IDAO<ProjectVM>{
    public List<ProjectVM> getProjectsByManagerID(Integer managerID) throws SQLException, ResultsNotFoundException;
    public List<ProjectVM> searchProjectByProjectName(String projectName, Integer currentPage, Integer limit) throws SQLException;
    public List<ProjectVM> getProjectsByUserID(Integer userID) throws SQLException, ResultsNotFoundException;
    public List<ProjectVM> getProjectsByUserName(String userName) throws SQLException, ResultsNotFoundException;

}
