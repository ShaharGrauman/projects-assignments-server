package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IAssignmentsDAO;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.AssignmentHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Service
public class AssignmentsDAO implements IAssignmentsDAO {
    @Autowired
    DBManager db;

    @Override
    public List<Assignment> findAll() throws SQLException {
        return null;
    }

    @Override
    public Assignment find(int id) throws SQLException {
        return null;
    }

    @Override
    public Assignment add(Assignment item) throws SQLException {
        System.out.println(item.getEmployeeID());
        try (Connection conn = db.getConnection()) {
            // fetch project id by name since project is a unique name which
            // guarantees retrieving the appropriate id
            String insertQuery = "INSERT INTO assignment (project_id, employee_id, start_date, end_date, requested_from_manager_id," +
                    " requested_to_manager_id, status) VALUES(?, ?, ?, ?, ?, ?,?)";

            // preparing a statement that guarantees returning the auto generated id
            try (PreparedStatement command = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                command.setInt(1, item.getProjectID());
                command.setInt(2, item.getEmployeeID());
                command.setDate(3, item.getStartDate());
                command.setDate(4, item.getEndDate());
                command.setInt(5, item.getRequestFromManagerID());
                command.setInt(6, item.getRequestToManagerID());
                command.setString(7, item.getStatus());
                command.executeUpdate();
                try (ResultSet generatedID = command.getGeneratedKeys()) {
                    if (generatedID.next())
                        item.setId(generatedID.getInt(1));
                    else
                        throw new SQLException("Assignment insertion failed.");
                }
            }


        }
        return item;
    }

    @Override
    public Assignment update(Assignment movie) throws SQLException {
        return null;
    }

    @Override
    public Assignment delete(int id) throws SQLException {
        return null;
    }

    @Override
    public List<AssignmentHistory> getAssignmentsByUserID(int employeeID, int currPage, int limit) throws SQLException {
        List<AssignmentHistory> assignments = new ArrayList<AssignmentHistory>();

        if (currPage < 1)
            currPage = 1;

        int offset = (currPage - 1) * limit; // index of which row to start retrieving data

        try (Connection conn = db.getConnection()) {

            String sqlCommand = "Select a.id, project_id, p.name, a.start_date, a.end_date, a.status, a.requested_from_manager_id,a.requested_to_manager_id\n" +
                                 "from assignment a join project p on a.project_id=p.id where employee_id = ? limit ? offset ?";

            try (PreparedStatement command = conn.prepareStatement(sqlCommand)) {
                command.setInt(1, employeeID);
                command.setInt(2, limit);
                command.setInt(3, offset);

                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        assignments.add(new AssignmentHistory(
                                result.getInt("a.id"),
                                result.getString("p.name"),
                                result.getInt("a.project_id"),
                                employeeID,
                                result.getDate("a.start_date"),
                                result.getDate("a.end_date"),
                                result.getInt("a.requested_from_manager_id"),
                                result.getInt("a.requested_to_manager_id"),
                                result.getString("a.status"))
                        );
                    }
                }
            }

        }
        if (assignments.isEmpty()) {
            throw new ResultsNotFoundException("Couldn't find assignments for this employee");

        }
        return assignments;
    }
}
