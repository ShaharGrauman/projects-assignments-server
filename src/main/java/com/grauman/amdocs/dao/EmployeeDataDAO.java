package com.grauman.amdocs.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
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
import com.grauman.amdocs.errors.custom.AlreadyExistsException;
import com.grauman.amdocs.mail.MailManager;

@Service
public class EmployeeDataDAO implements IEmployeeDataDAO {
	@Autowired
	DBManager db;
	@Autowired
	LoginDAO loginAttemptes;

	@Autowired
	MailManager mail;
	/**
	    * @param page
	    * @param limit
	    * @return all locked employees
	    * @throws SQLException
	    */
//Search all employees which are locked
	@Override
	public List<EmployeeData> findAll(int page,int limit) throws SQLException {
		int userId;
		if(page<1)
			  page=1;
		int offset=(page-1)*limit;
		List<EmployeeData> users = new ArrayList<>();
		String sqlAllUserscommand = "select  U.id,U.employee_number,U.first_name,U.last_name,U.manager_id,U.department,"
								+ "WS.name as worksite,WS.city,C.name as country,U.locked,U.deactivated "
								+ "From users U JOIN worksite WS ON U.work_site_id=WS.id "
								+ "JOIN country C ON WS.country_id=C.id "
								+ "where U.locked=true order by U.employee_number"
								+" limit ? offset ?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlAllUserscommand)) {
			command.setInt(1, limit);
			command.setInt(2, offset);
				ResultSet result = command.executeQuery();

				while (result.next()) {
					userId = result.getInt(1);
					List<Role> roles = new ArrayList<>();
					roles = getEmployeeRoles(userId);
					users.add(new EmployeeData(new Employee(
												result.getInt("U.id"),
												result.getInt("U.employee_number"),
												result.getString("U.first_name"),
												result.getString("U.last_name"),
												result.getInt("U.manager_id"),
												result.getString("U.department"),
												new WorkSite(result.getString("WS.name"), result.getString("WS.city")),
												new Country(result.getString("C.name")),
												result.getBoolean("U.locked"),
												result.getBoolean("U.deactivated")), roles));
				}
			}
		}
		return users;
	}
	/**
	    * @param page
	    * @param limit
	    * @return all employees
	    * @throws SQLException
	    */
//search all employees	
	public List<EmployeeData> findAllEmployees(int page,int limit) throws SQLException {
		int userId;
		if(page<1)
			  page=1;
		int offset=(page-1)*limit;
    	List<EmployeeData> users=new ArrayList<EmployeeData>();
		String sqlAllUserscommand="select  U.id,U.employee_number,U.first_name,U.last_name,U.manager_id,"
								+ "U.department,WS.name,WS.city,C.name,U.locked,U.deactivated "
								+ " From users U JOIN worksite WS ON U.work_site_id=WS.id"
								+ " JOIN country C ON WS.country_id=C.id order by U.employee_number"
								+" limit ? offset ?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlAllUserscommand)) {
				command.setInt(1, limit);
				command.setInt(2, offset);
				ResultSet result=command.executeQuery();
				while(result.next()) {
					userId=result.getInt(1);
					List<Role> roles=new ArrayList<>();
					roles=getEmployeeRoles(userId);
					users.add(new EmployeeData(new Employee(
												result.getInt("U.id"),
												result.getInt("U.employee_number"),
												result.getString("U.first_name"),
												result.getString("U.last_name"),
												result.getInt("U.manager_id"),
												result.getString("U.department"),
												new WorkSite(result.getString("WS.name"), result.getString("WS.city")),
												new Country(result.getString("C.name")),
												result.getBoolean("U.locked"),
												result.getBoolean("U.deactivated")), roles));
				}
			}
		}
		return users;
	}

	/**
	    * @param id 
	    * @return find employee by id
	    * @throws SQLException
	    */
