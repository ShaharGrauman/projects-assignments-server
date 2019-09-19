package com.grauman.amdocs.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.grauman.amdocs.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grauman.amdocs.dao.interfaces.IEmployeeDataDAO;

import com.grauman.amdocs.models.Country;
import com.grauman.amdocs.models.Department;
import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.WorkSite;


@Service
public class EmployeeDataDAO implements IEmployeeDataDAO {
	@Autowired
	DBManager db;
	@Autowired
	LoginDAO loginAttemptes;

//Search all employees which are locked
	@Override
	public List<EmployeeData> findAll() throws SQLException {
		int userId;
    	List<EmployeeData> users=new ArrayList<>();
		String sqlAllUserscommand="select  U.id,U.employee_number,U.first_name,U.last_name,U.department,"
								+ "WS.name as worksite,WS.city,C.name as country "
								+ "From users U JOIN worksite WS ON U.work_site_id=WS.id "
								+ "JOIN country C ON WS.country_id=C.id "
								+ "where U.locked=true";
		try (Connection conn = db.getConnection()) {
			try(Statement command = conn.createStatement()){
				ResultSet result=command.executeQuery(sqlAllUserscommand);
				
				while(result.next()) {
					userId=result.getInt(1);
					List<Role> roles=new ArrayList<>();
					roles=getEmployeeRoles(userId);
					users.add(new EmployeeData(new Employee(
											result.getInt(1),
											result.getInt(2),
											result.getString(3),
											result.getString(4),
											result.getString(5),
											new WorkSite(result.getString(6),result.getString(7)),
											new Country(result.getString(8))),roles));
										
					
				}
			}
		}
		return users;
	}
//search all employees	
	public List<EmployeeData> findAllEmployees() throws SQLException {
		int userId;
    	List<EmployeeData> users=new ArrayList<EmployeeData>();
		String sqlAllUserscommand="select  U.id,U.employee_number,U.first_name,U.last_name,U.manager_id,"
								+ "U.department,WS.name,WS.city,C.name "
								+ " From users U JOIN worksite WS ON U.work_site_id=WS.id"
								+ " JOIN country C ON WS.country_id=C.id";
		try (Connection conn = db.getConnection()) {
			try(Statement command = conn.createStatement()){
				ResultSet result=command.executeQuery(sqlAllUserscommand);
				
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
							new WorkSite(result.getString("WS.name"),result.getString("WS.city")),
							new Country(result.getString("C.name"))),roles));
				}
			}
		}
		return users;
	}
	
