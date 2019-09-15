package com.grauman.amdocs.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.grauman.amdocs.models.Country;
import com.grauman.amdocs.models.Department;
import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.WorkSite;

public interface IEmployeeDataDAO extends IDAO<EmployeeData>{
	//List<EmployeeData> findAll()throws SQLException;
	
	EmployeeData findByEmployeeNumber(int number) throws SQLException;
	
	
	List<EmployeeData> filterByName(String name) throws SQLException;
	List<EmployeeData> filterByRole(String roleName);
	List<EmployeeData> filterByDepartment(String departmentName);
	List<EmployeeData> filterByWorkSite(String siteName);
	List<EmployeeData> filterByCountry(String countryName)throws SQLException;
	
	List<WorkSite> findAllSites() throws SQLException;
	List<Role> findAllRoles() throws SQLException;
	List<Department> findAllDepartments() throws SQLException;
	List<EmployeeData> findAllManagers() throws SQLException;
	List<Country> findAllCountries() throws SQLException;
	
	
	 EmployeeData unlock(int id) throws SQLException;
	 
	 
	 List<Role> getEmployeeRoles(int id)throws SQLException;
	 
	 
	 Integer countEmployees() throws SQLException;
	 Integer countRoles() throws SQLException;
	 Integer countDepartments() throws SQLException;
	 Integer countWorkSites() throws SQLException;
}