// search the employee profile 
	public EmployeeData searchEmployeeProfile(int id) throws SQLException {
		Date auditDate=null;
		int employeeId;
		EmployeeData found = null;
		List<Role> roles = new ArrayList<>();
		String sqlFindLastLogin="SELECT max(date_time) as LastLogin FROM audit"
						  + " Where user_id=?";
		
		String sqlFindEmployee = "Select U1.*,concat(U2.first_name,' ',U2.last_name) as manager_name,"
								+ " WS.id as work_site_id,WS.name as work_site_name,"
								+ "WS.city as work_site_city, C.id as country_id,C.name as country_name"
								+ " From users U1 JOIN users U2 ON U1.manager_id=U2.id "
								+ "JOIN worksite WS ON U1.work_site_id=WS.id "
								+ "JOIN country C ON WS.country_id=C.id"
								+ " Where U1.work_site_id=WS.id AND U1.id=? AND U1.deactivated=false ";
						
		String sqlEmployeeRoles = "Select R.*" 
								+ " From roles R JOIN userrole UR ON R.id=UR.role_id "
								+ "Where UR.user_id=?";

		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command0 = conn.prepareStatement(sqlFindLastLogin)) {
				command0.setInt(1, id);
				ResultSet result0 = command0.executeQuery();

				if(result0.next()) {
					auditDate=result0.getDate(1);
				}
				try (PreparedStatement command = conn.prepareStatement(sqlFindEmployee)) {
					command.setInt(1, id);
					ResultSet result = command.executeQuery();
					if(result.next()) {
						employeeId = result.getInt(1);
					
						try (PreparedStatement command2 = conn.prepareStatement(sqlEmployeeRoles)) {
							command2.setInt(1, employeeId);
							ResultSet result2 = command2.executeQuery();
		
							while (result2.next()) {
								roles.add(new Role(result2.getInt(1),
										   result2.getString(2),
										   result2.getString(3)));
							}
						}
					}

					found =new EmployeeData(new Employee(
							  result.getInt("U1.id"),
							  result.getInt("U1.employee_number"),
							  result.getString("U1.first_name"),
							  result.getString("U1.last_name"),
							  result.getString("U1.email"),
							  result.getInt("U1.manager_id"),
							  result.getString("U1.department"),
							  new WorkSite(result.getInt("work_site_id"),result.getString("work_site_name"),
									  new Country(result.getInt("country_id"),result.getString("country_name")),result.getString("work_site_city")),
							  result.getString("U1.phone"),
							  result.getBoolean("U1.login_status"),
							  result.getBoolean("U1.locked"),
							  result.getString("U1.image"),
							  result.getBoolean("U1.deactivated")),
							
    						  result.getString("manager_name"),auditDate,roles);
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
		String sqlFindEmployee = "Select U1.*,U2.first_name as manager_name,"
								+ " WS.id as work_site_id,WS.name as work_site_name,WS.city as work_site_city,"
								+ " C.id as country_id,C.name as country_name"
								+ " From users U1 JOIN users U2 ON U1.manager_id=U2.id"
								+ " JOIN worksite WS ON U1.work_site_id=WS.id"
								+ " JOIN country C ON WS.country_id=C.id"
								+ " Where U1.work_site_id=WS.id AND U1.id=?";
		
		String sqlEmployeeRoles = "Select R.*" 
								+ " From roles R JOIN userrole UR ON R.id=UR.role_id "
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
							roles.add(new Role(result2.getInt(1),
											   result2.getString(2),
											   result2.getString(3)));
						}
						found = new EmployeeData(new Employee(
								  result.getInt("U1.id"),
								  result.getInt("U1.employee_number"),
								  result.getString("U1.first_name"),
								  result.getString("U1.last_name"),
								  result.getString("U1.email"),
								  result.getInt("U1.manager_id"),
								  result.getString("U1.department"),
								  new WorkSite(result.getInt("work_site_id"),result.getString("work_site_name"),
										  new Country(result.getInt("country_id"),result.getString("country_name")),result.getString("work_site_city")),
								  result.getString("U1.phone"),
								  result.getBoolean("U1.login_status"),
								  result.getBoolean("U1.locked"),
								  result.getBoolean("U1.deactivated")),
								  result.getString("manager_name"),roles);
						  }
				}
			}
		}
		return found;
	}

	// *******************************************************************************************
