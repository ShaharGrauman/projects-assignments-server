package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IEmployeeAssignmentDAO;
import com.grauman.amdocs.models.vm.EmployeeAssignmentVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeAssignmentDAO implements IEmployeeAssignmentDAO {
    @Autowired
    private DBManager db;

    @Override
    public List<EmployeeAssignmentVM> getDoneAssignments(Integer managerID, Date requestedDate, Integer pageNumber, Integer limit) throws SQLException {
        List<EmployeeAssignmentVM> doneAssignments = new ArrayList<>();



        return doneAssignments;
    }

    @Override
    public List<EmployeeAssignmentVM> findAll() throws SQLException {
        return null;
    }

    @Override
    public EmployeeAssignmentVM find(int id) throws SQLException {
        return null;
    }

    @Override
    public EmployeeAssignmentVM add(EmployeeAssignmentVM movie) throws SQLException {
        return null;
    }

    @Override
    public EmployeeAssignmentVM update(EmployeeAssignmentVM movie) throws SQLException {
        return null;
    }

    @Override
    public EmployeeAssignmentVM delete(int id) throws SQLException {
        return null;
    }
}
