package com.grauman.amdocs.controllers;

import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.EmployeeDataDAO;
import com.grauman.amdocs.models.Country;
import com.grauman.amdocs.models.Department;
import com.grauman.amdocs.models.Employee;
import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.WorkSite;

@RestController
@RequestMapping("/employee")
@CrossOrigin
public class EmployeeDataController {
	@Autowired
	private EmployeeDataDAO employeeDataDAO;
	/**
	    * @param page
	    * @param limit
	    * @return all locked employees
	    * @throws SQLException
	    */
//All Employees Which are locked
	@GetMapping("/locked")
	public ResponseEntity<List<EmployeeData>> allLocked(@RequestParam int page,@RequestParam int limit) throws SQLException {
		List<EmployeeData> employee= employeeDataDAO.findAll(page,limit);
		return ResponseEntity.ok().body(employee);
		}
	/**
	    * @param page
	    * @param limit
	    * @return all employees
	    * @throws SQLException
	    */
//All Employees	
	@GetMapping("")
	public ResponseEntity<List<EmployeeData>> all(@RequestParam int page,@RequestParam int limit) throws SQLException {
		List<EmployeeData> employee= employeeDataDAO.findAllEmployees(page,limit);
		return ResponseEntity.ok().body(employee);
	}
	/**
	    * @param id 
	    * @return find employee by id
	    * @throws SQLException
	    */
//Employee profile
	@GetMapping("/id")
	public ResponseEntity<EmployeeData> findEmployeeProfile(@RequestParam int id) throws SQLException {
		EmployeeData employee=employeeDataDAO.searchEmployeeProfile(id);
		return ResponseEntity.ok().body(employee);
	}
//Employee by employee id
//	@GetMapping("/id")
//	public ResponseEntity<EmployeeData> find(@RequestParam int id) throws SQLException {
//		EmployeeData employee=employeeDataDAO.find(id);
//		return ResponseEntity.ok().body(employee);
//	}
	/**
	    * @param employee 
	    * @return new added employee
	    * @throws SQLException
	    */
//Add Employee
	@PostMapping("")
	public ResponseEntity<EmployeeData> newEmployee(@RequestBody EmployeeData employee) throws SQLException{
		EmployeeData employeeResult = employeeDataDAO.add(employee);
		return ResponseEntity.ok().body(employeeResult);
	}
	/**
	    * @param id 
	    * @param employee
	    * @return update employee
	    * @throws SQLException
	    */
//Update Employee
	@PutMapping("/id")
	public ResponseEntity<EmployeeData> updateEmployeeInfo(@RequestParam int id, @RequestBody EmployeeData employee) throws SQLException {
		EmployeeData employeeByID = employeeDataDAO.update(employee);
		return ResponseEntity.ok().body(employeeByID); 
	}
	/**
	    * @param id 
	    * @return deleted/deactivated employee
	    * @throws SQLException
	    */
//Delete Employee
	@DeleteMapping("/id")
	public ResponseEntity<EmployeeData> deleteEmployee(@RequestParam Integer id) throws SQLException {
		EmployeeData deletedEmployee = employeeDataDAO.delete(id);
		return ResponseEntity.ok().body(deletedEmployee);   
	}
	@GetMapping("/number")
	public ResponseEntity<List<EmployeeData>> findByNumber(@RequestParam Integer number) throws SQLException {
		List<EmployeeData> employeeByName=employeeDataDAO.filterByNumber(number);
		return ResponseEntity.ok().body(employeeByName);
		}
	@GetMapping("/name")
	public ResponseEntity<List<EmployeeData>> findByName(@RequestParam String name,@RequestParam int page,@RequestParam int limit) throws SQLException {
		List<EmployeeData> employeeByName=employeeDataDAO.filterByName(name,page,limit);
		return ResponseEntity.ok().body(employeeByName);
		}
	