// we should generate a random password and then insert it to the uses table in the data base
	/**
	    * @param employee 
	    * @return new added employee
	    * @throws SQLException
	    */
	@Override
	public EmployeeData add(EmployeeData employee) throws SQLException {
		int newEmployeeId = -1;
		EmployeeData newEmployee = null;
		ResultSet exists = null;

		List<Role> roles = employee.getRoles();
		String checkIfEmployeeExists = "select * from users where employee_number=?";
		String sqlAddEmployeeStatement = "Insert INTO users (employee_number,first_name,last_name,email,manager_id,"
				+ "department,work_site_id,country,phone,login_status,locked,deactivated,password,image)"
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement state = conn.prepareStatement(checkIfEmployeeExists)) {
				state.setInt(1,employee.getEmployee().getNumber());
				exists = state.executeQuery();
				if (exists.next()) {
					throw new AlreadyExistsException("Employee Number already exists");
				}
				try (PreparedStatement statement = conn.prepareStatement(sqlAddEmployeeStatement,
						Statement.RETURN_GENERATED_KEYS)) {
	
					statement.setInt(1, employee.getEmployee().getNumber());
					statement.setString(2, employee.getEmployee().getFirstName());
					statement.setString(3, employee.getEmployee().getLastName());
					statement.setString(4, employee.getEmployee().getEmail());
					statement.setInt(5, employee.getEmployee().getManagerId());
					statement.setString(6, employee.getEmployee().getDepartment());
					statement.setInt(7, employee.getEmployee().getWorksite().getId());
					statement.setString(8, employee.getEmployee().getWorksite().getCountry().getName());
					statement.setString(9, employee.getEmployee().getPhone());
					statement.setBoolean(10, employee.getEmployee().getLoginStatus());
					statement.setBoolean(11, employee.getEmployee().getLocked());
					statement.setBoolean(12, employee.getEmployee().getDeactivated());
					// change it to the generated password!
					statement.setString(13, EmployeeDataDAO.generatePassword(6));
					statement.setString(14, employee.getEmployee().getImage());
	
					int rowCountUpdated = statement.executeUpdate();
	
					ResultSet ids = statement.getGeneratedKeys();
	
					while (ids.next()) {
						newEmployeeId = ids.getInt(1);
						// find employee by ID
						newEmployee = find(newEmployeeId);
					}
				}
	            
	            String sqlAddRoleToEmployee="Insert into userrole (user_id,role_id) values(?,?)";
	            try(PreparedStatement statement1=conn.prepareStatement(sqlAddRoleToEmployee)){
	                for(int i=0;i<roles.size();i++) {
	                    statement1.setInt(1, newEmployeeId);
	                    statement1.setInt(2, roles.get(i).getId());
	                    
	                    int rowCountUpdated=statement1.executeUpdate();
	                }
	            }
	            newEmployee=find(newEmployeeId);
	        }
		
		}
		return newEmployee;
	}

