package com.grauman.amdocs.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.grauman.amdocs.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grauman.amdocs.controllers.EmployeeDataController;
import com.grauman.amdocs.dao.interfaces.IEmployeeDataDAO;
import com.grauman.amdocs.mail.MailManager;

@Service
public class EmployeeDataDAO implements IEmployeeDataDAO {
	@Autowired
	DBManager db;

	@Autowired
	MailManager mail;

//Search all employees which are locked
	@Override
	public List<EmployeeData> findAll() throws SQLException {
		int userId;
		List<EmployeeData> users = new ArrayList<>();
		String sqlAllUserscommand = "select  U.id,U.employee_number,U.first_name,U.last_name,U.department,"
				+ "WS.name as worksite,WS.city,C.name as country "
				+ "From users U JOIN worksite WS ON U.work_site_id=WS.id " + "JOIN country C ON WS.country_id=C.id "
				+ "where U.locked=true";
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery(sqlAllUserscommand);

				while (result.next()) {
					userId = result.getInt(1);
					List<Role> roles = new ArrayList<>();
					roles = getEmployeeRoles(userId);
					users.add(new EmployeeData(new Employee(result.getInt(1), result.getInt(2), result.getString(3),
							result.getString(4), result.getString(5),
							new WorkSite(result.getString(6), result.getString(7)), new Country(result.getString(8))),
							roles));

				}
			}
		}
		return users;
	}

//search all employees	
	public List<EmployeeData> findAllEmployees() throws SQLException {
		int userId;
		List<EmployeeData> users = new ArrayList<EmployeeData>();
		String sqlAllUserscommand = "select  U.id,U.employee_number,U.first_name,U.last_name,"
				+ "U.department,WS.name,WS.city,C.name " + " From users U JOIN worksite WS ON U.work_site_id=WS.id"
				+ " JOIN country C ON WS.country_id=C.id";
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery(sqlAllUserscommand);

				while (result.next()) {
					userId = result.getInt(1);
					List<Role> roles = new ArrayList<>();
					roles = getEmployeeRoles(userId);
					users.add(new EmployeeData(new Employee(result.getInt("U.id"), result.getInt("U.employee_number"),
							result.getString("U.first_name"), result.getString("U.last_name"),
							result.getString("U.department"),
							new WorkSite(result.getString("WS.name"), result.getString("WS.city")),
							new Country(result.getString("C.name"))), roles));
				}
			}
		}
		return users;
	}

