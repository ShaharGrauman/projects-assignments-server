package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IEmployeesDAO;
import com.grauman.amdocs.models.Employee;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.WorkSite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeesDAO implements IEmployeesDAO {
    @Autowired DBManager db;

	
    @Override
    public List<Employee> findAll() throws SQLException {
    	
    	List<Employee> users=new ArrayList<Employee>();
		String sqlAllUserscommand="select U.id,U.employee_number,concat(U.first_name,' ',U.last_name) as name,"
								+ "U.department,concat(WS.name,C.name) as work_site"
								+ " From users U JOIN worksite WS ON U.work_site_id=WS.id"
								+ " JOIN country C ON WS.country_id=C.id";
		try (Connection conn = db.getConnection()) {
			try(Statement command = conn.createStatement()){
				ResultSet result=command.executeQuery(sqlAllUserscommand);
				while(result.next()) {
					users.add(
							new Employee(
									result.getInt("U.id"),
									result.getInt("U.employee_number"),
									result.getString("name"),
									result.getString("U.department"),
									result.getString("work_site")
									));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return users;
		}

  //Advanced Search
  //*********************************************************************************************
  //Employee's Roles
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
  //By Name
  		public List<Employee> find(String name) throws SQLException {
  			
  			List <Employee> found = new ArrayList<>();
  			List<Role> employeeRoles=new ArrayList<>();

  			String sqlFindCommand ="SELECT U.id, U.employee_number,CONCAT(U.first_name,' ',U.last_name) as userName "
  					+ ",W.name as workSiteName, U.department "
  					+ "FROM users U join worksite W on U.work_site_id=W.id "
  					+ "having userName=?";
  			
  			try (Connection conn = db.getConnection()) {
  				try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
  				 command.setString(1,name);
  					ResultSet result = command.executeQuery();
  					if(result.next()) {
  						employeeRoles=getEmployeeRoles(result.getInt(1));
  						found.add(new Employee(
  								result.getInt(1),
  								result.getInt(2),
  								result.getString(3),
  								employeeRoles,
  								result.getString(4),
  								result.getString(5)
  								));
  					}
  				}}
  			 catch (Exception e) {
  				e.printStackTrace();
  			}
  			return found;
  		}
  //By Role
  	public List<Employee> filterByRole(String roleName){
  		List <Employee> found = new ArrayList<>();
  		List<Role> employeeRoles=new ArrayList<>();

  		String sqlFindCommand ="SELECT U.id, U.employee_number,CONCAT(U.first_name,' ',U.last_name) as userName,"
  				+ "W.name as workSiteName, U.department"
  				+ " FROM users U JOIN worksite W ON U.work_site_id=W.id"
  				+ " JOIN userrole UR ON UR.user_id=U.id"
  				+ " JOIN roles R ON R.id=UR.role_id"
  				+ " where R.name=?";
  		try (Connection conn = db.getConnection()) {
  			try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
  			 command.setString(1,roleName);
  				ResultSet result = command.executeQuery();
  			
  				if(result.next()) {
  					employeeRoles=getEmployeeRoles(result.getInt(1));
  					found.add(new Employee(
  							result.getInt(1),
  							result.getInt(2),
  							result.getString(3),
  							employeeRoles,
  							result.getString(4),
  							result.getString(5)
  							));
  				}
  			}
  		} 
  		 catch (Exception e) {
  			e.printStackTrace();
  		}
  		return found;
  	}
  	
  //By Department
  	public List<Employee> filterByDepartment(String departmentName){
  		List <Employee> found = new ArrayList<>();
  		List<Role> employeeRoles=new ArrayList<>();

  		String sqlFindCommand ="SELECT U.id, U.employee_number,CONCAT(U.first_name,' ',U.last_name) as userName,"
  				+ "W.name as workSiteName, U.department"
  				+ " FROM users U JOIN worksite W ON U.work_site_id=W.id"
  				+ " JOIN userrole UR ON UR.user_id=U.id"
  				+ " JOIN roles R ON R.id=UR.role_id"
  				+ " where U.department=?";
  		try (Connection conn = db.getConnection()) {
  			try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
  			 command.setString(1,departmentName);
  				ResultSet result = command.executeQuery();
  			
  				if(result.next()) {
  					employeeRoles=getEmployeeRoles(result.getInt(1));
  					found.add(new Employee(
  							result.getInt(1),
  							result.getInt(2),
  							result.getString(3),
  							employeeRoles,
  							result.getString(4),
  							result.getString(5)
  							));
  				}
  			}
  		} 
  		 catch (Exception e) {
  			e.printStackTrace();
  		}
  		return found;
  	}
  //By WorkSite
  	public List<Employee> filterByWorkSite(String siteName){
  		List <Employee> found = new ArrayList<>();
  		List<Role> employeeRoles=new ArrayList<>();

  		String sqlFindCommand ="SELECT U.id, U.employee_number,CONCAT(U.first_name,' ',U.last_name) as userName,"
  				+ "W.name as workSiteName, U.department"
  				+ " FROM users U JOIN worksite W ON U.work_site_id=W.id"
  				+ " JOIN userrole UR ON UR.user_id=U.id"
  				+ " JOIN roles R ON R.id=UR.role_id"
  				+ " where W.name=?";
  		try (Connection conn = db.getConnection()) {
  			try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)) {
  			 command.setString(1,siteName);
  				ResultSet result = command.executeQuery();
  			
  				if(result.next()) {
  					employeeRoles=getEmployeeRoles(result.getInt(1));
  					found.add(new Employee(
  							result.getInt(1),
  							result.getInt(2),
  							result.getString(3),
  							employeeRoles,
  							result.getString(4),
  							result.getString(5)
  							));
  				}
  			}
  		} 
  		 catch (Exception e) {
  			e.printStackTrace();
  		}
  		return found;
  	}
 //*************************************************************
  	//Country
 	
  	
    @Override
    public Employee find(int id) throws SQLException {
        return null;
    }

    @Override
    public Employee add(Employee movie) throws SQLException {
        return null;
    }

    @Override
    public Employee update(Employee movie) throws SQLException {
        return null;
    }

    @Override
    public Employee delete(int id) throws SQLException {
        return null;
    }

	@Override
	public List<WorkSite> findAllSites() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
