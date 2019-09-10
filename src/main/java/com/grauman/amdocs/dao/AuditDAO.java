package com.grauman.amdocs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grauman.amdocs.dao.interfaces.IAuditDAO;
import com.grauman.amdocs.models.Audit;

@Service
public class AuditDAO implements IAuditDAO{
	 @Autowired DBManager db;

	@Override
	public List<Audit> findAll() throws SQLException {
		List<Audit> audit=new ArrayList<Audit>();
		String sqlAllUserscommand="select A.id,A.employee_number, A.date_time,U.id as Employeeid,A.activity "+ 
									"from users U JOIN audit A ON U.id=A.user_id";
		try(Connection conn = db.getConnection()) {
			try(Statement command = conn.createStatement()){
				ResultSet result=command.executeQuery(sqlAllUserscommand);
				while(result.next()) {
					audit.add(
							new Audit(
									result.getInt(1),
									result.getInt(2),
									result.getDate(3),
									result.getInt(4),
									result.getString(5)
									));
				}
			}
		}
		return audit;
	}

	@Override
	public Audit find(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Audit add(Audit movie) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Audit update(Audit movie) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Audit delete(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Audit> searchAuditByEmployeeNumber(int id) throws SQLException {
		List<Audit> audit = new ArrayList<>();
		String sqlSitesCommand = "Select id,employee_number,date_time,activity from audit where employee_number=?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlSitesCommand)) {
				command.setInt(1, id);
				ResultSet result = command.executeQuery();

				while (result.next()) {
					audit.add(new Audit(
							result.getInt("id"),
							result.getInt("employee_number"),
							result.getDate("date_time"),
							result.getString("activity")));
				}
			}
		}
		return audit;
	}
}