// the search will be by Employee Number 

	public EmployeeData findByEmployeeNumber(int number) throws SQLException {
		Date auditDate;
		int employeeId;
		EmployeeData found = null;
		List<Role> roles = new ArrayList<>();
		String sqlFindLastLogin = "SELECT max(date_time) as LastLogin FROM audit" + " Group by employee_number"
				+ " Having employee_number=?";

		String sqlFindEmployee = " Select  U1.*,U2.first_name,WS.name"
				+ " From users U1 JOIN users U2 ON U1.manager_id=U2.id" + " JOIN worksite WS ON U1.work_site_id=WS.id"
				+ " Where U1.work_site_id=WS.id AND U1.employee_number=?";

		String sqlEmployeeRoles = " Select R.name" + " From roles R JOIN userrole UR ON R.id=UR.role_id"
				+ " Where UR.user_id=?";

		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command0 = conn.prepareStatement(sqlFindLastLogin)) {
				command0.setInt(1, number);
				ResultSet result0 = command0.executeQuery();
				if (result0.next()) {
					auditDate = result0.getDate(1);

					try (PreparedStatement command = conn.prepareStatement(sqlFindEmployee)) {
						command.setInt(1, number);
						ResultSet result = command.executeQuery();
						if (result.next()) {
							employeeId = result.getInt(1);

							try (PreparedStatement command2 = conn.prepareStatement(sqlEmployeeRoles)) {
								command2.setInt(1, employeeId);
								ResultSet result2 = command2.executeQuery();

								while (result2.next()) {
									roles.add(new Role(result2.getString(1)));
								}
							}
						}
						found = new EmployeeData(
								new Employee(result.getInt("U1.id"), result.getInt("U1.employee_number"),
										result.getString("U1.first_name"), result.getString("U1.last_name"),
										result.getString("U1.email"), result.getInt("U1.manager_id"),
										result.getString("U1.department"),
										new WorkSite(result.getInt("U1.work_site_id"), result.getString("WS.name")),
										new Country(result.getString("U1.country")), result.getString("U1.phone"),
										result.getBoolean("U1.login_status"), result.getBoolean("U1.locked"),
										result.getBoolean("U1.deactivated")),
								result.getString("U2.first_name"), auditDate, roles);

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;
	}

// find employee by id .. helping function
	@Override
	public EmployeeData find(int id) throws SQLException {
		EmployeeData found = null;
		int employeeId;
		List<Role> roles = new ArrayList<>();
		String sqlFindEmployee = "Select U1.*,U2.first_name,WS.name"
				+ " From users U1 JOIN users U2 ON U1.manager_id=U2.id"
				+ " JOIN worksite WS ON U1.work_site_id=WS.id Where U1.work_site_id=WS.id AND U1.id=?";

		String sqlEmployeeRoles = "Select R.name" + " From roles R JOIN userrole UR ON R.id=UR.role_id "
				+ "Where UR.user_id=?";

		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlFindEmployee)) {
				command.setInt(1, id);
				ResultSet result = command.executeQuery();
				if (result.next()) {
					employeeId = result.getInt("U1.id");
					try (PreparedStatement command2 = conn.prepareStatement(sqlEmployeeRoles)) {
						command2.setInt(1, employeeId);
						ResultSet result2 = command2.executeQuery();

						while (result2.next()) {
							roles.add(new Role(result2.getString("R.name")));
						}
						System.out.println(roles);
						found = new EmployeeData(new Employee(result.getInt("U1.id"),
								result.getInt("U1.employee_number"), result.getString("U1.first_name"),
								result.getString("U1.last_name"), result.getString("U1.email"),
								result.getInt("U1.manager_id"), result.getString("U1.department"),
								new WorkSite(result.getInt("U1.work_site_id"), result.getString("WS.name")),
								new Country(result.getString("U1.country")), result.getString("U1.phone"),
								result.getBoolean("U1.login_status"), result.getBoolean("U1.locked"),
								result.getBoolean("U1.deactivated")), result.getString("U2.first_name"), roles);
					}
				}
			}
		}
		return found;
	}

	// *******************************************************************************************
// we should generate a random password and then insert it to the uses table in the data base
	@Override
	public EmployeeData add(EmployeeData employee) throws SQLException {
		int newEmployeeId = -1;
		EmployeeData newEmployee = null;

		List<Role> roles = employee.getRoles();
		String sqlAddEmployeeStatement = "Insert INTO users (employee_number,first_name,last_name,email,manager_id,"
				+ "department,work_site_id,country,phone,login_status,locked,deactivated,password)"
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement statement = conn.prepareStatement(sqlAddEmployeeStatement,
					Statement.RETURN_GENERATED_KEYS)) {

				statement.setInt(1, employee.getEmployee().getNumber());
				statement.setString(2, employee.getEmployee().getFirstName());
				statement.setString(3, employee.getEmployee().getLastName());
				statement.setString(4, employee.getEmployee().getEmail());
				statement.setInt(5, employee.getEmployee().getManagerId());
				statement.setString(6, employee.getEmployee().getDepartment());
				statement.setInt(7, employee.getEmployee().getWorksite().getId());
				statement.setString(8, employee.getEmployee().getCountry().getName());
				statement.setString(9, employee.getEmployee().getPhone());
				statement.setBoolean(10, employee.getEmployee().getLoginStatus());
				statement.setBoolean(11, employee.getEmployee().getLocked());
				statement.setBoolean(12, employee.getEmployee().getDeactivated());
				// change it to the generated password!
				statement.setString(13, EmployeeDataDAO.generatePassword(6));

				int rowCountUpdated = statement.executeUpdate();

				ResultSet ids = statement.getGeneratedKeys();

				while (ids.next()) {
					newEmployeeId = ids.getInt(1);
					// find employee by ID
					newEmployee = find(newEmployeeId);
					System.out.println(newEmployeeId);
				}
			}

			String sqlAddRoleToEmployee = "Insert into userrole (user_id,role_id) values(?,?)";
			try (PreparedStatement statement1 = conn.prepareStatement(sqlAddRoleToEmployee)) {
				for (int i = 0; i < roles.size(); i++) {
					statement1.setInt(1, newEmployeeId);
					statement1.setInt(2, roles.get(i).getId());

					int rowCountUpdated = statement1.executeUpdate();
				}
			}
		}

		return find(newEmployeeId);
	}