//never change the employee number!!
	/**
	    * @param id 
	    * @param employee
	    * @return update employee
	    * @throws SQLException
	    */
	@Override
	public EmployeeData update(EmployeeData employee) throws SQLException {

		EmployeeData updatedEmployee=null;
        List<Role> roles=new ArrayList<>();
        String sqlDelEmployeeStatement = "update users set first_name=?,last_name=?,"
						                + "email=?,manager_id=?,department=?,work_site_id=?,"
						                + "country=?,phone=?,login_status=?,locked=?,deactivated=? "
						                + "where id=?";
        try (Connection conn = db.getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(sqlDelEmployeeStatement)) {
            	//statement.setInt(1, employee.getEmployee().getNumber());
				statement.setString(1, employee.getEmployee().getFirstName());
				statement.setString(2, employee.getEmployee().getLastName());
				statement.setString(3, employee.getEmployee().getEmail());
				statement.setInt(4, employee.getEmployee().getManagerId());
				statement.setString(5, employee.getEmployee().getDepartment());
				statement.setInt(6, employee.getEmployee().getWorksite().getId());
				statement.setString(7, employee.getEmployee().getCountry().getName());
				statement.setString(8, employee.getEmployee().getPhone());
				statement.setBoolean(9, employee.getEmployee().getLoginStatus());
				statement.setBoolean(10, employee.getEmployee().getLocked());
				statement.setBoolean(11, employee.getEmployee().getDeactivated());

                statement.setInt(12, employee.getEmployee().getId());
                int rowCountUpdated = statement.executeUpdate();
	            }
            	
	            String deleteRoles="delete from userrole where user_id=?";
	            try(PreparedStatement statement=conn.prepareStatement(deleteRoles)){
	                roles=employee.getRoles();
	                for(int i=0;i<roles.size();i++) {
		                statement.setInt(1,employee.getEmployee().getId());
		                int rowCountUpdated=statement.executeUpdate();
	                }
	            }
	            String sqlAddRoleToEmployee="Insert into userrole (user_id,role_id) values(?,?)";
	            try(PreparedStatement statement1=conn.prepareStatement(sqlAddRoleToEmployee)){
	                for(int i=0;i<roles.size();i++) {
	                    statement1.setInt(1,employee.getEmployee().getId());
	                    statement1.setInt(2, roles.get(i).getId());
	                    
	                    int rowCountUpdated=statement1.executeUpdate();
	                }
	            }
        }
        updatedEmployee = find(employee.getEmployee().getId());
        return updatedEmployee;

	}
	/**
	    * @param id 
	    * @return deleted/deactivated employee
	    * @throws SQLException
	    */
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
	public List<EmployeeData> filterByName(String name,int page,int limit) throws SQLException {
			
		List <EmployeeData> found = new ArrayList<>();
		List<Role> employeeRoles=new ArrayList<>();
		if(page<1)
			  page=1;
		int offset=(page-1)*limit;
		//remove all the white space
		name=name.toLowerCase().trim();
		String sqlFindCommand ="select U.id,U.employee_number,LOWER(U.first_name),LOWER(U.last_name),"
								+ "U.department,WS.name,WS.city,C.name,U.locked,U.deactivated  "
								+ "From users U JOIN worksite WS ON U.work_site_id=WS.id"
								+ " JOIN country C ON WS.country_id=C.id "
								+ "where concat(LOWER(U.first_name),' ',LOWER(U.last_name)) like ?"
								+" limit ? offset ?";
				
			try (Connection conn = db.getConnection()) {
				try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
				 command.setString(1,name);
				 command.setInt(2, limit);
				 command.setInt(3, offset);
					ResultSet result = command.executeQuery();
					while(result.next()) {
						employeeRoles=getEmployeeRoles(result.getInt(1));
						found.add(new EmployeeData(new Employee(
								result.getInt(1),
								result.getInt(2),
								result.getString(3),
								result.getString(4),
								null,
								result.getString(8),
								new WorkSite(result.getString(5),result.getString(6)),
								new Country(result.getString(7)),
								result.getBoolean(9),
								result.getBoolean(10)),employeeRoles));
					}
					}}
				 catch (Exception e) {
					e.printStackTrace();
				}
		return found;
	}

//filter
	//show just the activated Employee 
	public List<EmployeeData> filter(int number,String roleName,String siteName,String departmentName,
			String countryName,int page,int limit)throws SQLException{
		  List <EmployeeData> found = new ArrayList<>();
		  List<Role> employeeRoles=new ArrayList<>();
		  if(page<1)
			  page=1;
		  int offset=(page-1)*limit;
		  
		  List<String> conditions = new ArrayList<>();
		  
		  if(number !=0) conditions.add(" U.employee_number=? ");
		  if(!roleName.isBlank()) conditions.add(" R.name=? ");
		  if(!siteName.isBlank()) conditions.add(" WS.city=? ");
		  if(!departmentName.isBlank()) conditions.add(" U.department=? ");
		  if(!countryName.isBlank()) conditions.add(" U.country=? ");
		  
		  String sqlFindCommand ="select U.id,U.employee_number,U.first_name,U.last_name,"
	  				+ "U.department,WS.name,WS.city,C.name,U.locked,U.deactivated  "
	  				+ " From users U "
	  				+ " JOIN userrole UR ON UR.user_id=U.id"
	  				+ " JOIN roles R ON R.id=UR.role_id"
	  				+ " JOIN worksite WS ON U.work_site_id=WS.id"
	  				+ " JOIN country C ON WS.country_id=C.id"
	  				+ " where "
	  				+ String.join(" and ", conditions)
	  				+ " Group by U.id order by U.employee_number"
	  				+" limit ? offset ?";
		  
		  System.out.println(sqlFindCommand);
			try (Connection conn = db.getConnection()) {
			    try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
			    	int counter = 1;
			      if(number!=0)
			    	  command.setInt(counter++,number);
			      if(!roleName.isBlank())
			    	  command.setString(counter++,roleName);
			      if(!siteName.isBlank())
			    	  command.setString(counter++,siteName);
			      if(!departmentName.isBlank())
			    	  command.setString(counter++,departmentName);
			      if(!countryName.isBlank())
			    	  command.setString(counter++,countryName);
			       
			       command.setInt(counter++, limit);
			       command.setInt(counter++, offset);
			       System.out.println(command);
			       ResultSet result = command.executeQuery();
			        
			      while(result.next()) {
			            employeeRoles=getEmployeeRoles(result.getInt(1));
			            found.add(new EmployeeData(new Employee(
								result.getInt(1),
								result.getInt(2),
								result.getString(3),
								result.getString(4),
								null,
								result.getString(8),
								new WorkSite(result.getString(5),result.getString(6)),
								new Country(result.getString(7)),
								result.getBoolean(9),
								result.getBoolean(10)),employeeRoles));
			          }
			      }
			  }
			  catch (Exception e) {
			      e.printStackTrace();
			   }
			return found;
		}