	@GetMapping("/role")
	public ResponseEntity<List<EmployeeData>> findByRole(@RequestParam String role,@RequestParam int page,@RequestParam int limit) throws SQLException {
		List<EmployeeData> employeeByRole=employeeDataDAO.filterByRole(role,page,limit);
		return ResponseEntity.ok().body(employeeByRole);
	}
	@GetMapping("/{department}")
	public ResponseEntity<List<EmployeeData>> findByDepartment(@PathVariable String department,@RequestParam int page,@RequestParam int limit) throws SQLException {
		List<EmployeeData> employeeByDepartment=employeeDataDAO.filterByDepartment(department,page,limit);
		return ResponseEntity.ok().body(employeeByDepartment);
	}
	@GetMapping("/worksite")
	public ResponseEntity<List<EmployeeData>> findByWorkSite(@RequestParam String worksite,@RequestParam int page,@RequestParam int limit) throws SQLException {
		List<EmployeeData> employeeByWorSite=employeeDataDAO.filterByWorkSite(worksite,page,limit);
		return ResponseEntity.ok().body(employeeByWorSite);
	}
	@GetMapping("/country")
	public ResponseEntity<List<EmployeeData>> findByCountry(@RequestParam String country,@RequestParam int page,@RequestParam int limit) throws SQLException {
		List<EmployeeData> employeeByWorSite=employeeDataDAO.filterByCountry(country,page,limit);
		return ResponseEntity.ok().body(employeeByWorSite);
	}
	/**
	    * @param id 
	    * @return unlocked employee 
	    * @throws SQLException
	    */
//Unlock Employee
	@PutMapping("/unlock/id")
	public ResponseEntity<EmployeeData>  unlockEmployee(@RequestParam Integer id) throws SQLException {
		EmployeeData unlockedEmployee = employeeDataDAO.unlockEmployee(id);
		return ResponseEntity.ok().body(unlockedEmployee);    
	}
	/**
	    * @param id 
	    * @return locked employee 
	    * @throws SQLException
	    */
//lock Employee	
	@PutMapping("/lock/id")
	public ResponseEntity<EmployeeData>  lockEmployee(@RequestParam Integer id) throws SQLException {
		EmployeeData lockedEmployee = employeeDataDAO.lockEmployee(id);
		return ResponseEntity.ok().body(lockedEmployee);    
	}
//################################################################################
	/** 
	    * @return all sites 
	    * @throws SQLException
	    */
//Select Work Sites
	@GetMapping("/worksites")
	public ResponseEntity<List<WorkSite>> allSites() throws SQLException {
		List<WorkSite> workSites=employeeDataDAO.findAllSites();
		return ResponseEntity.ok().body(workSites);
	}
	/** 
	    * @return all roles 
	    * @throws SQLException
	    */
//Select Roles
	@GetMapping("/roles")
    public ResponseEntity<List<Role>> allRoles() throws SQLException {
        List<Role> roles= employeeDataDAO.findAllRoles();
		return ResponseEntity.ok().body(roles);

    }
	/** 
	    * @return all departments 
	    * @throws SQLException
	    */
//Select Departments
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> allDepartments() throws SQLException {
    	List<Department> departments=employeeDataDAO.findAllDepartments();
		return ResponseEntity.ok().body(departments);

    }
    /** 
	    * @return all managers 
	    * @throws SQLException
	    */
//Select Managers	
    @GetMapping("/managers")
    public ResponseEntity<List<Employee>> allManagers() throws SQLException {
    	List<Employee>managers= employeeDataDAO.findAllManagers();
    	return ResponseEntity.ok().body(managers);
    }
    /** 
	    * @return all countries 
	    * @throws SQLException
	    */
//Select countries	
    @GetMapping("/countries")
    public ResponseEntity<List<Country>> allCountries() throws SQLException {
    	List<Country>countries= employeeDataDAO.findAllCountries();
    	return ResponseEntity.ok().body(countries);
    }
    /** 
	    * @return number of employees 
	    * @throws SQLException
	    */
//Number Of Employees
    @GetMapping("/countEmployees")
    public Integer numberOfEmployees() throws SQLException {
    	Integer Counter= employeeDataDAO.countEmployees();
    	return Counter;
    }
    /** 
	    * @return number of roles 
	    * @throws SQLException
	    */
//Number Of Roles
    @GetMapping("/countRoles")
    public Integer numberOfRoles() throws SQLException {
    	Integer Counter= employeeDataDAO.countRoles();
    	return Counter;
    }
    /** 
	    * @return number of departments 
	    * @throws SQLException
	    */
//Number Of Departments
    @GetMapping("/countDepartments")
    public Integer numberOfDepartments() throws SQLException {
    	Integer Counter= employeeDataDAO.countDepartments();
    	return Counter;
    }
    /** 
	    * @return number of countries 
	    * @throws SQLException
	    */
//Number Of Countries
    @GetMapping("/countWorkSites")
    public Integer numberOfWorkSites() throws SQLException {
    	Integer Counter= employeeDataDAO.countWorkSites();
    	return Counter;
    }
}
