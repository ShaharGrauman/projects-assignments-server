package com.grauman.amdocs.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.grauman.amdocs.models.Country;
import com.grauman.amdocs.models.Department;
import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.WorkSite;

public interface IEmployeeDataDAO extends IDAO<EmployeeData>{
	public List<EmployeeData> findAllEmployees() throws SQLException;
	
	EmployeeData findEmployeeById(int id) throws SQLException;
	
	
	List<EmployeeData> filterByName(String name) throws SQLException;
	List<EmployeeData> filterByRole(String roleName)throws SQLException;
	List<EmployeeData> filterByDepartment(String departmentName)throws SQLException;
	List<EmployeeData> filterByWorkSite(String siteName)throws SQLException;
	List<EmployeeData> filterByCountry(String countryName)throws SQLException;
	
	List<WorkSite> findAllSites() throws SQLException;
	List<Role> findAllRoles() throws SQLException;
	List<Department> findAllDepartments() throws SQLException;
	List<EmployeeData> findAllManagers() throws SQLException;
	List<Country> findAllCountries() throws SQLException;
	
	
	 EmployeeData unlock(int id) throws SQLException;


//	 List<EmployeeData> getEmployeesByManagerID (int managerID,int pageNumber,int limit) throws SQLException;
//	 List<EmployeeData> getEmployeesByProjectID(int projectid) throws SQLException;
//	 List<EmployeeData> searchEmployeesBySkillID(int skillID, int pageNumber, int limit) throws SQLException;
//	 List<EmployeeData> searchEmployeesBySkillSet(List<Integer> skillSet, int pageNumber, int limit) throws SQLException;

	 
	 
	 List<Role> getEmployeeRoles(int id)throws SQLException;
	 
	 
	 Integer countEmployees() throws SQLException;
	 Integer countRoles() throws SQLException;
	 Integer countDepartments() throws SQLException;
	 Integer countWorkSites() throws SQLException;

}
