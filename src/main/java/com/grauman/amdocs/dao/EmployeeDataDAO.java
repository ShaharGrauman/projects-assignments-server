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

import com.grauman.amdocs.dao.interfaces.IEmployeeDataDAO;
import com.grauman.amdocs.models.Department;
import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.WorkSite;

@Service
public class EmployeeDataDAO implements IEmployeeDataDAO {
	@Autowired
	DBManager db;

	@Override
	public List<EmployeeData> findAll() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EmployeeData find(int id) throws SQLException {

		EmployeeData found = null;
		String sqlFindCommand = "Select U1.*,U2.first_name,WS.name"
				+ " From users U1 JOIN users U2 ON U1.manager_id=U2.id"
				+ " JOIN worksite WS ON U1.work_site_id=WS.id Where U1.work_site_id=WS.id AND U1.id=?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
				command.setInt(1, id);
				ResultSet result = command.executeQuery();
				result.next();

				int employeeId = result.getInt("U1.id");

				String sqlEmployeeRoles = "Select R.name" + " From roles R JOIN userrole UR ON R.id=UR.role_id "
						+ "Where UR.user_id=?";
				List<Role> roles = new ArrayList<>();

				try (PreparedStatement command2 = conn.prepareStatement(sqlEmployeeRoles)) {
					command2.setInt(1, employeeId);
					ResultSet result2 = command2.executeQuery();

					while (result2.next()) {
						roles.add(new Role(result2.getString(1)));
					}
				}

				found = new EmployeeData(result.getInt("U1.id"), result.getInt("U1.employee_number"),
						result.getString("U1.first_name"), result.getString("U1.last_name"),
						result.getString("U1.email"), result.getString("U2.first_name"), result.getInt("U1.manager_id"),
						result.getString("U1.department"), result.getString("WS.name"),
						result.getInt("U1.work_site_id"), result.getString("U1.country"), result.getString("U1.phone"),
						result.getBoolean("U1.login_status"), result.getBoolean("U1.locked"),
						result.getBoolean("U1.deactivated"), result.getString("U1.password"), roles);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;
	}

	@Override
	public EmployeeData add(EmployeeData employee) throws SQLException {
		int employeeID;
		EmployeeData newEmployee = null;
		String sqlAddEmployeeStatement = "Insert INTO users (employee_number,first_name,last_name,email,manager_id,"
				+ "department,work_site_id,country,phone,login_status,locked,deactivated,password)"
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement statement = conn.prepareStatement(sqlAddEmployeeStatement,
					Statement.RETURN_GENERATED_KEYS)) {

				statement.setInt(1, employee.getNumber());
				statement.setString(2, employee.getFirstName());
				statement.setString(3, employee.getLastName());
				statement.setString(4, employee.getEmail());
				statement.setInt(5, employee.getManagerId());
				statement.setString(6, employee.getDepartment());
				statement.setInt(7, employee.getWorkSiteId());
				statement.setString(8, employee.getCountry());
				statement.setString(9, employee.getPhone());
				statement.setBoolean(10, employee.getLoginStatus());
				statement.setBoolean(11, employee.getLocked());
				statement.setBoolean(12, employee.getDeactivated());
				statement.setString(13, employee.getPassword());

				int rowCountUpdated = statement.executeUpdate();

				ResultSet ids = statement.getGeneratedKeys();

				while (ids.next()) {
					employeeID = ids.getInt(1);
					System.out.println(employeeID);
					newEmployee = find(employeeID);
				}

			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return newEmployee;
	}

//should update the userrole table
	@Override
	public EmployeeData update(EmployeeData employee) throws SQLException {

		String sqlDelEmployeeStatement = "update users set employee_number=?,first_name=?,last_name=?,"
				+ "email=?,manager_id=?,department=?,work_site_id=?,"
				+ "country=?,phone=?,login_status=?,locked=?,deactivated=?,password=? where id=?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement statement = conn.prepareStatement(sqlDelEmployeeStatement)) {

				statement.setInt(1, employee.getNumber());
				statement.setString(2, employee.getFirstName());
				statement.setString(3, employee.getLastName());
				statement.setString(4, employee.getEmail());
				statement.setInt(5, employee.getManagerId());
				statement.setString(6, employee.getDepartment());
				statement.setInt(7, employee.getWorkSiteId());
				statement.setString(8, employee.getCountry());
				statement.setString(9, employee.getPhone());
				statement.setBoolean(10, employee.getLoginStatus());
				statement.setBoolean(11, employee.getLocked());
				statement.setBoolean(12, employee.getDeactivated());
				statement.setString(13, employee.getPassword());

				statement.setInt(14, employee.getId());

				int rowCountUpdated = statement.executeUpdate();
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return employee;
	}

//deactivate an Employee
	@Override
	public EmployeeData delete(int id) throws SQLException {
		String sqlDelEmployeeStatement = "update users set deactivated=true where id=?";
		EmployeeData deactevatedEmployee = null;
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement statment = conn.prepareStatement(sqlDelEmployeeStatement)) {
				statment.setInt(1, id);

				int res = statment.executeUpdate();

				deactevatedEmployee = find(id);
			}
		}
		return deactevatedEmployee;
	}

//unlock user
	public EmployeeData unlock(int id) throws SQLException {
		String sqlUnlockEmployeeStatement = "update users set locked=true where id=?";
		EmployeeData lockedEmployee = null;
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement statment = conn.prepareStatement(sqlUnlockEmployeeStatement)) {
				statment.setInt(1, id);

				int res = statment.executeUpdate();
				lockedEmployee = find(id);
			}
		}
		return lockedEmployee;
	}

// get sites name
	public List<WorkSite> findAllSites() throws SQLException {
		List<WorkSite> sites = new ArrayList<WorkSite>();
		String sqlSitesCommand = "Select id,name from worksite";
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery(sqlSitesCommand);
				while (result.next()) {
					sites.add(new WorkSite(result.getInt("id"), result.getString("name")));
				}
			}
		}
		return sites;
	}

// get roles name
	public List<Role> findAllRoles() throws SQLException {
		List<Role> roles = new ArrayList<Role>();
		String sqlSitesCommand = "Select id,name From roles";
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery(sqlSitesCommand);
				while (result.next()) {
					roles.add(new Role(result.getInt("id"), result.getString("name")));
				}
			}
		}
		return roles;
	}

// get departments name
	public List<Department> findAllDepartments() throws SQLException {
		List<Department> departments = new ArrayList<Department>();
		String sqlDepartmetsCommand = "select * from department";
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery(sqlDepartmetsCommand);
				while (result.next()) {
					departments.add(new Department(result.getInt(1), result.getString(2)));
				}
			}
		}
		return departments;
	}

// get Managers (Name+ID)
	public List<EmployeeData> findAllManagers() throws SQLException {
		List<EmployeeData> managers = new ArrayList<EmployeeData>();
		String sqlSitesCommand = "select U1.id,U1.first_name " + "From users U1 JOIN users U2 ON U1.id=U2.manager_id "
				+ "JOIN userrole UR ON U1.id=UR.user_id JOIN roles R ON UR.role_id=R.id" + " Where R.name='manager'";

		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {

				ResultSet result = command.executeQuery(sqlSitesCommand);
				while (result.next()) {
					managers.add(new EmployeeData(result.getInt(1), result.getString(2)));
				}
			}
		}
		return managers;
	}

}
