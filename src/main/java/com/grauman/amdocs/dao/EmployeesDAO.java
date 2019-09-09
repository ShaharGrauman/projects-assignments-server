package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IEmployeesDAO;
import com.grauman.amdocs.models.Employee;

import java.sql.SQLException;
import java.util.List;

public class EmployeesDAO implements IEmployeesDAO {
    @Override
    public List<Employee> findAll() throws SQLException {
        return null;
    }

    @Override
    public Employee find(int id) throws SQLException {
        return null;
    }

    @Override
    public Employee add(Employee movie) throws SQLException {
        return null;
    }

    @Override
    public Employee update(Employee movie) throws SQLException {
        return null;
    }

    @Override
    public Employee delete(int id) throws SQLException {
        return null;
    }
}
