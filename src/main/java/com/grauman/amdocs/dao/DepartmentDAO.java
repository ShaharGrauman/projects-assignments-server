package com.grauman.amdocs.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grauman.amdocs.dao.interfaces.IDepartmentDAO;
import com.grauman.amdocs.errors.custom.AlreadyExistsException;
import com.grauman.amdocs.models.Audit;
import com.grauman.amdocs.models.AuditEmployee;
import com.grauman.amdocs.models.Department;
import com.grauman.amdocs.models.EmployeeException;

@Service
public class DepartmentDAO implements IDepartmentDAO {
	@Autowired
	DBManager db;
	
	@Autowired
	AuthenticationDAO authenticationDAO;

	@Autowired
	AuditDAO auditDAO;
	
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

	// validation done
	/**
	    * @param department
	    * @return new added department
	    * @throws SQLException
	    */

	@Override
	public Department add(Department department) throws Exception {
		int departmentId;
		boolean catchTimeOut = false;
		int tries = 0;
		ResultSet exists = null;
		Department newD = null;
		ResultSet ids = null;
		String checkIfDepartmentExists = "select * from department where name=?";
		String sqlAddDepartment = "Insert INTO department(name) values(?)";
		
		try (Connection conn = db.getConnection()) {
			// check if the department already exists
			try (PreparedStatement state = conn.prepareStatement(checkIfDepartmentExists)) {
				state.setString(1, department.getName());

				do {
					try {
						exists = state.executeQuery();
						catchTimeOut = false;
					} catch (SQLTimeoutException e) {
						catchTimeOut = true;
						if (tries++ > 3)
							throw e;
					}
				} while (catchTimeOut);

				tries = 0;
				if (exists.next()) {
					throw new AlreadyExistsException(String.format("Department %s already exists", department.getName()));
				}
				
				try (PreparedStatement statement = conn.prepareStatement(sqlAddDepartment,
						Statement.RETURN_GENERATED_KEYS)) {
					statement.setString(1, department.getName());

					if (department.getName() == null)
						throw new Exception("Department name is requried");

					int num = 0;
					do {
						try {
							num = statement.executeUpdate();
							if (num == 0)
								throw new Exception("update failed");
							catchTimeOut = false;
						} catch (SQLTimeoutException e) {
							catchTimeOut = true;
							if (tries++ > 3)
								throw e;
						}
					} while (catchTimeOut);
					
					ids = statement.getGeneratedKeys();						
					
					while (ids.next()) {
						departmentId = ids.getInt(1);
						String sqlresult = "select id,name From department Where id=?";

						tries = 0;
						try (PreparedStatement command = conn.prepareStatement(sqlresult)) {
							command.setInt(1, departmentId);
							ResultSet result = null;
							do {
								try {
									result = command.executeQuery();
									catchTimeOut = false;
								} catch (SQLTimeoutException e) {
									catchTimeOut = true;
									if (tries++ > 3)
										throw e;
								}
							} while (catchTimeOut);

							result.next();
							newD = new Department(result.getInt(1), result.getString(2));
						}
						
						auditDAO.add((new AuditEmployee().builder()
									.audit(new Audit().builder()
											.employeeNumber(authenticationDAO.getAuthenticatedUser().getEmployeeNumber())
											.dateTime(new Date(System.currentTimeMillis()))
											.userId(authenticationDAO.getAuthenticatedUser().getId())
											.activity("Add Department").build()
									)).build());
					}
				}
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
