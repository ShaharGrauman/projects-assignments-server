package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Project;

import java.sql.SQLException;
import java.util.List;

public interface IProjectsDAO extends IDAO<Project>{
    // List<Project> getManagerProjects(int managerId) throws SQLException;
    public List<Project> getProjectsByManagerID(Integer managerID) throws SQLException, ResultsNotFoundException;
}