/**call the resetAttempts from LoginDAO */
	/**
	    * @param id 
	    * @return unlocked employee 
	    * @throws SQLException
	    */
//unlock user
	public EmployeeData unlockEmployee(int id) throws SQLException {
		String sqlUnlockEmployeeStatement = "update users set locked=false where id=?";
		EmployeeData unLockedEmployee = null;
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement statment = conn.prepareStatement(sqlUnlockEmployeeStatement)) {
				statment.setInt(1, id);

				int res = statment.executeUpdate();
				unLockedEmployee = find(id);
			}
		}
		loginAttemptes.resetAttempts(unLockedEmployee.getEmployee().getEmail());
		return unLockedEmployee;
	}
	/**
	    * @param id 
	    * @return locked employee 
	    * @throws SQLException
	    */
//lock Employee after 3 attempts
	public EmployeeData lockEmployee(int id) throws SQLException {
		String sqlLockEmployeeStatement = "update users set locked=true where id=?";
		EmployeeData lockedEmployee = null;
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement statment = conn.prepareStatement(sqlLockEmployeeStatement)) {
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
		String sqlFindRoles = "SELECT R.id,R.name"
							+ " FROM roles R join userrole US ON R.id=US.role_id"
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
	/** 
	    * @return all sites 
	    * @throws SQLException
	    */
// get all sites 
	public List<WorkSite> findAllSites() throws SQLException {
		List<WorkSite> sites = new ArrayList<WorkSite>();
		String sqlSitesCommand = "Select WS.id,WS.name,WS.country_id,C.name,WS.city "
								+ "from worksite WS JOIN country C ON WS.country_id=C.id";
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery(sqlSitesCommand);
				while (result.next()) {
					sites.add(new WorkSite(result.getInt("WS.id"),
										   result.getString("WS.name"),
										   new Country(result.getInt("WS.country_id"),result.getString("C.name")),
										   result.getString("WS.city")));
				}
			}
		}
		return sites;
	}
	/** 
	    * @return all roles 
	    * @throws SQLException
	    */
// get all roles
	public List<Role> findAllRoles() throws SQLException {
		List<Role> roles = new ArrayList<Role>();
		String sqlSitesCommand = "Select id,name,description From roles";
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery(sqlSitesCommand);
				while (result.next()) {
					roles.add(new Role(result.getInt("id"), result.getString("name"),result.getString("description")));
				}
			}
		}
		return roles;
	}
	/** 
	    * @return all departments 
	    * @throws SQLException
	    */
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
	/** 
	    * @return all managers 
	    * @throws SQLException
	    */
