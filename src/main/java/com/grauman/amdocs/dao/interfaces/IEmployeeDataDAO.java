package com.grauman.amdocs.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.grauman.amdocs.models.Country;
import com.grauman.amdocs.models.Department;
import com.grauman.amdocs.models.Employee;
import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.WorkSite;
import com.grauman.amdocs.models.vm.EmployeeInSession;

public interface IEmployeeDataDAO extends IDAO<EmployeeData>{
	public List<EmployeeData> findAllEmployees(int page,int limit) throws SQLException;
	
	public EmployeeData searchEmployeeProfile(int id) throws SQLException;
	public List<EmployeeData> findAll(int page,int limit) throws SQLException;
	public List<EmployeeData> filterByNumber(int number) throws SQLException;
    List<EmployeeData> filterByName(String name,int page,int limit) throws SQLException;
    List<EmployeeData> filterByRole(String roleName,int page,int limit) throws SQLException;
    List<EmployeeData> filterByDepartment(String departmentName,int page,int limit) throws SQLException ;
    List<EmployeeData> filterByWorkSite(String siteName,int page,int limit) throws SQLException ;
    List<EmployeeData> filterByCountry(String countryName,int page,int limit)throws SQLException;
	
	List<WorkSite> findAllSites() throws SQLException;
	List<Role> findAllRoles() throws SQLException;
	List<Department> findAllDepartments() throws SQLException;
	List<Employee> findAllManagers() throws SQLException;
	List<Country> findAllCountries() throws SQLException;
	
	public Map<EmployeeData, List<EmployeeData>> findEmployeesHierarchy() throws SQLException;
	public EmployeeData lockEmployee(int id) throws SQLException ;
	public EmployeeData unlockEmployee(int id) throws SQLException;


//	 List<EmployeeData> getEmployeesByManagerID (int managerID,int pageNumber,int limit) throws SQLException;
//	 List<EmployeeData> getEmployeesByProjectID(int projectid) throws SQLException;
//	 List<EmployeeData> searchEmployeesBySkillID(int skillID, int pageNumber, int limit) throws SQLException;
//	 List<EmployeeData> searchEmployeesBySkillSet(List<Integer> skillSet, int pageNumber, int limit) throws SQLException;

	 
	 
	 List<Role> getEmployeeRoles(int id)throws SQLException;
	 
	 
	 Integer countEmployees() throws SQLException;
	 Integer countRoles() throws SQLException;
	 Integer countDepartments() throws SQLException;
	 Integer countWorkSites() throws SQLException;

	EmployeeInSession findEmployeeByEmail(String username) throws SQLException;
}