// the search will be by Employee Number 
	public EmployeeData findByEmployeeNumber(int number) throws SQLException {
		Date auditDate=null;
		int employeeId;
		EmployeeData found = null;
		List<Role> roles = new ArrayList<>();
		String sqlFindLastLogin="SELECT max(date_time) as LastLogin FROM audit"
						  + " Group by employee_number"
						  + " Having employee_number=?";
		
		String sqlFindEmployee = "Select U1.*,U2.first_name as manager_name,"
								+ " WS.id as work_site_id,WS.name as work_site_name,WS.city as work_site_city,"
								+ " C.id as country_id,C.name as country_name"
								+ " From users U1 JOIN users U2 ON U1.manager_id=U2.id"
								+ " JOIN worksite WS ON U1.work_site_id=WS.id"
								+ " JOIN country C ON WS.country_id=C.id"
								+ " Where U1.work_site_id=WS.id AND U1.employee_number=?";
						
		String sqlEmployeeRoles = "Select R.*" 
								+ " From roles R JOIN userrole UR ON R.id=UR.role_id "
								+ "Where UR.user_id=?";

		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command0 = conn.prepareStatement(sqlFindLastLogin)) {
				command0.setInt(1, number);
				ResultSet result0 = command0.executeQuery();
				if(result0.next()) {
					auditDate=result0.getDate(1);
				}
				try (PreparedStatement command = conn.prepareStatement(sqlFindEmployee)) {
					command.setInt(1, number);
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
				if(result.next()) {
				    employeeId = result.getInt("U1.id");
					try (PreparedStatement command2 = conn.prepareStatement(sqlEmployeeRoles)) {
						command2.setInt(1, employeeId);
						ResultSet result2 = command2.executeQuery();
	
						while (result2.next()) {
							roles.add(new Role(result2.getInt(1),
											   result2.getString(2),
											   result2.getString(3)));
						}
						System.out.println(roles);
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
	//*******************************************************************************************
// we should generate a random password and then insert it to the uses table in the data base
	@Override
	public EmployeeData add(EmployeeData employee) throws SQLException {
		int newEmployeeId = -1;
		EmployeeData newEmployee = null;
		
        List<Role> roles=employee.getRoles();
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
				//change it to the generated password!
				statement.setString(13, employee.getEmployee().getPassword());

				int rowCountUpdated = statement.executeUpdate();

				ResultSet ids = statement.getGeneratedKeys();

				while (ids.next()) {
					newEmployeeId = ids.getInt(1);
					//find employee by ID
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
        }
		return find(newEmployeeId);
	}

//never change the employee number!!
	@Override
	public EmployeeData update(EmployeeData employee) throws SQLException {

		EmployeeData updatedEmployee=null;
        List<Role> roles=new ArrayList<>();
        String sqlDelEmployeeStatement = "update users set first_name=?,last_name=?,"
                + "email=?,manager_id=?,department=?,work_site_id=?,"
                + "country=?,phone=?,login_status=?,locked=?,deactivated=? where id=?";
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
			
		List <EmployeeData> found = new ArrayList<>();
		List<Role> employeeRoles=new ArrayList<>();	
		//remove all the white space
		name=name.toLowerCase().trim();
		String sqlFindCommand ="select U.id,U.employee_number,LOWER(U.first_name),LOWER(U.last_name),"
								+ "U.department,WS.name,WS.city,C.name "
								+ "From users U JOIN worksite WS ON U.work_site_id=WS.id"
								+ " JOIN country C ON WS.country_id=C.id "
								+ "where concat(LOWER(U.first_name),' ',LOWER(U.last_name)) like ?";
				
			try (Connection conn = db.getConnection()) {
				try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
				 command.setString(1,name);
					ResultSet result = command.executeQuery();
					if(result.next()) {
						employeeRoles=getEmployeeRoles(result.getInt(1));
						found.add(new EmployeeData(new Employee(
								result.getInt(1),
								result.getInt(2),
								result.getString(3),
								result.getString(4),
								result.getString(5),
								new WorkSite(result.getString(6),result.getString(7)),
								new Country(result.getString(8))),employeeRoles));
					}
					}}
				 catch (Exception e) {
					e.printStackTrace();
				}
				return found;
			}
//By Role
	public List<EmployeeData> filterByRole(String roleName) throws SQLException{
		List <EmployeeData> found = new ArrayList<>();
		List<Role> employeeRoles=new ArrayList<>();
			String sqlFindCommand ="SELECT U.id, U.employee_number,U.first_name,U.last_name,"
                + "W.name as workSiteName,W.city,U.country, U.department"
                + " FROM users U JOIN worksite W ON U.work_site_id=W.id"
                + " JOIN userrole UR ON UR.user_id=U.id"
                + " JOIN roles R ON R.id=UR.role_id"
                + " where R.name=?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
			 command.setString(1,roleName);
				ResultSet result = command.executeQuery();
			
				while(result.next()) {
					employeeRoles=getEmployeeRoles(result.getInt(1));
					found.add(new EmployeeData(new Employee(
							result.getInt(1),
							result.getInt(2),
							result.getString(3),
							result.getString(4),
							result.getString(5),
							new WorkSite(result.getString(6),result.getString(7)),
							new Country(result.getString(8))),employeeRoles));
				}
			}
		} 
		 catch (Exception e) {
			e.printStackTrace();
		}
		return found;
	}
		
//By Department
	public List<EmployeeData> filterByDepartment(String departmentName) throws SQLException{
		List <EmployeeData> found = new ArrayList<>();
		List<Role> employeeRoles=new ArrayList<>();
			String sqlFindCommand ="select DISTINCT U.id,U.employee_number,U.first_name,U.last_name,"
				+ "U.department,WS.name,WS.city,C.name "
				+ " From users U JOIN worksite WS ON U.work_site_id=WS.id"
				+ " JOIN country C ON WS.country_id=C.id"
				+ " where U.department=?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
			 command.setString(1,departmentName);
				ResultSet result = command.executeQuery();
			
				while(result.next()) {
					employeeRoles=getEmployeeRoles(result.getInt(1));
					found.add(new EmployeeData(new Employee(
							result.getInt(1),
							result.getInt(2),
							result.getString(3),
							result.getString(4),
							result.getString(5),
							new WorkSite(result.getString(6),result.getString(7)),
							new Country(result.getString(8))),employeeRoles));
				}
			}
		} 
		 catch (Exception e) {
			e.printStackTrace();
		}
			return found;
}
//By WorkSite
	public List<EmployeeData> filterByWorkSite(String siteName) throws SQLException{
		List <EmployeeData> found = new ArrayList<>();
		List<Role> employeeRoles=new ArrayList<>();
			String sqlFindCommand ="select U.id,U.employee_number,U.first_name,U.last_name,"
				+ "U.department,WS.name,WS.city,C.name "
				+ " From users U JOIN worksite WS ON U.work_site_id=WS.id"
				+ " JOIN country C ON WS.country_id=C.id"
				+ " where WS.city=?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
			 command.setString(1,siteName);
				ResultSet result = command.executeQuery();
			
				while(result.next()) {
					employeeRoles=getEmployeeRoles(result.getInt(1));
					found.add(new EmployeeData(new Employee(
							result.getInt(1),
							result.getInt(2),
							result.getString(3),
							result.getString(4),
							result.getString(5),
							new WorkSite(result.getString(6),result.getString(7)),
							new Country(result.getString(8))),employeeRoles));
				}
			}
		} 
		 catch (Exception e) {
			e.printStackTrace();
		}
		return found;
	}