// get all Managers (Name+ID)
	public List<Employee> findAllManagers() throws SQLException {
		List<Employee> managers = new ArrayList<Employee>();
		String sqlSitesCommand = "select U1.id,U1.employee_number,U1.first_name ,U1.last_name"
								+ " From users U1 JOIN users U2 ON U1.id=U2.manager_id "
								+ "JOIN userrole UR ON U1.id=UR.user_id JOIN roles R ON UR.role_id=R.id "
								+ "Where R.name='manager' "
								+ "Group By U1.id";

		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {

				ResultSet result = command.executeQuery(sqlSitesCommand);
				while (result.next()) {
					managers.add(new Employee(
											  result.getInt("U1.id"),
											  result.getInt("U1.employee_number"),
											  result.getString("U1.first_name"),
											  result.getString("U1.last_name")));
				}
			}
		}
		return managers;
	}

	 /** 
	    * @return all countries 
	    * @throws SQLException
	    */
//get all countries
	public List<Country> findAllCountries() throws SQLException {
		List<Country> countries = new ArrayList<>();
		String sqlCountriesCommand = "select * from country";
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery(sqlCountriesCommand);
				while (result.next()) {
					countries.add(new Country(result.getInt(1), result.getString(2)));
				}
			}
		}
		return countries;
	}

	/** 
	    * @return number of employees 
	    * @throws SQLException
	    */
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
	/** 
	    * @return number of roles 
	    * @throws SQLException
	    */
	public Integer countRoles() throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery("select count(*) from roles");
				result.next();
				return result.getInt("count(*)");
			}
		}
	}
	/** 
	    * @return number of departments 
	    * @throws SQLException
	    */
	public Integer countDepartments() throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery("select count(*) from department");
				result.next();
				return result.getInt("count(*)");
			}
		}
	}
	/** 
	    * @return number of countries 
	    * @throws SQLException
	    */
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
	@SuppressWarnings("null")
	public void resetPassword(String toEmail, int number) throws SQLException, EmployeeException {
		boolean catchTimeOut = false;
		int retries = 0;
		String findEmployeeByEmail = "SELECT * from users where email=? AND employee_number=?";
		String updatePasswordInDataBase = "update users set password=? where id=?";
		String newPassword;
		EmployeeData employee = null;
		ResultSet result = null;
		int result2 =0;

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
					// employee = findEmployeeById(result.getInt(1)); // find gets the id of
					// employee and returns the employee

					try {
						employee =  find(result.getInt("id")); // find gets the id of employee
						employee.getEmployee().setPassword(newPassword);

						retries=0;
						try(PreparedStatement statement2= conn.prepareStatement(updatePasswordInDataBase)){
							statement2.setString(1, newPassword);
							statement2.setInt(2, employee.getEmployee().getId());
							do {
								try {
									result2 = statement2.executeUpdate();
									catchTimeOut = false;
								} catch (SQLTimeoutException e) {
									catchTimeOut = true;
									if (retries++ > 3)
										throw e;
								}
							} while (catchTimeOut);
						
						}
						
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
					text = text.replaceAll("##USER", firstName);
					text = text.replaceAll("##PWD", newPassword);
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
			message.setContent(text, "text/html; charset=utf-8");
//			message.setText();

			Transport.send(message);

		} catch (MessagingException e) { // stuck here
			e.printStackTrace();
			throw new SendFailedException("can't send email" + e.getMessage());
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new SendFailedException("Read-Only error" + e.getMessage());
		}
	}	

 public Map<EmployeeData, List<EmployeeData>> findEmployeesHierarchy() throws SQLException {

		List<EmployeeData> allEmployees = findAllEmployees(1,10);
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
			// done until here
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
  
	@Override
	public List<Permission> getEmployeePermissions(Integer id) throws SQLException {
		List<Permission> permissions = new ArrayList<>();

		String fetchPermissions = "SELECT P.id, P.name FROM userrole ER INNER JOIN rolepermissions RP on ER.role_id = RP.role_id INNER JOIN permissions P on P.id = RP.permission_id WHERE ER.user_id = ?";

		try (Connection conn = db.getConnection()){
			try(PreparedStatement preparedStatement = conn.prepareStatement(fetchPermissions)){
				preparedStatement.setInt(1, id);

				try (ResultSet resultSet = preparedStatement.executeQuery()){
					while (resultSet.next()){
						permissions.add(new Permission(resultSet.getInt(1), resultSet.getString(2)));
					}
				}
			}
		}
		return permissions;
	}
  
  @Override
  public List<EmployeeData> findAll() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }
}