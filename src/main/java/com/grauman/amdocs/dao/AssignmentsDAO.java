package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IAssignmentsDAO;
import com.grauman.amdocs.errors.custom.InvalidDataException;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.vm.AssignmentRequestVM;
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
        if(CheckIfAssignmentExist(item)){
            throw new InvalidDataException("Employee already assigned to this project");
        }
        try (Connection connection = db.getConnection()) {
            // fetch project id by name since project is a unique name which
            // guarantees retrieving the appropriate id
            String insertAssignmentQuery = "INSERT INTO assignment (project_id, employee_id, start_date, requested_from_manager_id," +
                    " requested_to_manager_id, status) VALUES(?, ?, ?, ?, ?, ?) ";

            // preparing a statement that guarantees returning the auto generated id
            try (PreparedStatement command = connection.prepareStatement(insertAssignmentQuery, Statement.RETURN_GENERATED_KEYS)) {
                command.setInt(1, item.getProjectID());
                command.setInt(2, item.getEmployeeID());
                command.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
                item.setStartDate(new java.sql.Date(new java.util.Date().getTime()));
                command.setInt(4, item.getRequestFromManagerID());

                if (item.getRequestFromManagerID() != (item.getRequestToManagerID())) {
                    command.setInt(5, item.getRequestToManagerID());
                    command.setString(6, "Pending approval");
                    item.setStatus("Pending approval");
                } else {
                    command.setNull(5, Types.INTEGER);
                    command.setString(6, "In progress");
                    item.setStatus("In progress");
                }
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
    public List<AssignmentRequestVM> getAssignmentsByUserID(int employeeID, int currentPage, int limit) throws SQLException {
        List<AssignmentRequestVM> assignments = new ArrayList<>();

        if (currentPage < 1)
             currentPage = 1;

        int offset = (currentPage - 1) * limit; // index of which row to start retrieving data
        String managerToName;
        String managerFromName;
        try (Connection connection = db.getConnection()) {

            String sqlCommand = "Select a.id, a.project_id, p.name, a.start_date, a.end_date, a.status, a.requested_from_manager_id,a.requested_to_manager_id\n" +
                    "from assignment a join project p on a.project_id=p.id where employee_id = ? limit ? offset ?";

            try (PreparedStatement command = connection.prepareStatement(sqlCommand)) {
                command.setInt(1, employeeID);
                command.setInt(2, limit);
                command.setInt(3, offset);

                try (ResultSet resultAssignment = command.executeQuery()) {
                    while (resultAssignment.next()) {

                        String managerNameQueryTo = "Select concat(u.first_name, \" \" , u.last_name) as name \n" +
                                "from users u where u.id = ?";
                        try (PreparedStatement commandManagerName = connection.prepareStatement(managerNameQueryTo)) {
                            commandManagerName.setInt(1, resultAssignment.getInt("a.requested_from_manager_id"));
                            try (ResultSet resultManagerName = commandManagerName.executeQuery()) {
                                resultManagerName.next();
                                managerToName = resultManagerName.getString(1);
                            }
                        }

                        String managerNameQueryFrom = "Select concat(u.first_name, \" \" , u.last_name) as name \n" +
                                "from users u where u.id = ?";
                        try (PreparedStatement commandManagerName = connection.prepareStatement(managerNameQueryFrom)) {
                            commandManagerName.setInt(1, resultAssignment.getInt("a.requested_to_manager_id"));
                            try (ResultSet resultManagerName = commandManagerName.executeQuery()) {
                                resultManagerName.next();
                                managerFromName = resultManagerName.getString(1);
                            }
                        }

                        assignments.add(new AssignmentRequestVM(
                                resultAssignment.getInt("a.id"),
                                resultAssignment.getString("p.name"),
                                resultAssignment.getInt("a.project_id"),
                                employeeID,
                                resultAssignment.getString("name"),
                                resultAssignment.getDate("a.start_date"),
                                resultAssignment.getDate("a.end_date"),
                                resultAssignment.getInt("a.requested_from_manager_id"),
                                resultAssignment.getInt("a.requested_to_manager_id"),
                                resultAssignment.getString("a.status"),managerFromName,managerToName
                                )

                        );
                    }
                }
            }

        }
        return assignments;
    }

    @Override
    public List<AssignmentRequestVM> getAssignmentsRequestByManagerID(int managerID, int currentPage, int limit) throws SQLException, ResultsNotFoundException {
        List<AssignmentRequestVM> assignmentsRequests = new ArrayList<>();
        if (currentPage < 1)
            currentPage = 1;
        int offset = (currentPage - 1) * limit; // index of which row to start retrieving data

        try (Connection connection = db.getConnection()) {
            String getAssignmentRequestQuery = "Select a.id,concat(u.first_name, \" \" , u.last_name) as name ,u.id, project_id, p.name, a.start_date, a.end_date, a.status, a.requested_from_manager_id,a.requested_to_manager_id\n" +
                    "from users u join assignment a on u.id = a.employee_id join project p on a.project_id=p.id where a.requested_to_manager_id = ? and a.status = 'Pending approval' limit ? offset ?";
            String managerToName;
            String managerFromName;
            try (PreparedStatement command = connection.prepareStatement(getAssignmentRequestQuery)) {
                command.setInt(1, managerID);
                command.setInt(2, limit);
                command.setInt(3, offset);
                try (ResultSet resultAssignment = command.executeQuery()) {
                    while (resultAssignment.next()) {
                        String managerNameQueryTo = "Select concat(u.first_name, \" \" , u.last_name) as name \n" +
                                "from users u where u.id = ?";
                        try (PreparedStatement commandManagerName = connection.prepareStatement(managerNameQueryTo)) {
                            commandManagerName.setInt(1, resultAssignment.getInt("a.requested_from_manager_id"));
                            try (ResultSet resultManagerName = commandManagerName.executeQuery()) {
                                resultManagerName.next();
                                managerToName = resultManagerName.getString(1);
                            }
                        }

                        String managerNameQueryFrom = "Select concat(u.first_name, \" \" , u.last_name) as name \n" +
                                "from users u where u.id = ?";
                        try (PreparedStatement commandManagerName = connection.prepareStatement(managerNameQueryFrom)) {
                            commandManagerName.setInt(1, resultAssignment.getInt("a.requested_to_manager_id"));
                            try (ResultSet resultManagerName = commandManagerName.executeQuery()) {
                                resultManagerName.next();
                                managerFromName = resultManagerName.getString(1);
                            }
                        }

                        assignmentsRequests.add(new AssignmentRequestVM(
                                resultAssignment.getInt("a.id"),
                                resultAssignment.getString("p.name"),
                                resultAssignment.getInt("a.project_id"),
                                resultAssignment.getInt("u.id"),
                                resultAssignment.getString("name"),
                                resultAssignment.getDate("a.start_date"),
                                resultAssignment.getDate("a.end_date"),
                                resultAssignment.getInt("a.requested_from_manager_id"),
                                resultAssignment.getInt("a.requested_to_manager_id"),
                                resultAssignment.getString("a.status"),
                                managerToName,
                                managerFromName)
                        );
                    }
                }
            }
        }
        return assignmentsRequests;
    }


    @Override
    public List<AssignmentRequestVM> getDoneAssignments(Integer managerID, Date requestedDate, Integer currentPage, Integer limit) throws SQLException {
        List<AssignmentRequestVM> doneAssignments = new ArrayList<>();
        if (currentPage < 1)
            currentPage = 1;

        Integer offset = (currentPage - 1) * limit;
        String managerToName;
        String managerFromName;
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


                        String managerNameQueryTo = "Select concat(u.first_name, \" \" , u.last_name) as name \n" +
                                "from users u where u.id = ?";
                        try (PreparedStatement commandManagerName = conn.prepareStatement(managerNameQueryTo)) {
                            commandManagerName.setInt(1, result.getInt("a.requested_from_manager_id"));
                            try (ResultSet resultManagerName = commandManagerName.executeQuery()) {
                                resultManagerName.next();
                                managerToName = resultManagerName.getString(1);
                            }
                        }

                        String managerNameQueryFrom = "Select concat(u.first_name, \" \" , u.last_name) as name \n" +
                                "from users u where u.id = ?";
                        try (PreparedStatement commandManagerName = conn.prepareStatement(managerNameQueryFrom)) {
                            commandManagerName.setInt(1, result.getInt("a.requested_to_manager_id"));
                            try (ResultSet resultManagerName = commandManagerName.executeQuery()) {
                                resultManagerName.next();
                                managerFromName = resultManagerName.getString(1);
                            }
                        }


                        doneAssignments.add(new AssignmentRequestVM(
                                result.getInt("a.id"),
                                result.getString("p.name"),
                                result.getInt("a.project_id"),
                                result.getInt("u.id"),
                                result.getString("name"),
                                result.getDate("a.start_date"),
                                result.getDate("a.end_date"),
                                result.getInt("a.requested_from_manager_id"),
                                result.getInt("a.requested_to_manager_id"),
                                result.getString("a.status"),managerFromName,managerToName)
                        );
                    }
                }
            }
        }
        return doneAssignments;
    }

    @Override
    public String updatePendingApprovalStatus(Assignment assignment, boolean approvalResponse) throws SQLException {
        String message = "SUCCESS";
        try (Connection connection = db.getConnection()) {
            String updateCommand = " update assignment SET status=?  where id= ? and status= \"Pending approval\"; ";
            try (PreparedStatement command = connection.prepareStatement(updateCommand, Statement.RETURN_GENERATED_KEYS)) {
                if (approvalResponse) {
                    command.setString(1, "In progress");
                } else {
                    command.setString(1, "Not approved");
                }
                command.setInt(2, assignment.getId());
                if (command.executeUpdate() <= 0)
                    message = "FAILURE";
            }
        }

        return message;
    }

    private boolean CheckIfAssignmentExist(Assignment item)throws SQLException{
        String checkQuery= "Select employee_id FROM assignment a where a.project_id= ? and employee_id=? and status='In progress'";
        try (Connection conn = db.getConnection()) {
            try (PreparedStatement command = conn
                    .prepareStatement(checkQuery)){
                command.setInt(1, item.getProjectID());
                command.setInt(2, item.getEmployeeID());
                ResultSet result = command.executeQuery();
                return result.next();
            }
        }
    }
}

