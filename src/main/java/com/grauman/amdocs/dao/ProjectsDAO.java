package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IProjectsDAO;
import com.grauman.amdocs.models.Project;

import java.sql.SQLException;
import java.util.List;

public class ProjectsDAO implements IProjectsDAO {
    @Override
    public List<Project> findAll() throws SQLException {
        return null;
    }

    @Override
    public Project find(int id) throws SQLException {
        return null;
    }

    @Override
    public Project add(Project movie) throws SQLException {
        return null;
    }

    @Override
    public Project update(Project movie) throws SQLException {
        return null;
    }

    @Override
    public Project delete(int id) throws SQLException {
        return null;
    }
}
