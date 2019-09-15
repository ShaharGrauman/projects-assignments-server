package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.vm.ProjectVM;

import java.sql.SQLException;
import java.util.List;

public interface IProjectsDAO extends IDAO<ProjectVM>{
    // List<ProjectVM> getManagerProjects(int managerId) throws SQLException;
    public List<ProjectVM> getProjectsByManagerID(Integer managerID) throws SQLException, ResultsNotFoundException;
}