//should update the userrole table
	@Override
	public EmployeeData update(EmployeeData employee) throws SQLException {
		EmployeeData updatedEmployee = null;
		List<Role> roles = new ArrayList<>();
		String sqlDelEmployeeStatement = "update users set employee_number=?,first_name=?,last_name=?,"
				+ "email=?,manager_id=?,department=?,work_site_id=?,"
				+ "country=?,phone=?,login_status=?,locked=?,deactivated=? where id=?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement statement = conn.prepareStatement(sqlDelEmployeeStatement)) {
				statement.setInt(1, employee.getEmployee().getNumber());
				statement.setString(2, employee.getEmployee().getFirstName());
				statement.setString(3, employee.getEmployee().getLastName());
				statement.setString(4, employee.getEmployee().getEmail());
				statement.setInt(5, employee.getEmployee().getManagerId());
				statement.setString(6, employee.getEmployee().getDepartment());
				statement.setInt(7, employee.getEmployee().getWorksite().getId());
				statement.setString(8, employee.getEmployee().getCountry().getName());
				statement.setString(9, employee.getEmployee().getPhone());
				statement.setBoolean(10, employee.getEmployee().getLoginStatus());
				statement.setBoolean(11, employee.getEmployee().getLocked());
				statement.setBoolean(12, employee.getEmployee().getDeactivated());

				statement.setInt(13, employee.getEmployee().getId());
				int rowCountUpdated = statement.executeUpdate();
			}
			String sqlUpdateRole = "update userrole set role_id=? where user_id=?";
			try (PreparedStatement statement = conn.prepareStatement(sqlUpdateRole)) {
				roles = employee.getRoles();
				for (int i = 0; i < roles.size(); i++) {
					statement.setInt(1, roles.get(i).getId());
					statement.setInt(2, employee.getEmployee().getId());

					int rowCountUpdated = statement.executeUpdate();
				}
			}
		}
		updatedEmployee = find(employee.getEmployee().getId());

		return updatedEmployee;

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

