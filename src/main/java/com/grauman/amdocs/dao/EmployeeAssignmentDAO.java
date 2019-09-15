package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IEmployeeAssignmentDAO;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.vm.AssignmentHistoryVM;
import com.grauman.amdocs.models.vm.EmployeeAssignmentVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeAssignmentDAO implements IEmployeeAssignmentDAO {
    @Autowired
    private DBManager db;

    @Override
    public List<EmployeeAssignmentVM> getDoneAssignments(Integer managerID, Date requestedDate, Integer pageNumber, Integer limit) throws SQLException {
        List<EmployeeAssignmentVM> doneAssignments = new ArrayList<>();
        // Date today = Calendar.getInstance().getTime();

        if (pageNumber < 1)
            pageNumber = 1;

        Integer offset = (pageNumber - 1) * limit;

        try (Connection conn = db.getConnection()) {
            String sqlCommand = " select u.id, concat(u.first_name, \" \" , u.last_name) as name, a.id, p.name,\n" +
                    "             a.start_date, a.end_date, a.requested_from_manager_id, a.requested_to_manager_id, a.status\n" +
                    "             from users u join assignment a on u.id=a.employee_id join project p on a.project_id=p.id \n" +
                    "             where u.manager_id = ? and a.status='Done!' and\n" +
                    "             (select datediff((select curdate()) , a.end_date)) <  (select datediff((select curdate()) , ? )) \n" +
                    "             and (select datediff((select curdate()) , a.end_date)) > 0 limit ? offset ? ;";

            try (PreparedStatement command = conn.prepareStatement(sqlCommand)) {
                command.setInt(1, managerID);
                command.setDate(2, (java.sql.Date) requestedDate);
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
                                result.getString("u.name"),
                                result.getString("p.name"))

                        );
                    }
                }
            }

        }
//        catch (SQLException e){
            if (doneAssignments.isEmpty()) {
                throw new ResultsNotFoundException("Couldn't find done assignments for this manager");

//            }
        }
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
