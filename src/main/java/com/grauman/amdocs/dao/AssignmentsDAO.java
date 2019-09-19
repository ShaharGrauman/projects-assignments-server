package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IAssignmentsDAO;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.vm.AssignmentHistoryVM;
import com.grauman.amdocs.models.vm.AssignmentRequestVM;
import com.grauman.amdocs.models.vm.EmployeeAssignmentVM;
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
    public List<AssignmentHistoryVM> getAssignmentsByUserID(int employeeID, int currPage, int limit) throws SQLException {
        List<AssignmentHistoryVM> assignments = new ArrayList<>();

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
                        assignments.add(new AssignmentHistoryVM(
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

    @Override
    public List<AssignmentRequestVM> getAssignmentsRequestByManagerID(int managerid, int currPage, int limit) throws SQLException, ResultsNotFoundException {
        List<AssignmentRequestVM> assignments = new ArrayList<>();
        if (currPage < 1)
            currPage = 1;
        int offset = (currPage - 1) * limit; // index of which row to start retrieving data
        try (Connection conn = db.getConnection()) {
            String sqlCommand = "Select a.id,concat(u.first_name, \" \" , u.last_name) as name ,u.id, project_id, p.name, a.start_date, a.end_date, a.status, a.requested_from_manager_id,a.requested_to_manager_id\n" +
                    "from users u join assignment a on u.id = a.employee_id join project p on a.project_id=p.id where a.requested_to_manager_id = ? and a.status = 'Pending approval' limit ? offset ?";
            String managerName;
            try (PreparedStatement command = conn.prepareStatement(sqlCommand)) {
                command.setInt(1, managerid);
                command.setInt(2, limit);
                command.setInt(3, offset);
                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        String sqlmanagerName = "Select concat(u.first_name, \" \" , u.last_name) as name \n" +
                                "from users u where u.id = ?";
                        try (PreparedStatement commandManagerName = conn.prepareStatement(sqlmanagerName)) {
                            commandManagerName.setInt(1, result.getInt(9));
                            try (ResultSet resultManagerName = commandManagerName.executeQuery()) {
                                resultManagerName.next();
                                managerName = resultManagerName.getString(1);
                            }
                        }
                        assignments.add(new AssignmentRequestVM(
                                result.getInt("a.id"),
                                result.getString("p.name"),
                                result.getInt("a.project_id"),
                                result.getInt("u.id"),
                                result.getString("name"),
                                result.getDate("a.start_date"),
                                result.getDate("a.end_date"),
                                result.getInt("a.requested_from_manager_id"),
                                result.getInt("a.requested_to_manager_id"),
                                result.getString("a.status"), managerName)
                        );
                    }
                }
            }
        }
        if (assignments.isEmpty()) {
            throw new ResultsNotFoundException("Couldn't find assignments for this manager");
        }
        return assignments;
    }


    @Override
    public List<EmployeeAssignmentVM> getAssignmentsbystatus(Integer managerID, Date requestedDate, Integer pageNumber, Integer limit) throws SQLException {
        List<EmployeeAssignmentVM> doneAssignments = new ArrayList<>();
        // Date today = Calendar.getInstance().getTime();
        if (pageNumber < 1)
            pageNumber = 1;
        Integer offset = (pageNumber - 1) * limit;
        try (Connection conn = db.getConnection()) {
            String sqlCommand = " select u.id, concat(u.first_name, \" \" , u.last_name) as name, a.project_id ,a.id, p.name,\n" +
                    "             a.start_date, a.end_date, a.requested_from_manager_id, a.requested_to_manager_id, a.status\n" +
                    "             from users u join assignment a on u.id=a.employee_id join project p on a.project_id=p.id \n" +
                    "             where u.manager_id = ? and a.status='Done!' and\n" +
                    "             (select datediff((select curdate()) , a.end_date)) <  (select datediff((select curdate()) , ? )) \n" +
                    "             and (select datediff((select curdate()) , a.end_date)) > 0 limit ? offset ? ;";
            try (PreparedStatement command = conn.prepareStatement(sqlCommand)) {
                command.setInt(1, managerID);
                command.setDate(2, requestedDate);
                command.setInt(3, limit);
                command.setInt(4, offset);
                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        doneAssignments.add(new EmployeeAssignmentVM(
                                result.getInt("a.id"),
                                result.getInt("a.project_id"),
                                result.getInt("u.id"),
                                result.getDate("a.start_date"),
                                result.getDate("a.end_date"),
                                result.getInt("a.requested_from_manager_id"),
                                result.getInt("a.requested_to_manager_id"),
                                result.getString("a.status"),
                                result.getString("name"),
                                result.getString("p.name"))
                        );
                    }
                }
            }
        }
        if (doneAssignments.isEmpty()) {
            throw new ResultsNotFoundException("Couldn't find done assignments for this manager");
        }
        return doneAssignments;
    }

    @Override
    public String updatePendingApprovalStatus(Assignment assignment, boolean response) throws SQLException {
        String message = "SUCCESS";
        try (Connection conn = db.getConnection()) {
            String updateCommand = " update assignment SET status=?  where id= ? and status= \"Pending approval\"; ";
            try (PreparedStatement command = conn.prepareStatement(updateCommand, Statement.RETURN_GENERATED_KEYS)) {
                if (response) {
                    command.setString(1, "In progress");
                } else {
                    command.setString(1, "Not approved");
                }
                command.setInt(2, assignment.getId());
                // int responseMessage = command.executeUpdate();
                if (command.executeUpdate() <= 0)
                    message = "FAILURE";
            }
        }

        return message;
    }
}

