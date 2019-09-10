package com.grauman.amdocs.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.EmployeeDataDAO;
import com.grauman.amdocs.models.Department;
import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.WorkSite;

@RestController
@RequestMapping("/employee")
public class EmployeeDataController {
	@Autowired
	private EmployeeDataDAO employeeDataDAO;
//Employee
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeData> findEmployeeById(@PathVariable int id) throws SQLException {
		EmployeeData employee=employeeDataDAO.find(id);
		return ResponseEntity.ok().body(employee);
	}
//Add Employee
    @PostMapping("")
    public ResponseEntity<EmployeeData> newEmployee(@RequestBody EmployeeData employee) throws SQLException{
    	EmployeeData employeeResult = employeeDataDAO.add(employee);
    	return ResponseEntity.ok().body(employeeResult);
    }
//Update Employee
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeData> updateEmployeeInfo(@PathVariable int id, @RequestBody EmployeeData employee) throws SQLException {
    	EmployeeData employeeByID = employeeDataDAO.update(employee);
    	return ResponseEntity.ok().body(employeeByID); 
}

//Delete Employee
    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeData> deleteEmployee(@PathVariable Integer id) throws SQLException {
    	EmployeeData deletedEmployee = employeeDataDAO.delete(id);
    	return ResponseEntity.ok().body(deletedEmployee);   
    	}
    
//Unlock Employee
    @PutMapping("/unlock/{id}")
    public ResponseEntity<EmployeeData>  unlockEmployee(@PathVariable Integer id) throws SQLException {
    	EmployeeData unlockedEmployee = employeeDataDAO.unlock(id);
    	return ResponseEntity.ok().body(unlockedEmployee);    
    	}
    
//Select Work Sites
	@GetMapping("/WorkSites")
	public ResponseEntity<List<WorkSite>> allSites() throws SQLException {
		List<WorkSite> workSites=employeeDataDAO.findAllSites();
		return ResponseEntity.ok().body(workSites);
	}
//Select Roles
	@GetMapping("/roles")
    public ResponseEntity<List<Role>> allRoles() throws SQLException {
        List<Role> roles= employeeDataDAO.findAllRoles();
		return ResponseEntity.ok().body(roles);

    }

//Select Departments
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> allDepartments() throws SQLException {
    	List<Department> departments=employeeDataDAO.findAllDepartments();
		return ResponseEntity.ok().body(departments);

    }
//Select Managers	
    @GetMapping("/Managers")
    public ResponseEntity<List<EmployeeData>> allManagers() throws SQLException {
    	List<EmployeeData>managers= employeeDataDAO.findAllManagers();
    	return ResponseEntity.ok().body(managers);
    }
}