//search for all the employees which are working in the selected country
	public List<EmployeeData> filterByCountry(String countryName)throws SQLException{
	  List <EmployeeData> found = new ArrayList<>();
	  List<Role> employeeRoles=new ArrayList<>();
      String sqlFindCommand ="select U.id,U.employee_number,U.first_name,U.last_name,"
    		  				+ "U.department,WS.name,WS.city,C.name "
						+ " From users U JOIN worksite WS ON U.work_site_id=WS.id"
						+ " JOIN country C ON WS.country_id=C.id"
		              + " where U.country=?";
		try (Connection conn = db.getConnection()) {
		    try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
		       command.setString(1,countryName);
		       ResultSet result = command.executeQuery();
		        
		      while(result.next()) {
		            employeeRoles=getEmployeeRoles(result.getInt(1));
		            found.add(new EmployeeData(new Employee(
							result.getInt(1),
							result.getInt(2),
							result.getString(3),
							result.getString(4),
							result.getString(5),
							new WorkSite(result.getString(6),result.getString(7)),
							new Country(result.getString(8))),employeeRoles));
		          }
		      }
		  }
		  catch (Exception e) {
		      e.printStackTrace();
		   }
		return found;
	}
//*******************************************very important!	**********************
/**call the resetAttempts from LoginDAO */
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
	public List<Role> getEmployeeRoles(int id)throws SQLException{
  		List<Role> employeeRoles=new ArrayList<>();
  		String sqlFindRoles="SELECT R.id,R.name" + 
  				" FROM roles R join userrole US ON R.id=US.role_id" + 
  				" WHERE US.user_id=?";
  		try(Connection conn = db.getConnection()){
  		try(PreparedStatement command=conn.prepareStatement(sqlFindRoles)){
  			 command.setInt(1,id);
  				ResultSet result = command.executeQuery();
  				while(result.next()) {
  					employeeRoles.add(new Role(
  							result.getInt(1),
  							result.getString(2)));
  				}
  			}
  		}
  		return employeeRoles;
  	}

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
		String sqlSitesCommand = "select DISTINCT U1.id,U1.employee_number,U1.first_name ,U1.last_name"
								+ " From users U1 JOIN users U2 ON U1.id=U2.manager_id "
								+ "JOIN userrole UR ON U1.id=UR.user_id JOIN roles R ON UR.role_id=R.id "
								+ "Where R.name='manager'";

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
//get all countries
    public List<Country> findAllCountries() throws SQLException {
        List<Country> countries = new ArrayList<>();
        String sqlDepartmetsCommand = "select * from country";
        try (Connection conn = db.getConnection()) {
            try (Statement command = conn.createStatement()) {
                ResultSet result = command.executeQuery(sqlDepartmetsCommand);
                while (result.next()) {
                    countries.add(new Country(result.getInt("id"),result.getString("name")));
                }
            }
        }
        return countries;
    }            
   
 //***************************************************************************************************
 //counters for the Home Page
 public Integer countEmployees() throws SQLException{
      try(Connection conn=db.getConnection()){
          try(Statement command=conn.createStatement()){
              ResultSet result=command.executeQuery("select count(*) from users");
              result.next();
             return result.getInt("count(*)");
          }
      }
 }
 public Integer countRoles() throws SQLException{
      try(Connection conn=db.getConnection()){
          try(Statement command=conn.createStatement()){
              ResultSet result=command.executeQuery("select count(*) from roles");
              result.next();
             return result.getInt("count(*)");
          }
      }
 }
 public Integer countDepartments() throws SQLException{
      try(Connection conn=db.getConnection()){
          try(Statement command=conn.createStatement()){
              ResultSet result=command.executeQuery("select count(*) from department");
              result.next();
             return result.getInt("count(*)");
          }
      }
 }
 public Integer countWorkSites() throws SQLException{
      try(Connection conn=db.getConnection()){
          try(Statement command=conn.createStatement()){
              ResultSet result=command.executeQuery("select count(*) from worksite");
              result.next();
             return result.getInt("count(*)");
          }
      }
 }

 public Map<EmployeeData, List<EmployeeData>> findEmployeesHierarchy() throws SQLException {

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
}
