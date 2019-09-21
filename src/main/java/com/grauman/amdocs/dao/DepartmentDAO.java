package com.grauman.amdocs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grauman.amdocs.dao.interfaces.IDepartmentDAO;
import com.grauman.amdocs.models.Department;

@Service
public class DepartmentDAO implements IDepartmentDAO{
	 @Autowired DBManager db;
	 
	@Override
	public List<Department> findAll() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department find(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department add(Department department) throws SQLException {
		int departmentId;
		Department newD=null;
		String checkIfDepartmentExists="select * from department where name=?";
		String sqlAddDepartment="Insert INTO department(name) values(?)";
		try (Connection conn = db.getConnection()) {
			//check if the department already exists
			try(PreparedStatement state = conn.prepareStatement(checkIfDepartmentExists)){
				state.setString(1,department.getName());
				ResultSet exists=state.executeQuery();
				//if the result set is false..there is no such department in the database
				if(!exists.next()) {
				try (PreparedStatement statement = conn.prepareStatement(sqlAddDepartment,Statement.RETURN_GENERATED_KEYS)) {
					statement.setString(1,department.getName());
					
					int rowCountUpdated = statement.executeUpdate();
	
					ResultSet ids = statement.getGeneratedKeys();
	
					while (ids.next()) {
						departmentId = ids.getInt(1);
						String sqlresult="select id,name From department Where id=?";
						try(PreparedStatement command = conn.prepareStatement(sqlresult)){
							command.setInt(1,departmentId);
							ResultSet result=command.executeQuery();
							result.next();
							newD=new Department(result.getInt(1), result.getString(2));
							
						}
					}
				  }
				}
				//throw excption
				}
			}
		return newD;
	}

	@Override
	public Department update(Department movie) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department delete(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
