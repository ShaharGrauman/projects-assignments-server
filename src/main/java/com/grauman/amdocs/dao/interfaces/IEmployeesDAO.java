package com.grauman.amdocs.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.grauman.amdocs.models.Employee;
import com.grauman.amdocs.models.WorkSite;

public interface IEmployeesDAO extends IDAO<Employee>{
	List<WorkSite> findAllSites() throws SQLException;
}
