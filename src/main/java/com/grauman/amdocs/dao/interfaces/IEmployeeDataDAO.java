package com.grauman.amdocs.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.grauman.amdocs.models.Department;
import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.WorkSite;

public interface IEmployeeDataDAO extends IDAO<EmployeeData>{
	 EmployeeData unlock(int id) throws SQLException;
	 List<WorkSite> findAllSites() throws SQLException;
	 List<Role> findAllRoles() throws SQLException;
	 List<Department> findAllDepartments() throws SQLException;
	 List<EmployeeData> findAllManagers() throws SQLException;
}
