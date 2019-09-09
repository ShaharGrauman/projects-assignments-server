package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IAssignmentsDAO;
import com.grauman.amdocs.models.Assignment;

import java.sql.SQLException;
import java.util.List;

public class AssignmentsDAO implements IAssignmentsDAO {
    @Override
    public List<Assignment> findAll() throws SQLException {
        return null;
    }

    @Override
    public Assignment find(int id) throws SQLException {
        return null;
    }

    @Override
    public Assignment add(Assignment movie) throws SQLException {
        return null;
    }

    @Override
    public Assignment update(Assignment movie) throws SQLException {
        return null;
    }

    @Override
    public Assignment delete(int id) throws SQLException {
        return null;
    }
}