//**********************************************************************
//advanced search
//By Name
	public List<EmployeeData> filterByName(String name) throws SQLException {

		List<EmployeeData> found = new ArrayList<>();
		List<Role> employeeRoles = new ArrayList<>();
		String[] splitedFullName = name.split("\\s+");
		String sqlFindCommand = "select U.id,U.employee_number,U.first_name,U.last_name,"
				+ "U.department,WS.name,WS.city,C.name " + " From users U JOIN worksite WS ON U.work_site_id=WS.id"
				+ " JOIN country C ON WS.country_id=C.id" + "  having U.first_name=? OR U.last_name=?";

		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
				command.setString(1, splitedFullName[0]);
				command.setString(2, splitedFullName[1]);
				ResultSet result = command.executeQuery();
				if (result.next()) {
					employeeRoles = getEmployeeRoles(result.getInt(1));
					found.add(new EmployeeData(new Employee(result.getInt(1), result.getInt(2), result.getString(3),
							result.getString(4), result.getString(5),
							new WorkSite(result.getString(6), result.getString(7)), new Country(result.getString(8))),
							employeeRoles));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;
	}

	// By Role
	public List<EmployeeData> filterByRole(String roleName) throws SQLException {
		List<EmployeeData> found = new ArrayList<>();
		List<Role> employeeRoles = new ArrayList<>();

		String sqlFindCommand = "SELECT U.id, U.employee_number,U.first_name,U.last_name,"
				+ "W.name as workSiteName,W.city,U.country, U.department"
				+ " FROM users U JOIN worksite W ON U.work_site_id=W.id" + " JOIN userrole UR ON UR.user_id=U.id"
				+ " JOIN roles R ON R.id=UR.role_id" + " where R.name=?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
				command.setString(1, roleName);
				ResultSet result = command.executeQuery();

				while (result.next()) {
					employeeRoles = getEmployeeRoles(result.getInt(1));
					found.add(new EmployeeData(new Employee(result.getInt(1), result.getInt(2), result.getString(3),
							result.getString(4), result.getString(5),
							new WorkSite(result.getString(6), result.getString(7)), new Country(result.getString(8))),
							employeeRoles));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;
	}

//By Department
	public List<EmployeeData> filterByDepartment(String departmentName) throws SQLException {
		List<EmployeeData> found = new ArrayList<>();
		List<Role> employeeRoles = new ArrayList<>();
		String sqlFindCommand = "select DISTINCT U.id,U.employee_number,U.first_name,U.last_name,"
				+ "U.department,WS.name,WS.city,C.name " + " From users U JOIN worksite WS ON U.work_site_id=WS.id"
				+ " JOIN country C ON WS.country_id=C.id" + " where U.department=?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
				command.setString(1, departmentName);
				ResultSet result = command.executeQuery();

				while (result.next()) {
					employeeRoles = getEmployeeRoles(result.getInt(1));
					found.add(new EmployeeData(new Employee(result.getInt(1), result.getInt(2), result.getString(3),
							result.getString(4), result.getString(5),
							new WorkSite(result.getString(6), result.getString(7)), new Country(result.getString(8))),
							employeeRoles));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;
	}

//By WorkSite
	public List<EmployeeData> filterByWorkSite(String siteName) throws SQLException {
		List<EmployeeData> found = new ArrayList<>();
		List<Role> employeeRoles = new ArrayList<>();
		String sqlFindCommand = "select U.id,U.employee_number,U.first_name,U.last_name,"
				+ "U.department,WS.name,WS.city,C.name " + " From users U JOIN worksite WS ON U.work_site_id=WS.id"
				+ " JOIN country C ON WS.country_id=C.id" + " where WS.city=?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
				command.setString(1, siteName);
				ResultSet result = command.executeQuery();

				while (result.next()) {
					employeeRoles = getEmployeeRoles(result.getInt(1));
					found.add(new EmployeeData(new Employee(result.getInt(1), result.getInt(2), result.getString(3),
							result.getString(4), result.getString(5),
							new WorkSite(result.getString(6), result.getString(7)), new Country(result.getString(8))),
							employeeRoles));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;
	}

//search for all the employees which are working in the selected country
	public List<EmployeeData> filterByCountry(String countryName) throws SQLException {
		List<EmployeeData> found = new ArrayList<>();
		List<Role> employeeRoles = new ArrayList<>();
		String sqlFindCommand = "select U.id,U.employee_number,U.first_name,U.last_name,"
				+ "U.department,WS.name,WS.city,C.name " + " From users U JOIN worksite WS ON U.work_site_id=WS.id"
				+ " JOIN country C ON WS.country_id=C.id" + " where U.country=?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
				command.setString(1, countryName);
				ResultSet result = command.executeQuery();

				while (result.next()) {
					employeeRoles = getEmployeeRoles(result.getInt(1));
					found.add(new EmployeeData(new Employee(result.getInt(1), result.getInt(2), result.getString(3),
							result.getString(4), result.getString(5),
							new WorkSite(result.getString(6), result.getString(7)), new Country(result.getString(8))),
							employeeRoles));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;
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

//Helping function used in findAll Employees
	public List<Role> getEmployeeRoles(int id) throws SQLException {
		List<Role> employeeRoles = new ArrayList<>();
		String sqlFindRoles = "SELECT R.id,R.name" + " FROM roles R join userrole US ON R.id=US.role_id"
				+ " WHERE US.user_id=?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlFindRoles)) {
				command.setInt(1, id);
				ResultSet result = command.executeQuery();
				while (result.next()) {
					employeeRoles.add(new Role(result.getInt(1), result.getString(2)));
				}
			}
		}
		return employeeRoles;
	}

// get all sites 
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

// get all roles
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

// get all departments
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

// get all Managers (Name+ID)
	public List<Employee> findAllManagers() throws SQLException {
		List<Employee> managers = new ArrayList<Employee>();
		String sqlSitesCommand = "select DISTINCT U1.id,U1.first_name "
				+ "From users U1 JOIN users U2 ON U1.id=U2.manager_id "
				+ "JOIN userrole UR ON U1.id=UR.user_id JOIN roles R ON UR.role_id=R.id" + " Where R.name='manager'";

		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {

				ResultSet result = command.executeQuery(sqlSitesCommand);
				while (result.next()) {
					managers.add(new Employee(result.getInt(1), result.getString(2)));
				}
			}
		}
		return managers;
	}

//get all countries
	public List<Country> findAllCountries() throws SQLException {
		List<Country> countries = new ArrayList<>();
		String sqlDepartmetsCommand = "select * from country";
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery(sqlDepartmetsCommand);
				while (result.next()) {
					countries.add(new Country(result.getInt(1), result.getString(2)));
				}
			}
		}
		return countries;
	}

	// ***************************************************************************************************
	// counters for the Home Page
	public Integer countEmployees() throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery("select count(*) from users");
				result.next();
				return result.getInt("count(*)");
			}
		}
	}

	public Integer countRoles() throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery("select count(*) from roles");
				result.next();
				return result.getInt("count(*)");
			}
		}
	}

	public Integer countDepartments() throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery("select count(*) from department");
				result.next();
				return result.getInt("count(*)");
			}
		}
	}

	public Integer countWorkSites() throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery("select count(*) from worksite");
				result.next();
				return result.getInt("count(*)");
			}
		}
	}

