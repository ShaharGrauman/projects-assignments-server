package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IAssignmentsDAO;
import com.grauman.amdocs.errors.custom.AlreadyExistsException;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.vm.AssignmentVM;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssignmentsDAO implements IAssignmentsDAO {
    String sqlCommand;
    @Autowired
    DBManager db;
    @Autowired
    AuthenticationDAO authenticationDAO;

    @Override
    public List<Assignment> findAll() throws SQLException {
        return null;
    }

    @Override
    public Assignment find(int id) throws SQLException {
        return null;
    }

    /**
     * @param  newAssignment
     * @return new added assignment
     * @throws SQLException
     */
    @Override
    public Assignment add(Assignment newAssignment) throws SQLException {

        if (CheckIfAssignment(newAssignment)) {
            throw new AlreadyExistsException("Employee already assigned to this project");
        }
        try (Connection connection = db.getConnection()) {
            // fetch project id by name since project is a unique name which
            // guarantees retrieving the appropriate id
            String insertAssignmentQuery = "INSERT INTO assignment (project_id, employee_id, start_date, requested_from_manager_id," +
                    " requested_to_manager_id, status) VALUES(?, ?, ?, ?, ?, ?) ";
            String getIdManagerQuery = "select u.id  from users u join users u2 on u.id = u2.manager_id where u2.id = ?;";
            Integer managerTo;

            try (PreparedStatement commandManagerID = connection.prepareStatement(getIdManagerQuery)) {
                commandManagerID.setInt(1, newAssignment.getEmployeeID());
                try (ResultSet resultManagerID = commandManagerID.executeQuery()) {
                    resultManagerID.next();
                    managerTo = resultManagerID.getInt(1);
                }
            }



            // preparing a statement that guarantees returning the auto generated id
            try (PreparedStatement command = connection.prepareStatement(insertAssignmentQuery, Statement.RETURN_GENERATED_KEYS)) {
                command.setInt(1, newAssignment.getProjectID());
                command.setInt(2, newAssignment.getEmployeeID());
                command.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
                newAssignment.setStartDate(new java.sql.Date(new java.util.Date().getTime()));
                command.setInt(4, authenticationDAO.getAuthenticatedUser().getId());

                if (authenticationDAO.getAuthenticatedUser().getId() !=managerTo) {
                    command.setInt(5, managerTo);
                    command.setString(6, "PENDING_APPROVAL");
                    newAssignment.setStatus("PENDING_APPROVAL");
                } else {
                    command.setNull(5, Types.INTEGER);
                    command.setString(6, "IN_PROGRESS");
                    newAssignment.setStatus("IN_PROGRESS");
                }
                command.executeUpdate();
                try (ResultSet generatedID = command.getGeneratedKeys()) {
                    if (generatedID.next())
                        newAssignment.setId(generatedID.getInt(1));
                    else
                        throw new SQLException("Assignment insertion failed.");
                }
            }


        }
        return newAssignment;
    }

    @Override
    public Assignment update(Assignment movie) throws SQLException {
        return null;
    }

    @Override
    public Assignment delete(int id) throws SQLException {
        return null;
    }

    /**
     * @param  employeeID
     * * @param  currentPage
     * * @param  limit
     * @return array of assignments for employee
     * @throws SQLException
     * @throws ResultsNotFoundException
     */
    @Override
    public List<AssignmentVM> getAssignmentsByUserID(int employeeID, int currentPage, int limit) throws SQLException {
        List<AssignmentVM> assignments = new ArrayList<>();

        if (currentPage < 1)
            currentPage = 1;

        int offset = (currentPage - 1) * limit; // index of which row to start retrieving data
        String managerToName;
        String managerFromName;
        try (Connection connection = db.getConnection()) {

            String sqlCommand = "Select a.id, a.project_id, p.name, a.start_date, a.end_date, a.status, a.requested_from_manager_id,a.requested_to_manager_id " +
                    "from assignment a join project p on a.project_id=p.id where employee_id = ? limit ? offset ?";

            try (PreparedStatement command = connection.prepareStatement(sqlCommand)) {
                command.setInt(1, employeeID);
                command.setInt(2, limit);
                command.setInt(3, offset);

                try (ResultSet resultAssignment = command.executeQuery()) {
                    while (resultAssignment.next()) {

                        String managerNameQueryTo = "Select concat(u.first_name, \" \" , u.last_name) as name " +
                                                    "from users u where u.id = ?";
                        try (PreparedStatement commandManagerName = connection.prepareStatement(managerNameQueryTo)) {
                            commandManagerName.setInt(1, resultAssignment.getInt("a.requested_from_manager_id"));
                            try (ResultSet resultManagerName = commandManagerName.executeQuery()) {
                                resultManagerName.next();
                                managerToName = resultManagerName.getString(1);
                            }
                        }

                        String managerNameQueryFrom = "Select concat(u.first_name, \" \" , u.last_name) as name " +
                                                      "from users u where u.id = ?";
                        try (PreparedStatement commandManagerName = connection.prepareStatement(managerNameQueryFrom)) {
                            commandManagerName.setInt(1, resultAssignment.getInt("a.requested_to_manager_id"));
                            try (ResultSet resultManagerName = commandManagerName.executeQuery()) {
                                resultManagerName.next();
                                managerFromName = resultManagerName.getString(1);
                            }
                        }

                        assignments.add(new AssignmentVM(
                                        resultAssignment.getInt("a.id"),
                                        resultAssignment.getString("p.name"),
                                        resultAssignment.getInt("a.project_id"),
                                        employeeID,
                                        resultAssignment.getString("name"),
                                        resultAssignment.getDate("a.start_date"),
                                        resultAssignment.getDate("a.end_date"),
                                        resultAssignment.getInt("a.requested_from_manager_id"),
                                        resultAssignment.getInt("a.requested_to_manager_id"),
                                        resultAssignment.getString("a.status"), managerFromName, managerToName
                                )

                        );
                    }
                }
            }

        }
        return assignments;
    }

    /**
     * @param  managerID
     * * @param  currentPage
     * * @param  limit
     * @return array of assignments requests for manager team
     * @throws SQLException
     * @throws ResultsNotFoundException
     */
    @Override
    public List<AssignmentVM> getAssignmentsRequestByManagerID(int managerID, int currentPage, int limit) throws SQLException, ResultsNotFoundException {
        List<AssignmentVM> assignmentsRequests = new ArrayList<>();
        if (currentPage < 1)
            currentPage = 1;
        int offset = (currentPage - 1) * limit; // index of which row to start retrieving data

        try (Connection connection = db.getConnection()) {
            String getAssignmentRequestQuery = "Select a.id,concat(u.first_name, \" \" , u.last_name) as name ,u.id, project_id, p.name, a.start_date, a.end_date, a.status, a.requested_from_manager_id,a.requested_to_manager_id " +
                                               "from users u join assignment a on u.id = a.employee_id join project p on a.project_id=p.id where a.requested_to_manager_id = ? and a.status = 'PENDING_APPROVAL' limit ? offset ?";
            String managerToName;
            String managerFromName;
            try (PreparedStatement command = connection.prepareStatement(getAssignmentRequestQuery)) {
                command.setInt(1, authenticationDAO.getAuthenticatedUser().getId());
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

                        String managerNameQueryFrom = "Select concat(u.first_name, \" \" , u.last_name) as name " +
                                                      "from users u where u.id = ?";
                        try (PreparedStatement commandManagerName = connection.prepareStatement(managerNameQueryFrom)) {
                            commandManagerName.setInt(1, resultAssignment.getInt("a.requested_to_manager_id"));
                            try (ResultSet resultManagerName = commandManagerName.executeQuery()) {
                                resultManagerName.next();
                                managerFromName = resultManagerName.getString(1);
                            }
                        }

                        assignmentsRequests.add(new AssignmentVM(
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

    /**
     * @param  managerID
     * * @param requestedDate
     * * @param  currentPage
     * * @param  limit
     * @return array of done assignments for manager team
     * @throws SQLException
     * @throws ResultsNotFoundException
     */
    @Override
    public List<AssignmentVM> getDoneAssignments(Integer managerID, Date requestedDate, Integer currentPage, Integer limit) throws SQLException {
        List<AssignmentVM> doneAssignments = new ArrayList<>();
        if (currentPage < 1)
            currentPage = 1;

        Integer offset = (currentPage - 1) * limit;
        String managerToName;
        String managerFromName;
        try (Connection conn = db.getConnection()) {
            String sqlCommand = " select u.id, concat(u.first_name, \" \" , u.last_name) as name, a.project_id ,a.id, p.name, " +
                                "a.start_date, a.end_date, a.requested_from_manager_id, a.requested_to_manager_id, a.status " +
                                "from users u join assignment a on u.id=a.employee_id join project p on a.project_id=p.id " +
                                "where u.manager_id = ? and a.status='DONE' and " +
                                "(select datediff((select curdate()) , a.end_date)) <  (select datediff((select curdate()) , ? ))  " +
                                "and (select datediff((select curdate()) , a.end_date)) > 0 limit ? offset ? ;";

            this.sqlCommand=" select count(*) "+
                    "from users u join assignment a on u.id=a.employee_id join project p on a.project_id=p.id " +
                    "where u.manager_id = " + authenticationDAO.getAuthenticatedUser().getId() +" and a.status='DONE' and " +
                    "(select datediff((select curdate()) , a.end_date)) <  (select datediff((select curdate()) ,'" + requestedDate + "'))  " +
                    "and (select datediff((select curdate()) , a.end_date)) > 0 limit "+ limit +" offset " + offset ;
            System.out.println(this.sqlCommand);
            try (PreparedStatement command = conn.prepareStatement(sqlCommand)) {
                command.setInt(1, authenticationDAO.getAuthenticatedUser().getId());
                command.setDate(2, requestedDate);
                command.setInt(3, limit);
                command.setInt(4, offset);
                System.out.println(command);
                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {


                        String managerNameQueryTo = "Select concat(u.first_name, \" \" , u.last_name) as name  " +
                                                    "from users u where u.id = ?";
                        try (PreparedStatement commandManagerName = conn.prepareStatement(managerNameQueryTo)) {
                            commandManagerName.setInt(1, result.getInt("a.requested_from_manager_id"));
                            try (ResultSet resultManagerName = commandManagerName.executeQuery()) {
                                resultManagerName.next();
                                managerToName = resultManagerName.getString(1);
                            }
                        }

                        String managerNameQueryFrom = "Select concat(u.first_name, \" \" , u.last_name) as name " +
                                                      "from users u where u.id = ?";
                        try (PreparedStatement commandManagerName = conn.prepareStatement(managerNameQueryFrom)) {
                            commandManagerName.setInt(1, result.getInt("a.requested_to_manager_id"));
                            try (ResultSet resultManagerName = commandManagerName.executeQuery()) {
                                resultManagerName.next();
                                managerFromName = resultManagerName.getString(1);
                            }
                        }


                        doneAssignments.add(new AssignmentVM(
                                result.getInt("a.id"),
                                result.getString("p.name"),
                                result.getInt("a.project_id"),
                                result.getInt("u.id"),
                                result.getString("name"),
                                result.getDate("a.start_date"),
                                result.getDate("a.end_date"),
                                result.getInt("a.requested_from_manager_id"),
                                result.getInt("a.requested_to_manager_id"),
                                result.getString("a.status"), managerFromName, managerToName)
                        );
                    }
                }
            }
        }
        return doneAssignments;
    }

    /**
     * @param  assignment
     * * @param response
     * @return message of success/failure in approving/not approving assignment request
     * @throws SQLException
     * @throws ResultsNotFoundException
     */
    @Override
    public String updatePendingApprovalStatus(Assignment assignment, boolean approvalResponse) throws SQLException {
        String message = "SUCCESS";
        try (Connection connection = db.getConnection()) {
            String updateCommand = " update assignment SET status=?  where id= ? and status= \"PENDING_APPROVAL\"; ";
            try (PreparedStatement command = connection.prepareStatement(updateCommand, Statement.RETURN_GENERATED_KEYS)) {
                if (approvalResponse) {
                    command.setString(1, "IN_PROGRESS");
                } else {
                    command.setString(1, "NOT_APPROVED");
                }
                command.setInt(2, assignment.getId());
                if (command.executeUpdate() <= 0)
                    message = "FAILURE";
            }
        }

        return message;
    }

    @Override
    public Integer countDoneAssignments() throws SQLException {
        try (Connection conn = db.getConnection()) {
            try (Statement command = conn.createStatement()) {
                ResultSet result = command.executeQuery(this.sqlCommand);
                result.next();
                return result.getInt("count(*)");
            }
        }
    }

    private boolean CheckIfAssignment(Assignment item) throws SQLException {
        String checkQuery = "Select employee_id FROM assignment a where a.project_id= ? and employee_id=? and status in ('IN_PROGRESS','PENDING_APPROVAL')";
        try (Connection conn = db.getConnection()) {
            try (PreparedStatement command = conn
                    .prepareStatement(checkQuery)) {
                command.setInt(1, item.getProjectID());
                command.setInt(2, item.getEmployeeID());
                ResultSet result = command.executeQuery();
                return result.next();
            }

        }
    }
}