//done Exceptions
//	@SuppressWarnings("null")
	@SuppressWarnings("null")
	public void resetPassword(String toEmail, int number) throws SQLException, EmployeeException {
		boolean catchTimeOut = false;
		int retries = 0;
		String findEmployeeByEmail = "SELECT * from users where email=? AND employee_number=?";
		String newPassword;
		EmployeeData employee = null;
		ResultSet result = null;

		if (!isValid(toEmail))
			throw new EmployeeException("email not valid");

		try (Connection conn = db.getConnection()) {
			try (PreparedStatement statment = conn.prepareStatement(findEmployeeByEmail)) {
				statment.setString(1, toEmail);
				statment.setInt(2, number);
				do {
					try {
						result = statment.executeQuery();
						catchTimeOut = false;
					} catch (SQLTimeoutException e) {
						catchTimeOut = true;
						if (retries++ > 3)
							throw e;
					}
				} while (catchTimeOut);

				// check if email exists in the Data base if resultSet is empty.
				if (!result.next())
					throw new EmployeeException("Email not found");

				/*
				 * generatePassword takes a number as an input (must be greater than 6) and
				 * generates a random password including numbers and symbols.
				 * 
				 * @return A string.
				 */
				newPassword = generatePassword(6);

				/*
				 * before sending the email, new password must be updated and saved in the
				 * database.
				 */
				System.out.println("here...");
				
					// employee = findEmployeeById(result.getInt(1)); // find gets the id of
					// employee and returns the employee

					try {
						employee.setEmployee((find(result.getInt("id")).getEmployee())); // find gets the id of employee
																							// and returns the employee
						System.out.println("found employee...");
						employee.getEmployee().setPassword(PasswordUtils.generateSecurePassword(newPassword));
						update(employee); // update the new password of this employee in the database.
						System.out.println("updated...");
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println("can't continue from here.......");
					}
				
				String firstName = result.getString("first_name");

				String subject = mail.getSubject();
				String text = mail.getText() == null ? " " : mail.getText();

				// in the text file, replace the ##USER with the username and ##PWD with
				// newPassword.
				if (text != " ") {
					text.replaceFirst("##USER", firstName);
					text.replaceFirst("##PWD", newPassword);
				}

				try {
					sendGeneralEmail(toEmail, firstName, subject, text);
					System.out.println("sent...");
				} catch (SendFailedException e) {
					System.out.println("send failed! " + e.getMessage());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (EmployeeException e) {
			e.printStackTrace();
		}

	}

	private static boolean isValid(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}

	private static String generatePassword(int length) {
		Random random = new Random();
		Random random2 = new Random();
		String capitalLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String smallLetters = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
//		String symbols = "~!@#$%=+_{}|/:;^[ ]&*?";
		String[] allCombinations = { capitalLetters, smallLetters, numbers };// symbols };

		StringBuilder password = new StringBuilder();
		int j, i = 0;

		while (i < length) {
			password.append(allCombinations[j = (random2.nextInt(allCombinations.length))]
					.charAt(random.nextInt(allCombinations[j].length())));
			i++;
		}

		return password.toString();
	}

	// done Exceptions
	public void sendGeneralEmail(String toEmail, String firstName, String subject, String text)
			throws SendFailedException {

		final String fromEmail = mail.getFromEmail();
		final String password = mail.getFromPassword();

		if (!isValid(toEmail)) {
			throw new SendFailedException("email not valid.");
		}

		Properties properties = new Properties(); // for sending email
		properties.put("mail.smtp.auth", mail.getAuthentication());// authentication
		properties.put("mail.smtp.starttls.enable", mail.getEncryption()); // tls encryption
		properties.put("mail.smtp.host", mail.getHost()); // host: Gmail
		properties.put("mail.smtp.port", mail.getPort()); // SMTP port for Gmail is '587'

		// anonymous class
		javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			message.setSubject(subject);
			message.setText(text);

			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new SendFailedException("can't send email" + e.getMessage());
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new SendFailedException("Read-Only error" + e.getMessage());
		}
	}

	
	
	public Map<EmployeeData, List<EmployeeData>> FindEmployeesHierarchy() throws SQLException {

		List<EmployeeData> allEmployees = findAllEmployees();
		Map<Integer, List<Integer>> map = new HashMap<>();

		if (!(allEmployees.isEmpty())) {
			// for each manager, find all his subordinates.
			for (EmployeeData employee : allEmployees) {
				if (map.containsKey(employee.getEmployee().getManagerId())) {
					map.get(employee.getEmployee().getManagerId()).add(employee.getEmployee().getId());
				} else {
					map.put(employee.getEmployee().getManagerId(), new ArrayList<Integer>());
					map.get(employee.getEmployee().getManagerId()).add(employee.getEmployee().getId());
				}
			}
			// for each manager, find all his managers.
			if (!(map.keySet().isEmpty())) {
				for (Integer managerId : map.keySet()) {
					boolean found = false;
					if (!map.values().isEmpty()) {
						for (List<Integer> list : map.values()) {
							if (!((map.get(managerId)).equals(list))) {
								if (list.contains(managerId)) {
									list.remove(managerId);
									list.addAll(map.get(managerId));
									map.get(managerId).clear();
									found = true;
								}
							}
						}
					}
					if (!found) {
						for (Integer manager2Id : map.keySet()) {
							if (map.get(managerId).contains(manager2Id)) {
								map.get(managerId).remove(manager2Id);
								map.get(managerId).addAll(map.get(manager2Id));
								map.get(manager2Id).clear();
							}
						}
					}
				}
			}

			Map<EmployeeData, List<EmployeeData>> returnMap = new HashMap<>(map.size());

			for (List<EmployeeData> l : returnMap.values())
				l = new ArrayList<>();

			List<EmployeeData> managerEmployees = new ArrayList<>();
//done until here
			for (Integer managerId : map.keySet()) {
				managerEmployees.clear();

				if (!(map.get(managerId).isEmpty())) {
					for (Integer employeeId : map.get(managerId))
						managerEmployees.add(find(employeeId));
					returnMap.put(find(managerId), managerEmployees);
				}
			}
			System.out.println("Done");
			return returnMap;
		}
		return null;
	}

}



